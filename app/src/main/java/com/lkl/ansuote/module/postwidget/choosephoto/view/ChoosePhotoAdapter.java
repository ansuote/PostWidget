package com.lkl.ansuote.module.postwidget.choosephoto.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.lkl.ansuote.module.postwidget.GlideApp;
import com.lkl.ansuote.module.postwidget.R;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;

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
    private Context mContext;

    public void setData(List<ImageEntity> list) {
        if (null != list) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (mList.size() > position) {
            final ImageEntity imageEntity = mList.get(position);
            if (null != imageEntity) {
                holder.checkbox.setTag(imageEntity);
                GlideApp.with(mContext)
                        .load(imageEntity.getPath())
                        .placeholder(R.mipmap.ic_default)
                        .error(R.mipmap.ic_default)
                        .into(holder.imageView);

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
                            mOnChoosePhotoClickListener.onClickImage(imageEntity, position);
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
