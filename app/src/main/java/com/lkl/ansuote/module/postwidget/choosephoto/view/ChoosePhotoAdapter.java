package com.lkl.ansuote.module.postwidget.choosephoto.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.lkl.ansuote.module.postwidget.R;
import com.lkl.ansuote.module.postwidget.base.Constants;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by huangdongqiang on 27/09/2017.
 */
public class ChoosePhotoAdapter extends RecyclerView.Adapter<ChoosePhotoAdapter.ViewHolder> {
    private List<ImageEntity> mList = new ArrayList<>();
    private OnChoosePhotoClickListener mOnChoosePhotoClickListener;

    public void setData(List<ImageEntity> list) {
        if (null != list) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (mList.size() > position) {
            ImageEntity imageEntity = mList.get(position);
            if (null != imageEntity) {
                holder.checkbox.setTag(imageEntity);
                holder.imageView.setTag(imageEntity);
                //holder.checkbox.setEnabled(imageEntity.isCheckedEnable());
                ImageLoader.getInstance().displayImage(Constants.PRE_FILE + imageEntity.getPath(), holder.imageView);
                //getView的时候，必须把 checkbox 的监听去掉，不然滑动的时候，复用的 checkbox 会调 setChecked，此时也会进入回调
                holder.checkbox.setOnCheckedChangeListener(null);
                holder.checkbox.setChecked(imageEntity.isChecked());

                holder.checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mOnChoosePhotoClickListener) {
                            mOnChoosePhotoClickListener.onClickCheckBox((CheckBox)v, (ImageEntity) v.getTag(), position);
                        }
                    }
                });

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mOnChoosePhotoClickListener) {
                            mOnChoosePhotoClickListener.onClickImage((ImageEntity) v.getTag(), position);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnChoosePhotoClickListener(OnChoosePhotoClickListener listener) {
        mOnChoosePhotoClickListener = listener;
    }

    public interface OnChoosePhotoClickListener {
        //void onCheckedChanged(ImageEntity entity, boolean isChecked);
        void onClickImage(ImageEntity entity, int position);
        void onClickCheckBox(CheckBox checkBox, ImageEntity entity, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        ImageView imageView;
        @Bind(R.id.checkbox)
        CheckBox checkbox;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void onDestory() {
        mList = null;
    }


}
