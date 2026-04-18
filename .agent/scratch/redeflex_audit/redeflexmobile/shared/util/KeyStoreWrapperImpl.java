package com.axys.redeflexmobile.shared.util;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.axys.redeflexmobile.BuildConfig;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Calendar;

import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

// https://proandroiddev.com/secure-data-in-android-encryption-in-android-part-2-991a89e55a23
public class KeyStoreWrapperImpl implements KeyStoreWrapper {

    public static final String ALIAS = BuildConfig.APPLICATION_ID;
    private static final String KEYSTORE_TYPE = "AndroidKeyStore";
    private static final String ALGORITHM = "RSA";
    private static final String ALGORITHM_AES = "AES";
    private static final String DIGEST_INSTANCE = "SHA-1";
    private static final int BYTE_SIZE = 16;
    private static final int DUE_YEAR = 30;
    private final Context context;

    public KeyStoreWrapperImpl(Context context) {
        this.context = context;
    }

    public SecretKeySpec getAnotherKeys() throws NoSuchAlgorithmException {
        byte[] binary = ALIAS.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance(DIGEST_INSTANCE);
        binary = sha.digest(binary);
        binary = Arrays.copyOf(binary, BYTE_SIZE);
        return new SecretKeySpec(binary, ALGORITHM_AES);
    }

    @Override
    @Nullable
    public KeyPair getPairKey(String alias) throws CertificateException,
            NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException {
        KeyStore keyStore = getKeyStore();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, null);
        PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();

        if (privateKey != null && publicKey != null) {
            return new KeyPair(publicKey, privateKey);
        }

        return null;
    }

    @Override
    public KeyPair createKeyStore(String alias) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException, CertificateException,
            KeyStoreException, IOException, UnrecoverableKeyException {
        KeyStore keyStore = getKeyStore();
        if  (keyStore.containsAlias(alias)) return getPairKey(alias);

        KeyPairGenerator instance = KeyPairGenerator.getInstance(ALGORITHM, KEYSTORE_TYPE);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            initGeneratorBeforeAndroidM(instance, alias);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initGeneratorAfterAndroidM(instance, alias);
        }

        return instance.generateKeyPair();
    }

    private KeyStore getKeyStore() throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException {
        KeyStore androidKeyStore = KeyStore.getInstance(KEYSTORE_TYPE);
        androidKeyStore.load(null);
        return androidKeyStore;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initGeneratorBeforeAndroidM(KeyPairGenerator keyPairGenerator, String alias)
            throws InvalidAlgorithmParameterException {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        end.add(Calendar.YEAR, DUE_YEAR);

        KeyPairGeneratorSpec.Builder builder = new KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSerialNumber(BigInteger.ONE)
                .setSubject(new X500Principal("CN=" + alias + " CA Certificate"))
                .setStartDate(start.getTime())
                .setEndDate(end.getTime());

        keyPairGenerator.initialize(builder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initGeneratorAfterAndroidM(KeyPairGenerator keyPairGenerator, String alias)
            throws InvalidAlgorithmParameterException {
        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec
                .Builder(alias, KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1);

        keyPairGenerator.initialize(builder.build());
    }
}
