package com.axys.redeflexmobile.shared.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

public interface CipherWrapper {

    String encrypt(String data, PublicKey key) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException;

    String decrypt(String data, PrivateKey key) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException;

    String encrypt(String data, SecretKeySpec key) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException;

    String decrypt(String data, SecretKeySpec key) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, NoSuchAlgorithmException;

    String hash(String data) throws NoSuchAlgorithmException;
}
