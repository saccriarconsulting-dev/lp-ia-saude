package com.axys.redeflexmobile.shared.services.bluetooth;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao.viana on 13/07/2017.
 */

public class PrintPicture {
    private static String hexStr = "0123456789ABCDEF";
    private static String[] binaryArray = {"0000", "0001", "0010", "0011",
            "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
            "1100", "1101", "1110", "1111"};

    public static byte[] POS_PrintBMP(Bitmap mBitmap, int nWidth, int nMode) {
        int width = ((nWidth + 7) / 8) * 8;
        int height = mBitmap.getHeight() * width / mBitmap.getWidth();
        height = ((height + 7) / 8) * 8;

        Bitmap rszBitmap = mBitmap;
        if (mBitmap.getWidth() != width) {
            rszBitmap = UtilPrint.resizeImage(mBitmap, width, height);
        }

        Bitmap grayBitmap = UtilPrint.toGrayscale(rszBitmap);
        byte[] dithered = UtilPrint.thresholdToBWPic(grayBitmap);
        byte[] data = UtilPrint.eachLinePixToCmd(dithered, width, nMode);
        return data;
    }

    public static byte[] Print_1D2A(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        byte data[] = new byte[1024 * 10];
        data[0] = 0x1D;
        data[1] = 0x2A;
        data[2] = (byte) ((width - 1) / 8 + 1);
        data[3] = (byte) ((height - 1) / 8 + 1);
        byte k = 0;
        int position = 4;
        int i;
        int j;
        byte temp = 0;

        for (i = 0; i < width; i++) {
            for (j = 0; j < height; j++) {
                if (bmp.getPixel(i, j) != -1) {
                    temp |= (0x80 >> k);
                } // end if
                k++;
                if (k == 8) {
                    data[position++] = temp;
                    temp = 0;
                    k = 0;
                } // end if k
            }// end for j
            if (k % 8 != 0) {
                data[position++] = temp;
                temp = 0;
                k = 0;
            }

        }
        if (width % 8 != 0) {
            i = height / 8;
            if (height % 8 != 0) i++;
            j = 8 - (width % 8);
            for (k = 0; k < i * j; k++) {
                data[position++] = 0;
            }
        }
        return data;
    }

    public static byte[] decodeBitmap(Bitmap bmp) {
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        List<String> list = new ArrayList<String>(); //binaryString list
        StringBuffer sb;

        // 每行字节数(除以8，不足补0)
        int bitLen = bmpWidth / 8;
        int zeroCount = bmpWidth % 8;
        // 每行需要补充的0
        String zeroStr = "";
        if (zeroCount > 0) {
            bitLen = bmpWidth / 8 + 1;
            for (int i = 0; i < (8 - zeroCount); i++) {
                zeroStr = zeroStr + "0";
            }
        }
        // 逐个读取像素颜色，将非白色改为黑色
        for (int i = 0; i < bmpHeight; i++) {
            sb = new StringBuffer();
            for (int j = 0; j < bmpWidth; j++) {
                int color = bmp.getPixel(j, i); // 获得Bitmap 图片中每一个点的color颜色值
                //颜色值的R G B
                int r = (color >> 16) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;

                // if color close to white，bit='0', else bit='1'
                if (r > 160 && g > 160 && b > 160)
                    sb.append("0");
                else
                    sb.append("1");
            }
            // 每一行结束时，补充剩余的0
            if (zeroCount > 0) {
                sb.append(zeroStr);
            }
            list.add(sb.toString());
        }
        // binaryStr每8位调用一次转换方法，再拼合
        List<String> bmpHexList = binaryListToHexStringList(list);
        String commandHexString = "1D763000";
        // 宽度指令
        String widthHexString = Integer
                .toHexString(bmpWidth % 8 == 0 ? bmpWidth / 8
                        : (bmpWidth / 8 + 1));
        if (widthHexString.length() > 2) {
            return null;
        } else if (widthHexString.length() == 1) {
            widthHexString = "0" + widthHexString;
        }
        widthHexString = widthHexString + "00";

        // 高度指令
        String heightHexString = Integer.toHexString(bmpHeight);
        if (heightHexString.length() > 2) {
            return null;
        } else if (heightHexString.length() == 1) {
            heightHexString = "0" + heightHexString;
        }
        heightHexString = heightHexString + "00";

        List<String> commandList = new ArrayList<String>();
        commandList.add(commandHexString + widthHexString + heightHexString);
        commandList.addAll(bmpHexList);

        return hexList2Byte(commandList);
    }

    public static List<String> binaryListToHexStringList(List<String> list) {
        List<String> hexList = new ArrayList<String>();
        for (String binaryStr : list) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < binaryStr.length(); i += 8) {
                String str = binaryStr.substring(i, i + 8);
                // 转成16进制
                String hexString = myBinaryStrToHexString(str);
                sb.append(hexString);
            }
            hexList.add(sb.toString());
        }
        return hexList;

    }

    public static byte[] hexList2Byte(List<String> list) {

        List<byte[]> commandList = new ArrayList<byte[]>();

        for (String hexStr : list) {
            commandList.add(hexStringToBytes(hexStr));
        }
        byte[] bytes = sysCopy(commandList);
        return bytes;
    }

    public static String myBinaryStrToHexString(String binaryStr) {
        String hex = "";
        String f4 = binaryStr.substring(0, 4);
        String b4 = binaryStr.substring(4, 8);
        for (int i = 0; i < binaryArray.length; i++) {
            if (f4.equals(binaryArray[i]))
                hex += hexStr.substring(i, i + 1);
        }
        for (int i = 0; i < binaryArray.length; i++) {
            if (b4.equals(binaryArray[i]))
                hex += hexStr.substring(i, i + 1);
        }

        return hex;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte[] sysCopy(List<byte[]> srcArrays) {
        int len = 0;
        for (byte[] srcArray : srcArrays) {
            len += srcArray.length;
        }
        byte[] destArray = new byte[len];
        int destLen = 0;
        for (byte[] srcArray : srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
            destLen += srcArray.length;
        }
        return destArray;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}