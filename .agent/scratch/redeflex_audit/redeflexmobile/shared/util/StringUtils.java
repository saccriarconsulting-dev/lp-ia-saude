package com.axys.redeflexmobile.shared.util;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * Created by Vitor Otero on 13/10/17.
 */

public class StringUtils {

    public static final int EMPTY_LIST = 0;
    public static final String EMPTY_STRING = "";
    public static final String SPACE_STRING = " ";
    public static final String SEMI_COLON = ";";
    public static final int CEP_LENGTH = 8;
    public static final int CPF_LENGTH = 11;
    public static final int CNPJ_LENGTH = 14;
    public static final int PHONE_LENGTH = 10;
    private static final String REGEX_CELLPHONE_MASK = "(\\d{2})(\\d{5})(\\d{4})";
    private static final String REPLACEMENT_CELLPHONE = "($1) $2-$3";
    private static final String REGEX_PHONE_MASK = "(\\d{2})(\\d{4})(\\d{4})";
    private static final String REPLACEMENT_PHONE = "($1) $2-$3";
    private static final String REGEX_POSTAL_CODE_MASK = "(\\d{5})(\\d{3})";
    private static final String REPLACEMENT_POSTAL_CODE = "$1-$2";


    public static Boolean isEmpty(String text) {
        if (text == null) {
            return true;
        }
        String newText = text.trim();
        return newText.length() == 0;
    }

    public static Boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    public static boolean isCpfValid(String cpfParam) {
        String cpf = returnOnlyNumbers(cpfParam);
        if (cpf.length() < 11) {
            return false;
        }

        if ("11111111111".equals(cpf)
                || "22222222222".equals(cpf)
                || "33333333333".equals(cpf)
                || "44444444444".equals(cpf)
                || "55555555555".equals(cpf)
                || "66666666666".equals(cpf)
                || "77777777777".equals(cpf)
                || "88888888888".equals(cpf)
                || "99999999999".equals(cpf)
                || "00000000000".equals(cpf)) {

            return false;
        }

        int primeiroDigito;
        int segundoDigito;
        int soma = 0;
        int peso = 10;

        for (int i = 0; i < cpf.length() - 2; i++) {
            soma += Integer.parseInt(cpf.substring(i, i + 1)) * peso--;
        }

        int mod = soma % 11;
        primeiroDigito = 11 - mod;
        if (mod < 2) {
            primeiroDigito = 0;
        }

        peso = 11;
        soma = 0;
        for (int i = 0; i < cpf.length() - 1; i++) {
            soma += Integer.parseInt(cpf.substring(i, i + 1)) * peso--;
        }
        mod = soma % 11;
        segundoDigito = 11 - mod;
        if (mod < 2) {
            segundoDigito = 0;
        }

        String digitos = primeiroDigito + String.valueOf(segundoDigito);

        return cpf.endsWith(digitos);
    }

    public static boolean isCnpjValid(String cnpjParam) {
        String cnpj = returnOnlyNumbers(cnpjParam);

        if (cnpj.length() < 14) {
            return false;
        }

        if ("11111111111111".equals(cnpj)
                || "222222222222222".equals(cnpj)
                || "333333333333333".equals(cnpj)
                || "444444444444444".equals(cnpj)
                || "555555555555555".equals(cnpj)
                || "666666666666666".equals(cnpj)
                || "777777777777777".equals(cnpj)
                || "888888888888888".equals(cnpj)
                || "999999999999999".equals(cnpj)
                || "000000000000000".equals(cnpj)) {

            return false;
        }

        int primeiroDigito;
        int segundoDigito;
        int soma = 0;
        int peso = 5;

        for (int i = 0; i < cnpj.length() - 2; i++) {
            soma += Integer.parseInt(cnpj.substring(i, i + 1)) * peso--;
            if (peso < 2) {
                peso = 9;
            }
        }

        int mod = soma % 11;
        primeiroDigito = 11 - mod;
        if (mod < 2) {
            primeiroDigito = 0;
        }

        peso = 6;
        soma = 0;
        for (int i = 0; i < cnpj.length() - 1; i++) {
            soma += Integer.parseInt(cnpj.substring(i, i + 1)) * peso--;
            if (peso < 2) {
                peso = 9;
            }
        }
        mod = soma % 11;
        segundoDigito = 11 - mod;
        if (mod < 2) {
            segundoDigito = 0;
        }

        String digitos = primeiroDigito + String.valueOf(segundoDigito);

        return cnpj.endsWith(digitos);
    }

    public static boolean isCepValid(String cep) {
        if (isEmpty(cep)) return false;
        return returnOnlyNumbers(cep).length() == CEP_LENGTH;
    }

    public static boolean isEmailNotValid(String email) {
        if ((email == null) || (email.trim().length() == 0)) {
            return true;
        }

        String emailPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return !matcher.matches();
    }

    public static Boolean isCnpj(String text) {
        return returnOnlyNumbers(text).length() == CNPJ_LENGTH;
    }

    public static Boolean isPhone(String text) {
        return returnOnlyNumbers(text).length() == 10;
    }

    public static Boolean isCellphone(String text) {
        return returnOnlyNumbers(text).length() == 11;
    }

    public static String returnOnlyNumbers(String value) {
        if (isEmpty(value)) {
            return EMPTY_STRING;
        }
        return value.replaceAll("\\D+", "");
    }

    public static String returnOnlyNumbers(Double value) {
        if (value == null) {
            return EMPTY_STRING;
        }
        String text = String.valueOf(value);
        if (isEmpty(text)) {
            return EMPTY_STRING;
        }
        return text.replaceAll("\\D+", "");
    }

    public static String returnNonSpecials(String value) {
        if (isEmpty(value)) return value;
        return value.replaceAll("[^a-zA-Z0-9]", EMPTY_STRING);
    }

    public static String maskCurrencyDouble(String value) {
        if (value == null) return EMPTY_STRING;
        Locale locale = Locale.getDefault();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        String symbol = Currency.getInstance(locale).getSymbol(locale);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);

        try {
            return formatter.format(Double.parseDouble(value))
                    .replace(symbol, String.format("%s", symbol));
        } catch (NumberFormatException e) {
            Timber.e(e);
            return EMPTY_STRING;
        }
    }

    public static String maskCurrencyDouble(Double value) {
        if (value == null) return EMPTY_STRING;
        return maskCurrencyDouble(value.toString());
    }

    public static String maskCpfCnpj(String value) {
        String item = returnOnlyNumbers(value);
        if (isEmpty(item)) {
            return value;
        }

        if (item.length() == CPF_LENGTH) {
            return String.format("%s.%s.%s-%s",
                    item.substring(0, 3),
                    item.substring(3, 6),
                    item.substring(6, 9),
                    item.substring(9, 11));
        } else if (item.length() == CNPJ_LENGTH) {
            return String.format("%s.%s.%s/%s-%s",
                    item.substring(0, 2),
                    item.substring(2, 5),
                    item.substring(5, 8),
                    item.substring(8, 12),
                    item.substring(12, 14));
        }

        return value;
    }

    public static String maskPostalCode(String postalCodeNumber) {
        return postalCodeNumber.replaceAll(REGEX_POSTAL_CODE_MASK, REPLACEMENT_POSTAL_CODE);
    }

    public static String maskCellPhone(String cellPhoneNumber) {
        return cellPhoneNumber.replaceAll(REGEX_CELLPHONE_MASK, REPLACEMENT_CELLPHONE);
    }

    public static String maskPhone(String phoneNumber) {
        return phoneNumber.replaceAll(REGEX_PHONE_MASK, REPLACEMENT_PHONE);
    }
}
