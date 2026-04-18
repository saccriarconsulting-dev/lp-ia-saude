package com.axys.redeflexmobile.shared.util.exception;

public class GenericValidationException extends NullPointerException {

    private int messageInteger;
    private String messageString;
    private final GenericValidationExceptionType type;

    public GenericValidationException(int messageInteger, GenericValidationExceptionType type) {
        super();
        this.messageInteger = messageInteger;
        this.type = type;
    }

    public GenericValidationException(String messageString, GenericValidationExceptionType type) {
        super();
        this.messageString = messageString;
        this.type = type;
    }

    public int getMessageInteger() {
        return messageInteger;
    }

    public String getMessageString() {
        return messageString;
    }

    public GenericValidationExceptionType getType() {
        return type;
    }

    public enum GenericValidationExceptionType {
        SETTINGS_ALERT, MOCK_LOCATION, WRONG_DATE
    }
}
