package com.axys.redeflexmobile.ui.component.customedittext;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.SINGLE_INT;
import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;

/**
 * Created by Vitor Herrmann on 01/06/18.
 */
public class PhoneMask implements TextWatcher {

    private static final int NUMBER_TWO = 2;
    private static final int NUMBER_FOUR = 4;
    private static final int NUMBER_FIVE = 5;
    private static final int NUMBER_NINE = 9;

    private static final String PARENTHESES_LEFT = "(";
    private static final String PARENTHESES_RIGHT = ") ";
    private static final String HYPHEN = "-";
    private int maxLength = NUMBER_NINE + NUMBER_TWO;
    private boolean mFormatting;
    private boolean clearFlag;
    private int mLastStartLocation;
    private String mLastBeforeText;
    private WeakReference<EditText> mWeakEditText;
    private boolean credential;

    public PhoneMask(WeakReference<EditText> weakEditText, String maskValue) {
        this.mWeakEditText = weakEditText;
        if (maskValue.equals("TELEPHONE"))
            this.maxLength = 10;
        else
            this.maxLength = 11;
    }

    public PhoneMask(WeakReference<EditText> weakEditText, boolean credential) {
        this.mWeakEditText = weakEditText;
        this.credential = credential;

        if (credential) {
            this.maxLength = NUMBER_NINE;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (after == EMPTY_INT && s.toString().equals(PARENTHESES_LEFT)) {
            clearFlag = true;
        }
        mLastStartLocation = start;
        mLastBeforeText = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //unused
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!mFormatting) {
            mFormatting = true;
            int curPos = mLastStartLocation;
            String beforeValue = mLastBeforeText;
            String currentValue = s.toString();
            String formattedValue = formatUsNumber(s);
            if (currentValue.length() > beforeValue.length()) {
                int setCursorPos = formattedValue.length()
                        - (beforeValue.length() - curPos);
                mWeakEditText.get().setSelection(setCursorPos < EMPTY_INT ?
                        EMPTY_INT : setCursorPos);
            } else {
                int setCursorPos = formattedValue.length()
                        - (currentValue.length() - curPos);
                if (setCursorPos > EMPTY_INT &&
                        !Character.isDigit(formattedValue.charAt(setCursorPos - SINGLE_INT))) {
                    setCursorPos--;
                }
                mWeakEditText.get().setSelection(setCursorPos < EMPTY_INT ? EMPTY_INT : setCursorPos);
            }
            mFormatting = false;
        }
    }

    private String formatUsNumber(Editable text) {
        StringBuilder formattedString = new StringBuilder();
        int p = EMPTY_INT;
        while (p < text.length()) {
            char ch = text.charAt(p);
            if (!Character.isDigit(ch)) {
                text.delete(p, p + SINGLE_INT);
            } else {
                p++;
            }
        }
        String allDigitString = text.toString();
        int totalDigitCount = allDigitString.length();
        if (totalDigitCount > maxLength) {
            allDigitString = allDigitString.substring(EMPTY_INT, maxLength);
            totalDigitCount--;
        }
        boolean isLonger = totalDigitCount == maxLength;
        int dashAfter = getDash(isLonger, maxLength);

        if (checkTotalDigitCountNotCredential(totalDigitCount, allDigitString)) {
            text.clear();
            text.append(allDigitString);
            return allDigitString;
        }

        int alreadyPlacedDigitCount = EMPTY_INT;
        if (!credential) {
            if (checkAllDigit(allDigitString)) {
                text.clear();
                clearFlag = false;
                return EMPTY_STRING;
            }
            if (totalDigitCount - alreadyPlacedDigitCount > NUMBER_TWO) {
                formattedString.append(PARENTHESES_LEFT)
                        .append(allDigitString.substring(alreadyPlacedDigitCount,
                                alreadyPlacedDigitCount + NUMBER_TWO)).append(PARENTHESES_RIGHT);
                alreadyPlacedDigitCount += NUMBER_TWO;
            }
        }
        if (totalDigitCount - alreadyPlacedDigitCount > dashAfter) {
            formattedString.append(allDigitString.substring(alreadyPlacedDigitCount,
                    alreadyPlacedDigitCount + dashAfter)).append(HYPHEN);
            alreadyPlacedDigitCount += dashAfter;
        }
        if (totalDigitCount > alreadyPlacedDigitCount) {
            formattedString.append(allDigitString
                    .substring(alreadyPlacedDigitCount));
        }
        text.clear();
        text.append(formattedString.toString());
        return formattedString.toString();
    }

    private boolean checkAllDigit(String allDigitString) {
        return allDigitString.equals(PARENTHESES_LEFT) && clearFlag;
    }

    private boolean checkTotalDigitCountNotCredential(int totalDigitCount, String allDigitString) {
        return !credential && checkTotalDigitCount(totalDigitCount, allDigitString);
    }

    private int getDash(boolean isLonger, int maxLen) {
        return isLonger && maxLen == 10 ? NUMBER_FOUR : NUMBER_FIVE;
    }

    private boolean checkTotalDigitCount(int totalDigitCount, String allDigitString) {
        return totalDigitCount == EMPTY_INT
                || (totalDigitCount > maxLength && !allDigitString.startsWith(PARENTHESES_LEFT))
                || totalDigitCount > maxLength + SINGLE_INT;
    }
}

