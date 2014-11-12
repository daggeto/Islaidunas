package com.islaidunas.listener;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

/**
 * Created by daggreto on 2014.10.04.
 */
public class SwipeListViewListenerImpl extends BaseSwipeListViewListener {

    private SwipeListView listView;

    public SwipeListViewListenerImpl(SwipeListView listView) {
        this.listView = listView;
    }

    int currentOpenedItem = -1;
    int openedItem = -1;
    int lastClosedItem = -1;

    @Override
    public void onOpened(int position, boolean toRight) {
        openedItem = position;
        if (currentOpenedItem > -1 && openedItem != lastClosedItem) {
            listView.closeAnimate(currentOpenedItem);
        }
        lastClosedItem = -1;
        currentOpenedItem = position;
    }

    @Override
    public void onStartClose(int position, boolean right) {
        lastClosedItem = position;
    }

}
