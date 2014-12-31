package com.islaidunas.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;

public class ScrollObservableListView extends ListView {

    public static int SCROLL_UP = 1;
    public static int SCROLL_DOWN = -1;
    public static int NO_SCROLL = 0;

    private int mCurrentScrollState = OnScrollListener.SCROLL_STATE_IDLE;

    private final ForwardingOnScrollListener mForwardingOnScrollListener = new ForwardingOnScrollListener();
    private int mFirstVisiblePosition;
    private float mFirstVisibleViewTop;
    private int mLastVisiblePosition;

    private float mLastVisibleViewTop;
    private int mPrevFirstPos;

    private float mPrevFirstTop;
    private PublishSubject mScrollSubject;

    private PublishSubject mScrollStateSubject;



    public ScrollObservableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.setOnScrollListener(mForwardingOnScrollListener);
        mForwardingOnScrollListener.selfListener = mOnScrollListener;
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mForwardingOnScrollListener.clientListener = l;
    }

    private static class ForwardingOnScrollListener implements OnScrollListener {

        private OnScrollListener selfListener;
        private OnScrollListener clientListener;

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (selfListener != null) {
                selfListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
            if (clientListener != null) {
                clientListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (selfListener != null) {
                selfListener.onScrollStateChanged(view, scrollState);
            }
            if (clientListener != null) {
                clientListener.onScrollStateChanged(view, scrollState);
            }
        }
    }

    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            mCurrentScrollState = scrollState;

            if(OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState){
                mPrevFirstPos = view.getFirstVisiblePosition();
                mPrevFirstTop = getChildAt(0).getY();
            }

            if(OnScrollListener.SCROLL_STATE_IDLE == scrollState){
                emitState(getScrollDirection(view));
            }
        }

        private int getScrollDirection(AbsListView view) {
            if(view.getFirstVisiblePosition() == mPrevFirstPos){
               float dist = getChildAt(0).getY() - mPrevFirstTop;

                if(dist > 0) return SCROLL_UP;
                if(dist < 0) return SCROLL_DOWN;
            }

            int dif = view.getFirstVisiblePosition() - mPrevFirstPos;

            if(dif > 0) return SCROLL_DOWN;
            if(dif < 0) return SCROLL_UP;

            return NO_SCROLL;
    }

        @Override
        public void onScroll(AbsListView view, int firstVisiblePosition, int visibleItemCount, int totalItemCount) {
            final int lastVisiblePosition = firstVisiblePosition + visibleItemCount - 1;

            float distance = 0;

            if (isScrollUp(firstVisiblePosition, lastVisiblePosition)) {
                distance = getChildAt(mFirstVisiblePosition - firstVisiblePosition).getY() - mFirstVisibleViewTop;
            } else if (isScrollDown(firstVisiblePosition, lastVisiblePosition)) {
                distance = getChildAt(mLastVisiblePosition - firstVisiblePosition).getY() - mLastVisibleViewTop;
            } else {
                distance = getAvarageDistance(firstVisiblePosition, visibleItemCount);
            }

            if(mScrollSubject != null
                    && OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == mCurrentScrollState){
                mScrollSubject.onNext(distance);
            }

            mFirstVisiblePosition = firstVisiblePosition;
            mFirstVisibleViewTop = getChildAt(0).getY();
            mLastVisiblePosition = lastVisiblePosition;
            mLastVisibleViewTop = getChildAt(visibleItemCount - 1).getY();
        }

        private boolean isScrollDown(int firstVisiblePosition, int lastVisiblePosition) {
            return mLastVisiblePosition >= firstVisiblePosition
                    && mLastVisiblePosition <= lastVisiblePosition;
        }

        private boolean isScrollUp(int firstVisiblePosition, int lastVisiblePosition) {
            return mFirstVisiblePosition >= firstVisiblePosition
                    && mFirstVisiblePosition <= lastVisiblePosition;
        }

        private float getAvarageDistance(int firstVisiblePosition, int visibleItemCount) {
            int heightSum = 0;
            for (int i = 0; i < visibleItemCount; i++) {
                heightSum += getChildAt(i).getHeight();
            }
            return heightSum / visibleItemCount * (mFirstVisiblePosition - firstVisiblePosition);
        }
    };

    public Observable<Float> createScrollObservable() {
        mScrollSubject = PublishSubject.create();
        return mScrollSubject.asObservable();
    }

    public Observable<Integer> createScrollStateObservable(){
        mScrollStateSubject = PublishSubject.create();
        return mScrollStateSubject.asObservable();
    }

    private void emitState(int state){
        if(mScrollStateSubject == null){
            return;
        }

        mScrollStateSubject.onNext(state);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mScrollSubject.onCompleted();
        mScrollStateSubject.onCompleted();
    }
}
