package com.lkl.ansuote.module.postwidget.detailphoto.choose.view;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lkl.ansuote.hdqlibrary.mvp.BaseMVPActivity;
import com.lkl.ansuote.module.postwidget.R;
import com.lkl.ansuote.module.postwidget.addPhoto.view.AddPhotoActivity;
import com.lkl.ansuote.module.postwidget.base.Constants;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;
import com.lkl.ansuote.module.postwidget.base.widget.HackyViewPager;
import com.lkl.ansuote.module.postwidget.detailphoto.base.BasePagerAdapter;
import com.lkl.ansuote.module.postwidget.detailphoto.choose.DetailChoosePhotoPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailChoosePhotoActivity extends BaseMVPActivity<IDetailChoosePhotoView, DetailChoosePhotoPresenter> implements IDetailChoosePhotoView {

    @Bind(R.id.hack_view_pager)
    HackyViewPager mHackViewPager;
    @Bind(R.id.text_count)
    TextView mTextCount;
    @Bind(R.id.layout_title)
    RelativeLayout mLayoutTitle;
    @Bind(R.id.layout_bottom)
    RelativeLayout mLayoutBottom;
    @Bind(R.id.btn_title_right)
    Button mDoneBtn;
    @Bind(R.id.checkbox)
    CheckBox mCheckBox;

    private BasePagerAdapter mDetailPhotoAdapter;

    @Override
    protected DetailChoosePhotoPresenter createPresenter() {
        return new DetailChoosePhotoPresenter();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_detail_choose_photo);
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
    public void setTextCount(int position, int count) {
        if (null != mTextCount) {
            String text = getString(R.string.choose_photo_count, position, count);
            mTextCount.setText(text);
        }
    }


    @Override
    public void setChooseCount(int currentChooseCount, int maxChooseCount) {
        if (null != mDoneBtn) {
            mDoneBtn.setText(getString(R.string.btn_choose_photo_done, currentChooseCount, maxChooseCount));
        }
    }

    @Override
    public void showTopLayout() {
        if (null != mLayoutTitle) {
            mLayoutTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideTopLayout() {
        if (null != mLayoutTitle) {
            mLayoutTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void showBottomLayout() {
        if (null != mLayoutBottom) {
            mLayoutBottom.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideBottomLayout() {
        if (null != mLayoutBottom) {
            mLayoutBottom.setVisibility(View.GONE);
        }
    }


    @OnClick({
            R.id.btn_title_left,
            R.id.btn_title_right,
            R.id.checkbox,
            R.id.hack_view_pager,
            R.id.layout_title,
            R.id.layout_bottom,
            R.id.btn_preview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_title_left:
                finish();
                break;
            case R.id.btn_preview:
                if (null != mPresenter) {
                    mPresenter.reverseShowMode();
                }
                break;
            case R.id.btn_title_right:
                if (null != mPresenter) {
                    mPresenter.clickDoneBtn();
                }
                break;
            case R.id.checkbox:
                if (null != mPresenter) {
                    mPresenter.clickCheckBox();
                }
                break;
            case R.id.layout_title:
                break;
            case R.id.layout_bottom:
                break;
        }
    }

    public static void actionStart(Context context,
                                   List<ImageEntity> allList,
                                   int position,
                                   int currentChooseCount,
                                   int maxChooseCount,
                                   List<ImageEntity> selectedList) {
        if (null != context) {
            Intent intent = new Intent(context, DetailChoosePhotoActivity.class);
            if (null != intent) {
                intent.putParcelableArrayListExtra(Constants.EXTRA_PHOTO_LIST, (ArrayList<? extends Parcelable>) allList);
                intent.putExtra(Constants.EXTRA_PHOTO_CLICK_POSITION, position);
                intent.putExtra(Constants.EXTRA_PHOTO_CURRENT_CHOOSE_COUNT, currentChooseCount);
                intent.putExtra(Constants.EXTRA_PHOTO_MAX_CHOOSE_COUNT, maxChooseCount);
                intent.putParcelableArrayListExtra(Constants.EXTRA_PHOTO_SELECT_LIST, (ArrayList<? extends Parcelable>) selectedList);
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void setCheckBox(boolean b) {
        if (null != mCheckBox) {
            mCheckBox.setChecked(b);
        }
    }

    @Override
    public void showOverMaxChooseCountThoast() {
        Toast.makeText(this, getString(R.string.over_max_choose_count), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startAddPhotoActivity(List<ImageEntity> selectedList) {
        AddPhotoActivity.actionStart(this, selectedList, Constants.REQUEST_CODE_ALBUM);
    }

    @Override
    protected void onDestroy() {
        if (null != mHackViewPager) {
            mHackViewPager.removeOnPageChangeListener(mOnPageChangeListener);
        }

        super.onDestroy();
    }

    @Override
    public void finishCurrentActivity() {
        finish();
    }
}
