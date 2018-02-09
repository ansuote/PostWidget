package com.lkl.ansuote.module.postwidget.detailphoto.base;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.lkl.ansuote.module.postwidget.GlideApp;
import com.lkl.ansuote.module.postwidget.R;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;

import java.util.List;

/**
 * Created by huangdongqiang on 09/10/2017.
 */
public class BasePagerAdapter extends PagerAdapter{
    private List<ImageEntity> mList;
    private OnClickPhotoViewListener mOnClickPhotoViewListener;

    public BasePagerAdapter(List<ImageEntity> list) {
        mList = list;
    }

    public void setData(List<ImageEntity> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != mList) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        if (null != mList && mList.size() > position) {
            final ImageEntity imageEntity = mList.get(position);
            if (null != imageEntity) {
                PhotoView photoView = new PhotoView(container.getContext());
                photoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mOnClickPhotoViewListener) {
                            mOnClickPhotoViewListener.onClickPhotoViewListener(v, position, imageEntity);
                        }
                    }
                });
                GlideApp.with(container.getContext())
                        .load(imageEntity.getPath())
                        .placeholder(R.mipmap.ic_default)
                        .error(R.mipmap.ic_default)
                        .into(photoView);

                // Now just add PhotoView to ViewPager and return it
                container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return photoView;
            }
        }

        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setOnClickPhotoViewListener(OnClickPhotoViewListener onClickPhotoViewListener) {
        mOnClickPhotoViewListener = onClickPhotoViewListener;
    }

    public interface OnClickPhotoViewListener{
        void onClickPhotoViewListener(View v, int position, ImageEntity entity);
    }
}
