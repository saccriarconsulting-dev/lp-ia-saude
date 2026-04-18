package com.axys.redeflexmobile.shared.util;

import android.util.Base64;

import com.axys.redeflexmobile.BuildConfig;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import timber.log.Timber;

// https://proandroiddev.com/secure-data-in-android-encryption-in-android-part-2-991a89e55a23
public class CipherWrapperImpl implements CipherWrapper {

    private static final String ALIAS = "RFM" + BuildConfig.APPLICATION_ID;
    private static final String DIGEST_INSTANCE = "SHA-1";
    private static final int BYTE_SIZE = 16;

    private Cipher cipher;

    public CipherWrapperImpl(CipherTransformation cipherTransformation) {
        try {
            this.cipher = Cipher.getInstance(cipherTransformation.getValue());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            Timber.e(e);
        }
    }

    @Override
    public String encrypt(String data, PublicKey key) throws BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] text = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeToString(text, Base64.DEFAULT);
    }

    @Override
    public String decrypt(String data, PrivateKey key) throws BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException {
        byte[] text = Base64.decode(data, Base64.DEFAULT);
        cipher.init(Cipher.DECRYPT_MODE, key);

        return new String(cipher.doFinal(text), StandardCharsets.UTF_8);
    }

    @Override
    public String encrypt(String data, SecretKeySpec key) throws BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException {
        cipher.init(Cipher.ENCRYPT_MODE, key, generateIv());
        byte[] text = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeToString(text, Base64.DEFAULT);
    }

    @Override
    public String decrypt(String data, SecretKeySpec key) throws InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException {
        byte[] text = Base64.decode(data, Base64.DEFAULT);
        cipher.init(Cipher.DECRYPT_MODE, key, generateIv());

        return new String(cipher.doFinal(text), StandardCharsets.UTF_8);
    }

    @Override
    public String hash(String data) throws NoSuchAlgorithmException {
        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = algorithm.digest(data.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(messageDigest, Base64.NO_WRAP);
    }

    private IvParameterSpec generateIv() throws NoSuchAlgorithmException {
        byte[] binary = ALIAS.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance(DIGEST_INSTANCE);
        binary = sha.digest(binary);
        binary = Arrays.copyOf(binary, BYTE_SIZE);
        return new IvParameterSpec(binary);
    }
}
