package com.axys.redeflexmobile.shared.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.axys.redeflexmobile.ui.redeflex.Config;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by joao.viana on 28/07/2016.
 */
public class Util_IO {
    public static String formataValor(Double valor) {
        DecimalFormat precision = new DecimalFormat("0.00");
        return precision.format(valor);
    }

    public static Double getValor(double pValor) {
        return Double.valueOf(formataValor(pValor).replace(".", "").replace(",", "."));
    }

    public static String toTitleCase(String pTexto) {
        if (pTexto == null)
            return "";
        try {
            StringBuilder sbRetorno = new StringBuilder();
            String[] arry = pTexto.split(" ");
            for (String item : arry)
                sbRetorno.append(StringUtils.capitalize(item.toLowerCase().trim()) + " ");
            return sbRetorno.toString().trim();
        } catch (Exception ex) {
            ex.printStackTrace();
            return StringUtils.capitalize(pTexto.toLowerCase().trim());
        }
    }

    public static String formataTelefone(String pDDD, String pTelefone) {
        String retorno = "";
        if (!Util_IO.isNullOrEmpty(pDDD))
            retorno += "(" + pDDD + ") ";
        if (!Util_IO.isNullOrEmpty(pTelefone))
            retorno += pTelefone;
        return retorno;
    }

    public static boolean isNumber(String pTexto) {
        return StringUtils.isNumeric(pTexto);
    }

    public static String formataDataPtBr(Date pData) {
        try {
            Locale BRAZIL = new Locale("pt", "BR");
            SimpleDateFormat fmt = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", BRAZIL);
            return fmt.format(pData);
        } catch (Exception ex) {
            return dateToStringBr(pData);
        }
    }

    public static String getDataHojePtBr() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        try {
            Locale BRAZIL = new Locale("pt", "BR");
            SimpleDateFormat fmt = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", BRAZIL);
            return fmt.format(calendar.getTime());
        } catch (Exception ex) {
            return dateToStringBr(calendar.getTime());
        }
    }

    public static String retiraCaracterEspecial(String pTexto) {
        String letra;
        java.lang.StringBuilder resultado = new java.lang.StringBuilder();

        if (pTexto == null)
            return "";

        for (int i = 0; i < pTexto.length(); i++) {
            letra = String.valueOf(pTexto.charAt(i));
            switch (letra) {
                case " ":
                    letra = " ";
                    break;
                case "×":
                case "`":
                case "|":
                case "π":
                case "&":
                case "º":
                case ":":
                case "#":
                case "$":
                case "%":
                case "¨":
                case "*":
                case "(":
                case ")":
                case "ª":
                case "°":
                case "/":
                case "´":
                case "'":
                case "-":
                case ";":
                    letra = "";
                    break;
                case "\uD83D\uDCAF":
                    letra = "100";
                    break;
            }
            resultado.append(letra);
        }

        return resultado.toString();
    }

    public static String retiraAcento(String pTexto) {
        String letra;
        java.lang.StringBuilder resultado = new java.lang.StringBuilder();

        if (pTexto == null)
            return "";

        for (int i = 0; i < pTexto.length(); i++) {
            letra = String.valueOf(pTexto.charAt(i));
            switch (letra) {
                case "Å":
                case "Æ":
                case "Ã":
                case "Ä":
                case "Â":
                case "À":
                case "Á":
                    letra = "A";
                    break;
                case "å":
                case "æ":
                case "á":
                case "à":
                case "â":
                case "ã":
                case "ä":
                    letra = "a";
                    break;
                case "é":
                case "è":
                case "ê":
                case "ë":
                    letra = "e";
                    break;
                case "í":
                case "ì":
                case "ï":
                case "î":
                    letra = "i";
                    break;
                case "ó":
                case "ò":
                case "õ":
                case "ö":
                case "ô":
                    letra = "o";
                    break;
                case "ú":
                case "ù":
                case "ü":
                case "û":
                    letra = "u";
                    break;
                case "ñ":
                    letra = "n";
                    break;
                case "ç":
                    letra = "c";
                    break;
                case "É":
                case "È":
                case "Ê":
                case "Ë":
                    letra = "E";
                    break;
                case "Í":
                case "Ì":
                case "Î":
                case "Ï":
                    letra = "I";
                    break;
                case "Ó":
                case "Ò":
                case "Ô":
                case "Ö":
                case "Õ":
                    letra = "O";
                    break;
                case "Ú":
                case "Ù":
                case "Û":
                case "Ü":
                    letra = "U";
                    break;
                case "Ñ":
                    letra = "N";
                    break;
                case "Ç":
                    letra = "C";
                    break;
            }
            resultado.append(letra);
        }
        return resultado.toString();
    }

    public static int integerTryParse(String pValor) {
        try {
            return Integer.parseInt(pValor);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static Date stringToDate(String pDate) {
        try {
            if (pDate == null)
                return null;
            if (pDate.contains("-")) {
                return new SimpleDateFormat(Config.FormatDateStringBanco, Locale.getDefault()).parse(pDate);
            } else {
                Date initDate = new SimpleDateFormat(Config.FormatDateStringBr, Locale.getDefault()).parse(pDate);
                SimpleDateFormat formatter = new SimpleDateFormat(Config.FormatDateStringBanco, Locale.getDefault());
                assert initDate != null;
                String parsedDate = formatter.format(initDate);
                ParsePosition pos = new ParsePosition(0);
                return formatter.parse(parsedDate, pos);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Date stringToDate(String pDate, String pFormat) {
        if (pDate == null)
            return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(pFormat, Locale.getDefault());
        return simpledateformat.parse(pDate, pos);
    }

    public static Date stringToDateBr(String pDate) {
        if (pDate == null)
            return null;
        SimpleDateFormat simpledateformat = new SimpleDateFormat(Config.FormatDateStringBr, Locale.getDefault());
        try {
            return simpledateformat.parse(pDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date stringToDateSql(String pDate) {
        if (pDate == null)
            return null;
        SimpleDateFormat simpledateformat = new SimpleDateFormat(Config.FormatDateTimeStringBanco, Locale.getDefault());
        try {
            return simpledateformat.parse(pDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String timeToString(Date pData) {
        return dateTimeToString(pData, Config.FormatHoraMinutoString);
    }

    public static String dateTimeToString(Date pDate, String pFormato) {
        try {
            if (pDate == null)
                return "";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pFormato, Locale.getDefault());
            return dateFormat.format(pDate);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String stringDateTimeToString(@NotNull String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(Config.FormatDateStringBr, Locale.getDefault());
            return dateFormat.format(Objects.requireNonNull(stringToDate(date)));
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String dateToStringBr(Date pData) {
        return dateTimeToString(pData, Config.FormatDateStringBr);
    }

    public static String dateTimeToStringBr(Date pDate) {
        return dateTimeToString(pDate, Config.FormatDateTimeStringBr);
    }

    public static String dateTimeToString(Date pDate) {
        return dateTimeToString(pDate, "dd-MM-yyyy HH:mm");
    }

    public static int booleanToNumber(boolean pValor) {
        return (pValor) ? 1 : 0;
    }

    public static boolean stringToBoolean(String pValor) {
        return numberToBoolean(Integer.parseInt(pValor));
    }

    public static boolean numberToBoolean(int pValor) {
        return (pValor == 1);
    }

    public static String trataStringV2(String pTexto) {
        return Normalizer.normalize(pTexto, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    public static String trataString(String pValor) {
        try {
            if (pValor == null) {
                return "";
            }
            pValor = retiraAcento(pValor);
            pValor = retiraCaracterEspecial(pValor);
            pValor = trataStringV2(pValor);
            pValor = pValor.replaceAll("[\n]{2,}", " ");
            return pValor;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static boolean isNullOrEmpty(String pValor) {
        if (pValor == null)
            return true;
        else return pValor.trim().isEmpty();
    }

    public static String padLeft(String pTexto, int pQuantidade, String pParametro) {
        try {
            return StringUtils.leftPad(pTexto, pQuantidade, pParametro);
        } catch (Exception ex) {
            ex.printStackTrace();
            return pTexto;
        }
    }

    public static Date getDateByInput(String pData) throws Exception {
        return new SimpleDateFormat(Config.FormatDateStringBr, Locale.getDefault()).parse(pData);
    }

    public static String getStringByDateFormatBanco(Date pDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(Config.FormatDateStringBanco, Locale.getDefault());
        return formatter.format(pDate);
    }

    public static Double converterValorParaDoisDecimaisDouble(Double value) {
        Locale fmtLocale = Locale.getDefault();
        NumberFormat formatter = NumberFormat.getInstance(fmtLocale);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        return Double.parseDouble(formatter.format(value == null ? 0.0 : value).replace(",", "."));
    }

    public static String formatDoubleToDecimalNonDivider(Double value) {
        if (value == null) return "";
        return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(value);
    }

    public static String formatDoubleToDecimalPercent(Double value) {
        if (value == null) return "";
        return NumberFormat.getPercentInstance(Locale.getDefault()).format(value);
    }

    public static String formatToDecimal(Integer value) {
        if (value == null) return "";
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(value);
    }

    public static <E> boolean isEmptyOrNullList(List<E> list) {
        return list == null || list.isEmpty();
    }

    public static abstract class Mask {
        static String unmask(String s) {
            return s.replaceAll("[.]", "").replaceAll("[-]", "")
                    .replaceAll("[/]", "").replaceAll("[(]", "")
                    .replaceAll("[)]", "").replaceAll("[:]", "");
        }

        public static TextWatcher insert(final String mask, final EditText ediTxt) {
            return new TextWatcher() {
                boolean isUpdating;
                String old = "";

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    String str = Mask.unmask(s.toString());
                    String mascara = "";
                    if (isUpdating) {
                        old = str;
                        isUpdating = false;
                        return;
                    }
                    int i = 0;
                    for (char m : mask.toCharArray()) {
                        if (m != '#' && str.length() > old.length()) {
                            mascara += m;
                            continue;
                        }
                        try {
                            mascara += str.charAt(i);
                        } catch (Exception e) {
                            break;
                        }
                        i++;
                    }
                    isUpdating = true;
                    ediTxt.setText(mascara);
                    ediTxt.setSelection(mascara.length());
                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            };
        }
    }

    public static class StringBuilder {
        private java.lang.StringBuilder sb;

        public StringBuilder() {
            sb = new java.lang.StringBuilder();
        }

        public int length() {
            return sb.length();
        }

        public void appendLine(Object pParametro) {
            sb.append(pParametro).append("\n");
        }

        public void append(Object pParametro) {
            sb.append(pParametro);
        }

        public String toString() {
            return sb.toString();
        }

        public void clear() {
            sb = new java.lang.StringBuilder();
        }
    }
}
