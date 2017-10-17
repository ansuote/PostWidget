package com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.adpater;


import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.entity.PostAddImageEntity;


/**
 * 自定义拖拽回调
 * 用于处理，禁止某个拖拽 ／ 禁止某个位置不能被排序（被拖拽进去）
 * Created by huangdongqiang on 13/09/2017.
 */
public class CustomItemDragAndSwipeCallback extends ItemDragAndSwipeCallback {
    public CustomItemDragAndSwipeCallback(BaseItemDraggableAdapter adapter) {
        super(adapter);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //禁止某个拖拽
        if (viewHolder.getItemViewType() == PostAddImageEntity.TYPE_BUTTON_ADD) {
            return makeMovementFlags(0, 0);
        }

        return super.getMovementFlags(recyclerView, viewHolder);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        //禁止某个位置不能被排序（被拖拽进去）
        if (source.getItemViewType() == PostAddImageEntity.TYPE_BUTTON_ADD) {
            return false;
        }

        return super.onMove(recyclerView, source, target);
    }
}
