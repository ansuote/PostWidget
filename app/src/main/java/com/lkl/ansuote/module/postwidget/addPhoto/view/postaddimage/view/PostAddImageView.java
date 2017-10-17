package com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lkl.ansuote.module.postwidget.R;
import com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.adpater.CustomItemDragAndSwipeCallback;
import com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.adpater.PostAddImageAdapter;
import com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.entity.PostAddImageEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * Post 朋友圈选择图片控件
 * Created by huangdongqiang on 12/09/2017.
 */
public class PostAddImageView extends LinearLayout implements IPostAddImageView{
    private final int DEFAULT_BAGROUND_COLOR = Color.WHITE;
//    private final int DEFAULT_BAGROUND_COLOR = R.color.white;

    private int mColumn = 4;    //列数默认为一行4个
    private int mMaxChooseCount = 9;    //最大的可选图片数，默认为 9
    private int mColumnMargin;  //列与列间的间距
    private int mRowMargin; //行与行之间的间距
    //private int mCurrentChooseCount;    //当前选择的图片数量
    private Context mContext;
    private AttributeSet mAttrs;
    private List<PostAddImageEntity> mChooseList = new ArrayList<>();//已经选择的图片数据集
    private int mMarginLeft;    //左外边距
    private int mMarginRight;   //右外边距
    private int mPaddingLeft;   //左内边距
    private int mPaddingRight;  //右内边距

    private RecyclerView mRecyclerView;
    PostAddImageAdapter mPostAddImageAdapter;
    private OnPostAddImageClickListener mOnPostAddImageClickListener;

    public PostAddImageView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public PostAddImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        init();
    }

    public PostAddImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mAttrs = attrs;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PostAddImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        mAttrs = attrs;
        init();
    }


    private void init() {
        if (null == mContext) {
            return;
        }

        initView();

        regEvent(true);
    }


    private void initView() {

        initAttrs();

        this.setOrientation(LinearLayout.VERTICAL);

        initRecyclerView();
        initData();
        initAdapter();

    }

    /**
     * 初始化数据，默认只有一个加号按钮
     */
    private void initData() {
        /*for(int i = 0; i < 9; i++) {
            PostAddImageEntity entity2 = new PostAddImageEntity(PostAddImageEntity.TYPE_IMAGE);
            entity2.setUrl("position = " + i);
            mChooseList.add(entity2);
        }*/

        PostAddImageEntity entity = new PostAddImageEntity(PostAddImageEntity.TYPE_BUTTON_ADD);
        mChooseList.add(entity);
    }

    private void initRecyclerView() {
        mRecyclerView = new RecyclerView(mContext);
        if (null != mRecyclerView) {
            this.addView(mRecyclerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, mColumn));
        }
    }

    private void initAdapter() {
        if (null == mRecyclerView) {
            return;
        }

        //拖拽适配器
        mPostAddImageAdapter = new PostAddImageAdapter(mChooseList);
        if (null != mPostAddImageAdapter) {
            //ItemDragAndSwipeCallback  CustomItemDragAndSwipeCallback
            CustomItemDragAndSwipeCallback itemDragAndSwipeCallback = new CustomItemDragAndSwipeCallback(mPostAddImageAdapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);

            // 开启拖拽
            mPostAddImageAdapter.enableDragItem(itemTouchHelper);

            mRecyclerView.setAdapter(mPostAddImageAdapter);
        }
    }

    private void initAttrs() {
        if (null != mAttrs) {
            TypedArray array = mContext.obtainStyledAttributes(mAttrs, R.styleable.PostAddImageView);
            if (null != array) {
                Drawable backgroundDrawable = array.getDrawable(R.styleable.PostAddImageView_background);
                int padding = array.getDimensionPixelSize(R.styleable.PostAddImageView_padding, 0);

                if (null == backgroundDrawable) {
                    this.setBackgroundColor(DEFAULT_BAGROUND_COLOR);
                } else {
                    this.setBackground(backgroundDrawable);
                }
                this.setPadding(padding, padding, padding, padding);

                array.recycle();
            }
        }
    }

    private void regEvent(boolean b) {
        if (null != mPostAddImageAdapter) {
            mPostAddImageAdapter.setOnItemClickListener(b ? new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (adapter.getItemViewType(position) == PostAddImageEntity.TYPE_BUTTON_ADD) {
                        if (null != mOnPostAddImageClickListener) {
                            mOnPostAddImageClickListener.onClickAddBtn();
                        }
                    } else if (adapter.getItemViewType(position) == PostAddImageEntity.TYPE_IMAGE) {
                        if (null != mOnPostAddImageClickListener) {
                            mOnPostAddImageClickListener.onClickImage(position, getEntityFromAdater(adapter, position));
                        }
                    }
                }
            } : null);
        }
    }

    private PostAddImageEntity getEntityFromAdater(BaseQuickAdapter adapter, int position) {
        if (null != adapter && adapter instanceof PostAddImageAdapter) {
            PostAddImageAdapter postAddImageAdapter = (PostAddImageAdapter) adapter;
            return postAddImageAdapter.getData().get(position);
        }

        return null;
    }

    @Override
    public void refresh(List<PostAddImageEntity> list) {
        if (null != mPostAddImageAdapter) {
            mPostAddImageAdapter.setNewData(list);
        }
    }

    @Override
    public int getCurrentChooseCount() {
        int size = mPostAddImageAdapter.getData().size();
        if (size > 0 && mPostAddImageAdapter.getItem(size - 1).getItemType() == PostAddImageEntity.TYPE_BUTTON_ADD) {
            return size - 1;
        } else {
            return size;
        }
    }


    public void setOnPostAddImageClickListener(OnPostAddImageClickListener onPostAddImageClickListener) {
        mOnPostAddImageClickListener = onPostAddImageClickListener;
    }

    @Override
    public void onDestroy() {
        regEvent(false);


    }

}
