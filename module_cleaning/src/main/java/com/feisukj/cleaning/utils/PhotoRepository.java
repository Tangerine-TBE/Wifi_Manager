package com.feisukj.cleaning.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.feisukj.cleaning.bean.ImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gavin on 2017/3/27.
 */

public class PhotoRepository {

    private static final String TAG = PhotoRepository.class.getSimpleName();

    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
    };

    public static List<ImageBean> getPhoto(Context context) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " desc";
        Cursor cursor;
        try {
            cursor=contentResolver.query(uri, STORE_IMAGES, null, null, sortOrder);
        }catch (Exception e){
            e.printStackTrace();
            return Collections.emptyList();
        }
        List<ImageBean> result = new ArrayList<>();

        while (cursor != null && cursor.moveToNext()) {
            File file=new File(cursor.getString(1));
            ImageBean photo = new ImageBean(file);
            photo.setId(cursor.getLong(0));
            if (file.exists()) {
                result.add(photo);
            }
        }

        if (cursor != null) cursor.close();

        Log.d(TAG, "getPhoto: size=" + result.size());

        return result;
    }

}
