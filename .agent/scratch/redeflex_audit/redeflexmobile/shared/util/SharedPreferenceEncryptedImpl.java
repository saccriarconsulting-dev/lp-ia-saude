package com.axys.redeflexmobile.shared.util;

import com.axys.redeflexmobile.shared.bd.DBCredencial;
import com.axys.redeflexmobile.shared.models.login.Login;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

import io.reactivex.Completable;
import io.reactivex.Single;

public class SharedPreferenceEncryptedImpl implements SharedPreferenceEncrypted {

    private final CipherWrapper cipherWrapper;
    private final KeyStoreWrapper keyStoreWrapper;
    private final DBCredencial dbCredencial;

    public SharedPreferenceEncryptedImpl(CipherWrapper cipherWrapper,
                                         KeyStoreWrapper keyStoreWrapper,
                                         DBCredencial dbCredencial) {
        this.cipherWrapper = cipherWrapper;
        this.keyStoreWrapper = keyStoreWrapper;
        this.dbCredencial = dbCredencial;
    }

    @Override
    public Completable saveLogin(Login login) {
        return Completable.defer(() -> {
            keyStoreWrapper.createKeyStore(KeyStoreWrapperImpl.ALIAS);
            KeyPair keyPair = keyStoreWrapper.getPairKey(KeyStoreWrapperImpl.ALIAS);

            if (keyPair == null)
                return Completable.error(new IllegalStateException("Invalid Pair of Keys"));

            dbCredencial.adicionar(new Login(
                    encrypt(login.getUuid(), keyStoreWrapper.getAnotherKeys()),
                    encrypt(login.getUsername(), keyStoreWrapper.getAnotherKeys()),
                    encrypt(login.getPassword(), keyStoreWrapper.getAnotherKeys())
            ));

            return Completable.complete();
        });
    }

    @Override
    public Single<Login> getLogin() {
        return Single.defer(() -> {
            keyStoreWrapper.createKeyStore(KeyStoreWrapperImpl.ALIAS);
            KeyPair keyPair = keyStoreWrapper.getPairKey(KeyStoreWrapperImpl.ALIAS);

            if (keyPair == null)
                return Single.error(new IllegalStateException("Invalid Pair of Keys"));

            Login login = dbCredencial.obter();
            if (login == null)
                return Single.error(new IllegalStateException("Não existe usuário salvo no seu dispositivo, tente fazer o login online."));

            return Single.just(new Login(
                    decrypt(login.getUuid(), keyStoreWrapper.getAnotherKeys()),
                    decrypt(login.getUsername(), keyStoreWrapper.getAnotherKeys()),
                    decrypt(login.getPassword(), keyStoreWrapper.getAnotherKeys())
            ));
        });
    }

    @Override
    public Login getSimpleLogin() throws CertificateException,
            UnrecoverableKeyException, NoSuchAlgorithmException,
            KeyStoreException, NoSuchProviderException,
            InvalidAlgorithmParameterException, IOException,
            BadPaddingException, InvalidKeyException,
            IllegalBlockSizeException {
        keyStoreWrapper.createKeyStore(KeyStoreWrapperImpl.ALIAS);
        KeyPair keyPair = keyStoreWrapper.getPairKey(KeyStoreWrapperImpl.ALIAS);

        if (keyPair == null) throw new IllegalStateException("Invalid Pair of Keys");

        Login login = dbCredencial.obter();
        if (login == null) return new Login(null, null, null);

        return new Login(
                decrypt(login.getUuid(), keyStoreWrapper.getAnotherKeys()),
                decrypt(login.getUsername(), keyStoreWrapper.getAnotherKeys()),
                decrypt(login.getPassword(), keyStoreWrapper.getAnotherKeys())
        );
    }

    private String encrypt(String message, SecretKeySpec publicKey) throws BadPaddingException,
            InvalidKeyException, IllegalBlockSizeException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException {
        if (StringUtils.isEmpty(message)) return null;

        return cipherWrapper.encrypt(message, publicKey);
    }

    private String decrypt(String message, SecretKeySpec privateKey) throws BadPaddingException,
            InvalidKeyException, IllegalBlockSizeException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException {
        if (StringUtils.isEmpty(message)) return null;

        return cipherWrapper.decrypt(message, privateKey);
    }
}
