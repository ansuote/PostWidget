package com.lkl.ansuote.module.postwidget.choosephoto;

import android.content.Context;
import android.text.TextUtils;

import com.lkl.ansuote.module.postwidget.R;
import com.lkl.ansuote.module.postwidget.base.Constants;
import com.lkl.ansuote.module.postwidget.base.entity.ImageDirEntity;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;

import java.io.File;
import java.util.List;

/**
 * Created by huangdongqiang on 30/09/2017.
 */
public class ChoosePhotoUtil {
    /**
     * 目录的第一张图片的路径(拼接前缀)
     * @param imageDirEntity
     * @return
     */
    public static String getFirstImagePathWithPreFile(ImageDirEntity imageDirEntity) {
        String firstImagePath = Constants.PRE_FILE;
        List<ImageEntity> pathList = imageDirEntity.getPathList();
        if (null != pathList && pathList.size() > 0 && null != pathList.get(0)) {
            firstImagePath = firstImagePath + pathList.get(0).getPath();
        }
        return firstImagePath;
    }

    /**
     * 目录的第一张图片的路径（没有拼接前缀）
     * @param imageDirEntity
     * @return
     */
    public static String getFirstImagePath(ImageDirEntity imageDirEntity) {
        List<ImageEntity> pathList = imageDirEntity.getPathList();
        if (null != pathList && pathList.size() > 0 && null != pathList.get(0)) {
            return pathList.get(0).getPath();
        }
        return "";
    }


    /**
     * 获取目录名称
     * @param dir
     * @return
     */
    public static String getDirName(String dir) {
        return getDirName(dir, "");
    }

    /**
     * 获取目录名称
     * @param dir
     * @param defaultValue
     * @return
     */
    public static String getDirName(String dir, String defaultValue) {
        if (!TextUtils.isEmpty(dir)) {
            File file = new File(dir);
            if (file.exists()) {
                return file.getName();
            }
        }
        return defaultValue;
    }

    /**
     * 所有图片的情况下，显示的标题
     * @param context
     * @return
     */
    public static String getDefaultAllImageDirName(Context context) {
        if (null != context) {
            return context.getString(R.string.choose_photo_all_picture_dir);
        }
        return "";
    }
}
