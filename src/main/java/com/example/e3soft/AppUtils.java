package com.example.e3soft;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class AppUtils {
    /**
     * copies files on the native filesystem, optional unzip
     */
    public static void AssetFileCopy(Context context, String PathDest, String assetName, boolean gunzip) {
        try {

            File fdOut = new File(PathDest);
            fdOut.createNewFile();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fdOut));
            InputStream in = null;
            if (gunzip)
                in = new GZIPInputStream(context.getAssets().open(assetName));
            else
                in = new BufferedInputStream(context.getAssets().open(assetName));
            //copy file content
            int length;
            byte buffer[] = new byte[4096];
            while ((length = in.read(buffer, 0, 4096)) != -1) {
                out.write(buffer, 0, length);
            }
            out.flush();
            out.close();
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
