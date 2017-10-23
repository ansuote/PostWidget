package com.lkl.ansuote.module.postwidget.base;

/**
 * Created by huangdongqiang on 27/09/2017.
 */
public class Constants {
    public static String PRE_FILE = "file://";
    //public static String EXTRA_PHOTO_LIST = "extra_photo_list";
    public static String EXTRA_PHOTO_CLICK_POSITION = "extra_photo_click_position";
    public static String EXTRA_PHOTO_MAX_CHOOSE_COUNT = "extra_photo_max_choose_count";
    public static String EXTRA_PHOTO_CURRENT_CHOOSE_COUNT = "extra_photo_current_choose_count";
    //public static String EXTRA_PHOTO_SELECT_LIST = "extra_photo_select_list";
    public static final String EXTRA_IMAGE_LIST = "imglist";    //选择相册返回的图片列表

    public static final String EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE";
    public static final int REQUEST_CODE_ALBUM = 0;
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_DELETE_PHOTO = 3;
    public static final int REQUEST_CODE_SETTING = 10;
    public static final int REQUEST_CODE_CROP = 11;
    public static final int REQUEST_CODE_CHOOSE_PHOTO = 12;


    public interface Permission {
        int REQUEST_CODE_CAMERA = 1;
    }

    public interface PostWidget{
        String FILE_NAME_PHOTO_SELECT_LIST = "file_name_photo_select_list";
        String FILE_NAME_PHOTO_ALL_LIST = "file_name_photo_all_list";

    }

    public static final String EXTRA_FINISH_ACTIVITY = "extra_finish_activity";


}
