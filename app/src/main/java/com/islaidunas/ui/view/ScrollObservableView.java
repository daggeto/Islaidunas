package com.islaidunas.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import rx.Observable;

public class ScrollObservableView extends FrameLayout {

    protected Observable<Float> scrollObservable;
    protected Observable<Integer> scrollStateObservable;

    public ScrollObservableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void setObservablesFromListView(ScrollObservableListView listView){
        scrollObservable = listView.createScrollObservable();
        scrollStateObservable = listView.createScrollStateObservable();
    }
}
