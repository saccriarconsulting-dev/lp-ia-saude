package com.axys.redeflexmobile.shared.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import com.axys.redeflexmobile.shared.models.venda.Imagem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.media.ExifInterface.ORIENTATION_NORMAL;
import static android.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.media.ExifInterface.ORIENTATION_ROTATE_90;
import static android.media.ExifInterface.TAG_ORIENTATION;

public class Util_Imagem {
    private static final int INDEX_ONE = 1;
    private static final String FILE_PATH = "path";
    private static final String FILE_MIME = "mime";
    private static final String DEVICE_SAMSUNG = "samsung";
    private static final int ORIENTATION_90 = 90;
    private static final int ORIENTATION_180 = 180;
    private static final int ORIENTATION_270 = 270;

    //Nova tela de vendas
    public static final String PHOTO_EXTENSION = ".jpg";
    public static final int TAG_PHOTO = 99;
    public static final String PROVIDER = ".provider";
    public static final int IMAGE_QUALITY = 100;
    public static final int IMAGE_SIZE = 1200;
    public static final String DEVICE_FILE = "file";

    public static Bitmap createScaledBitmap(Context context,
                                            Imagem image,
                                            int imageSize) {
        InputStream imageStream;
        Bitmap bmp = null;

        try {
            imageStream = context.getContentResolver().openInputStream(image.getUri());
            bmp = BitmapFactory.decodeStream(imageStream);
            bmp = getResizedBitmap(bmp, imageSize);

            String imagePath = getFileInfo(context, image.getUri(), FILE_PATH);
            ExifInterface exif = new ExifInterface(imagePath);
            int rotation = exif.getAttributeInt(TAG_ORIENTATION, ORIENTATION_NORMAL);
            if (rotation == 0 && Build.MANUFACTURER.equals(DEVICE_SAMSUNG)
                    && bmp.getWidth() > bmp.getHeight()) {
                rotation = ORIENTATION_ROTATE_90;
            }

            if (rotation != 0f) {
                Matrix matrix = new Matrix();
                matrix.preRotate(exifToDegrees(rotation));
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    private static Bitmap getResizedBitmap(Bitmap image, int imageSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > INDEX_ONE) {
            width = imageSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = imageSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String getFileInfo(Context context, Uri uri, String type) {
        if (FILE_MIME.equals(type)) {
            String mimeType;
            if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                ContentResolver cr = context.getContentResolver();
                mimeType = cr.getType(uri);
            } else {
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                        fileExtension.toLowerCase());
            }
            return mimeType;
        }

        Cursor cursor;
        int columnIndex;

        if (FILE_PATH.equals(type)) {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(
                    uri, projection, null, null, null);
            if (cursor == null) return "";
            columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        } else {
            cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor == null) return "";
            columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        }

        String result;
        if (columnIndex > -1) {
            cursor.moveToFirst();
            result = cursor.getString(columnIndex);
            cursor.close();
        } else {
            result = uri.getPath();
        }
        return result;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ORIENTATION_ROTATE_90) {
            return ORIENTATION_90;
        } else if (exifOrientation == ORIENTATION_ROTATE_180) {
            return ORIENTATION_180;
        } else if (exifOrientation == ORIENTATION_ROTATE_270) {
            return ORIENTATION_270;
        }
        return 0;
    }

    public static void saveImage(Bitmap image, String imagePath, int imageQuality) {
        try {
            File file = new File(imagePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, imageQuality, bos);
            byte[] thumbnailByte = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(thumbnailByte);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
