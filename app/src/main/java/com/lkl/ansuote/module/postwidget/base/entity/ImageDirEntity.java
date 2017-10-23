package com.lkl.ansuote.module.postwidget.base.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangdongqiang on 27/09/2017.
 */
public class ImageDirEntity implements Serializable{
    private String mDir;    //当前的目录 /storage/emulated/0/Pictures
    private List<com.lkl.ansuote.module.postwidget.base.entity.ImageEntity> mPathList = new ArrayList<>(); //目录下的图片资源

    public String getDir() {
        return mDir;
    }

    public void setDir(String dir) {
        mDir = dir;
    }

    public List<com.lkl.ansuote.module.postwidget.base.entity.ImageEntity> getPathList() {
        return mPathList;
    }

    /*public void setPathList(List<String> pathList) {
        mPathList = pathList;
    }*/

    /**
     * 往目录增加图片路径，唯一，不重复的
     * @param entity
     */
    /*public void addPathUnique(String path) {
        if (!mPathList.contains(path)) {
            mPathList.add(path);
        }
    }*/
    public void addPathUnique(com.lkl.ansuote.module.postwidget.base.entity.ImageEntity entity) {
        if (!mPathList.contains(entity)) {
            mPathList.add(entity);
        }
    }

    public void setPathList(List<com.lkl.ansuote.module.postwidget.base.entity.ImageEntity> list) {
        mPathList = list;
    }
}
