package com.axys.redeflexmobile.shared.util;

import com.axys.redeflexmobile.shared.models.login.Login;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface SharedPreferenceEncrypted {

    Completable saveLogin(Login login);

    Single<Login> getLogin();

    Login getSimpleLogin() throws CertificateException, UnrecoverableKeyException,
            NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException,
            InvalidAlgorithmParameterException, IOException, BadPaddingException,
            InvalidKeyException, IllegalBlockSizeException;
}
