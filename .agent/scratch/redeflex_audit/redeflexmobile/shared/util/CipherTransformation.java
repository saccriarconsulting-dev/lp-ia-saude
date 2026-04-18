package com.axys.redeflexmobile.shared.util;

public enum CipherTransformation {
    TRANSFORMATION_ASYMMETRIC("RSA/ECB/PKCS1Padding"),
    TRANSFORMATION_PADDING("AES/CBC/PKCS5Padding");

    private final String value;

    CipherTransformation(String transformation) {
        value = transformation;
    }

    String getValue() {
        return value;
    }
}
