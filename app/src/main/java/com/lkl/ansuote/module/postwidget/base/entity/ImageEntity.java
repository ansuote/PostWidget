package com.lkl.ansuote.module.postwidget.base.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huangdongqiang on 27/09/2017.
 */
public class ImageEntity implements Parcelable{
    private String mPath; //图片图片所在路径
    private String mDir; //图片所在文件夹
    private boolean mChecked;  //是否被选中
    //private boolean mCheckedEnable = true; //是否使能可以选中

    public ImageEntity() {

    }

    protected ImageEntity(Parcel in) {
        mPath = in.readString();
        mDir = in.readString();
        //mChecked = in.readByte() != 0;
        //mCheckedEnable = in.readByte() != 0;
        mChecked = in.readInt() != 0;
        //mCheckedEnable = in.readInt() != 0;
    }

    public static final Creator<ImageEntity> CREATOR = new Creator<ImageEntity>() {
        @Override
        public ImageEntity createFromParcel(Parcel in) {
            return new ImageEntity(in);
        }

        @Override
        public ImageEntity[] newArray(int size) {
            return new ImageEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPath);
        dest.writeString(mDir);
        dest.writeInt(mChecked ? 1 : 0);
        //dest.writeInt(mCheckedEnable ? 1 : 0);
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getDir() {
        return mDir;
    }

    public void setDir(String dir) {
        mDir = dir;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    /*public boolean isCheckedEnable() {
        return mCheckedEnable;
    }

    public void setCheckedEnable(boolean checkedEnable) {
        mCheckedEnable = checkedEnable;
    }*/

}
