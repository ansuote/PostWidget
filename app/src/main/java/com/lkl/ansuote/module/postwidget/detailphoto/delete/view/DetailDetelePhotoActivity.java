package com.lkl.ansuote.module.postwidget.detailphoto.delete.view;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lkl.ansuote.hdqlibrary.mvp.BaseMVPActivity;
import com.lkl.ansuote.module.postwidget.R;
import com.lkl.ansuote.module.postwidget.addPhoto.view.AddPhotoActivity;
import com.lkl.ansuote.module.postwidget.base.Constants;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;
import com.lkl.ansuote.module.postwidget.base.widget.HackyViewPager;
import com.lkl.ansuote.module.postwidget.detailphoto.base.BasePagerAdapter;
import com.lkl.ansuote.module.postwidget.detailphoto.delete.DetaiDetailPhotoPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailDetelePhotoActivity extends BaseMVPActivity<IDetailDeletePhotoView, DetaiDetailPhotoPresenter> implements IDetailDeletePhotoView {
    @Bind(R.id.hack_view_pager)
    HackyViewPager mHackViewPager;
    @Bind(R.id.text_count)
    TextView mTextCount;
    private BasePagerAdapter mDetailPhotoAdapter;
    private DetailPhotoDialog mDetailPhotoDialog;

    @Override
    protected DetaiDetailPhotoPresenter createPresenter() {
        return new DetaiDetailPhotoPresenter();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_detail_detele_photo);
        ButterKnife.bind(this);
    }


    @Override
    public void initDetailPhotoAdapter(List<ImageEntity> list, int currentItem) {
        if (null != mHackViewPager) {
            mDetailPhotoAdapter = new BasePagerAdapter(list);
            if (null != mDetailPhotoAdapter) {
                mDetailPhotoAdapter.setOnClickPhotoViewListener(new BasePagerAdapter.OnClickPhotoViewListener() {
                    @Override
                    public void onClickPhotoViewListener(View v, int position, ImageEntity entity) {
                        if (null != mPresenter) {
                            mPresenter.onClickPhotoViewListener(v, position, entity);
                        }
                    }
                });
                mHackViewPager.setAdapter(mDetailPhotoAdapter);
            }
            mHackViewPager.addOnPageChangeListener(mOnPageChangeListener);

            mHackViewPager.setCurrentItem(currentItem);
        }
    }

    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (null != mPresenter) {
                mPresenter.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void setTextCount(int position, int maxChooseCount) {
        if (null != mTextCount) {
            String text = getString(R.string.choose_photo_count, position, maxChooseCount);
            mTextCount.setText(text);
        }
    }

    @Override
    public void finishCurrentActivity() {
        this.finish();
    }

    @Override
    public void startAddPhotoActivity(List<ImageEntity> selectedList) {
        AddPhotoActivity.actionStart(this, selectedList, Constants.REQUEST_CODE_DELETE_PHOTO);
    }

    public static void actionStart(Context context, List<ImageEntity> allList, int position) {
        if (null != context) {
            Intent intent = new Intent(context, DetailDetelePhotoActivity.class);
            if (null != intent) {
                intent.putParcelableArrayListExtra(Constants.EXTRA_PHOTO_LIST, (ArrayList<? extends Parcelable>) allList);
                intent.putExtra(Constants.EXTRA_PHOTO_CLICK_POSITION, position);
                context.startActivity(intent);
            }
        }
    }

    @OnClick(R.id.btn_delete)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                showDeletePhotoDialog();
                break;
        }
    }

    @Override
    public void showDeletePhotoDialog() {
        if (null == mDetailPhotoDialog) {
            mDetailPhotoDialog = new DetailPhotoDialog(this);
            mDetailPhotoDialog.setOnDetialDetelePhotoListener(new DetailPhotoDialog.OnDetialDetelePhotoListener() {
                @Override
                public void onClose() {

                }

                @Override
                public void onDelete() {
                    if (null != mPresenter) {
                        mPresenter.deletePhoto();
                    }
                }
            });
        }

        if (null != mDetailPhotoDialog) {
            mDetailPhotoDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (null != mPresenter) {
            mPresenter.onBackPressed();
        }
    }
}
