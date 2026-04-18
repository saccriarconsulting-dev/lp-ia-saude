package com.axys.redeflexmobile.shared.util;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.spec.SecretKeySpec;

public interface KeyStoreWrapper {

    @Nullable
    KeyPair getPairKey(String alias) throws CertificateException,
            NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException;

    KeyPair createKeyStore(String alias) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException, CertificateException, KeyStoreException, IOException, UnrecoverableKeyException;

    SecretKeySpec getAnotherKeys() throws NoSuchAlgorithmException;
}
