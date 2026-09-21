package com.example.tripbuddy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    public static String saveBitmapToInternal(Context context, Bitmap bitmap, String filename) throws IOException {

        File dir = context.getFilesDir();
        File file = new File(dir, filename);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
        } finally {
            if (fos != null) fos.close();
        }

        return file.getAbsolutePath();
    }
    public static Bitmap loadBitmapFromPath(String path) {

        return BitmapFactory.decodeFile(path);

    }
    public static Uri getUriFromPath(String path) {

        return Uri.fromFile(new File(path));

    }
}

