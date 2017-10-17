package com.lkl.ansuote.module.postwidget.base.util;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.lkl.ansuote.module.postwidget.base.entity.ImageDirEntity;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huangdongqiang on 27/09/2017.
 */
public class MediaUtil {
    private static final String TAG = "MediaUtil";
    /**
     * 获取主存媒体资源中，所有图片的集合
     * （耗时操作，后台线程调用）
     *  (需要 STORAGE 权限组权限)
     * @param context
     * @return
     */
    public static List<ImageEntity> getMediaImageList(Context context) {
        if (null == context) {
            return null;
        }

        ArrayList<ImageEntity> list = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        if (null != contentResolver) {
            Cursor cursor = contentResolver.query(uri,
                    null,
                    MediaStore.Images.Media.MIME_TYPE
                            + "=?or "
                            + MediaStore.Images.Media.MIME_TYPE
                            + "=?",
                    new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
            if (null != cursor) {
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (!TextUtils.isEmpty(path)) {
                        ImageEntity entity = new ImageEntity();
                        if (null != entity) {
                            entity.setDir(new File(path).getParent());
                            entity.setPath(path);
                            list.add(entity);
                        }
                    }
                }
                cursor.close();
            }
        }
        Log.i(TAG, "getMediaImageList: " + "list.size = " + list.size());
        return list;
    }

    /**
     * 获取目录分类好的图片路径
     * （耗时操作，后台线程调用）
     *  (需要 STORAGE 权限组权限)
     * @param imageList
     * @return
     */
    public static List<ImageDirEntity> getMediaImageListSortByDir(List<ImageEntity> imageList) {
        List<ImageDirEntity> dirList = new ArrayList<>();
        //防止反复加入资源
        HashMap<String, ImageDirEntity> map = new HashMap<>();
        for (ImageEntity imageEntity : imageList) {
            String dir = imageEntity.getDir();
            ImageDirEntity dirEntity = null;
            if (!map.containsKey(dir)) {
                //如果当前没有包含这个目录
                dirEntity = new ImageDirEntity();
                if (null != dirEntity) {
                    dirEntity.setDir(dir);
                    dirList.add(dirEntity);
                    //保存到map缓存
                    map.put(dir, dirEntity);
                }
            } else {
                //之前缓存的，就自己取出来
                dirEntity = map.get(dir);
            }

            dirEntity.addPathUnique(imageEntity);
        }
        return dirList;
    }

    /**
     * 获取目录分类好的图片路径
     * （耗时操作，后台线程调用）
     *  (需要 STORAGE 权限组权限)
     * @param context
     * @return
     */
    public static List<ImageDirEntity> getMediaImageListSortByDir(Context context) {
        List<ImageEntity> imageList = getMediaImageList(context);
        return getMediaImageListSortByDir(imageList);
    }
}
