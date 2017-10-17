package com.lkl.ansuote.module.postwidget.choosephoto.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.lkl.ansuote.module.postwidget.R;
import com.lkl.ansuote.module.postwidget.base.entity.ImageDirEntity;
import com.lkl.ansuote.module.postwidget.choosephoto.ChoosePhotoUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 点击选择目录相册，弹出界面
 * Created by huangdongqiang on 28/09/2017.
 */
public class ChoosePhotoPopupWindow extends PopupWindow {
    private Context mContext;
    private OnPopupWindowClickListener mOnPopupWindowClickListener;
    private RecyclerView mRecyclerView;
    private PopWindowAdapter mAdapter;

    public ChoosePhotoPopupWindow(Context context) {
        if (null == context) {
            return;
        }
        mContext = context;
        initView();
        regEvent(true);
    }

    /**
     * 刷新数据，在 showAsDropDown 之前调用
     * @param list
     * @param entity
     */
    public void setData(List<ImageDirEntity> list, ImageDirEntity entity) {
        if (null != mAdapter) {
            mAdapter.setData(list, entity);
        }
    }

    private void initView() {
        mRecyclerView = new RecyclerView(mContext);
        if (null != mRecyclerView) {
            mRecyclerView.setBackgroundColor(Color.WHITE);
            mAdapter = new PopWindowAdapter();
            if (null != mAdapter) {
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            }
        }

        setContentView(mRecyclerView);
        setWidth(ScreenUtils.getScreenWidth());
        setHeight((int)(ScreenUtils.getScreenHeight() * 0.7));

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        //setBackgroundDrawable(new ColorDrawable(Color.GRAY));
    }

    private void regEvent(boolean b) {

    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (null != mOnPopupWindowClickListener) {
            mOnPopupWindowClickListener.onCloseWindow();
        }
    }

    public void setOnPopupWindowClickListener(OnPopupWindowClickListener listener) {
        mOnPopupWindowClickListener = listener;
    }



    public interface OnPopupWindowClickListener {
        /**
         * 点击某一个目录
         * @param entity  当前目录下的图片数据集合
         * @param position 当前点击的位置
         */
        void onItemDirClick(ImageDirEntity entity, int position);

        /**
         * 关闭窗口
         */
        void onCloseWindow();
    }


    class PopWindowAdapter extends RecyclerView.Adapter<PopWindowAdapter.ViewHolder>{
        private List<ImageDirEntity> mList = new ArrayList<>();
        private ImageDirEntity mSelectEntity; //打开界面前被选中的目录
        private Context mContext;

        public void setData(List<ImageDirEntity> list, ImageDirEntity entity) {
            if (null != list) {
                mSelectEntity = entity;
                mList.clear();
                mList.addAll(list);
                notifyDataSetChanged();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_photo_popupwindow, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if (null != mList && mList.size() > position) {
                final ImageDirEntity imageDirEntity = mList.get(position);
                if (null != imageDirEntity) {
                    ImageLoader.getInstance().displayImage(ChoosePhotoUtil.getFirstImagePath(imageDirEntity), holder.mImageView);
                    holder.mDirCountText.setText(String.valueOf(mList.size()));
                    holder.mDirText.setText(TextUtils.isEmpty(imageDirEntity.getDir()) ?
                            ChoosePhotoUtil.getDefaultAllImageDirName(mContext)
                            : ChoosePhotoUtil.getDirName(imageDirEntity.getDir()));
                    //holder.mDirText.setText(ChoosePhotoUtil.getDirName(imageDirEntity.getDir(), ChoosePhotoUtil.getDefaultAllImageDirName(mContext)));
                    holder.mSelectImage.setVisibility(imageDirEntity == mSelectEntity ? View.VISIBLE : View.INVISIBLE);
                    holder.itemView.setTag(mList.get(position));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != mOnPopupWindowClickListener) {
                                //重置选中的目录
                                mSelectEntity = (ImageDirEntity) v.getTag();
                                mOnPopupWindowClickListener.onItemDirClick(mSelectEntity, position);
                            }
                            dismiss();
                        }
                    });
                }
            }
        }



        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            @Bind(R.id.image)
            ImageView mImageView;
            @Bind(R.id.text_dir)
            TextView mDirText;
            @Bind(R.id.text_dir_count)
            TextView mDirCountText;
            @Bind(R.id.image_select)
            ImageView mSelectImage;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }




    }


    public void onDestory() {
        regEvent(false);
    }


}
