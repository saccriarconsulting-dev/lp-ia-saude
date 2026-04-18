package com.axys.redeflexmobile.shared.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompressFile {

    private String source;
    private String destinyFolder;
    private String zipName;

    public CompressFile(String source, String destinyFolder, String zipName) {
        this.source = source;
        this.destinyFolder = destinyFolder;
        this.zipName = zipName;

        createDestinyFolder();
    }

    public boolean compress() {
        try {
            String destinyFile = destinyFolder + File.separator + zipName;
            FileOutputStream fos = new FileOutputStream(destinyFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File srcFile = new File(source);
            File[] files = srcFile.listFiles();
            if (files == null) {
                return false;
            }

            for (File file : files) {
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(file);
                zos.putNextEntry(new ZipEntry(file.getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        } catch (IOException ioe) {
            return false;
        }

        return true;
    }

    public File getCompressedFile() {
        File file = new File(destinyFolder + File.separator + zipName);
        if (file.exists()) {
            return file;
        }

        throw new IllegalStateException("The compressed file do not exists");
    }

    private void createDestinyFolder() {
        File file = new File(destinyFolder);
        if (file.exists()) {
            return;
        }

        boolean isCreated = file.mkdir();
        if (!isCreated) {
            throw new IllegalStateException("The folder cannot be created");
        }
    }
}
