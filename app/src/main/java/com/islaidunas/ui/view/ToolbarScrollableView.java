package com.islaidunas.ui.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.islaidunas.utils.ResourcesUtils;

import rx.Observable;

/**
 * Created by daggreto on 2014.12.26.
 */
public class ToolbarScrollableView extends ScrollObservableView {
    protected ViewGroup toolbarContainer;
    protected float toolBarSize;

    public ToolbarScrollableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void subscribeToolbarToListView(ScrollObservableListView listView){
        toolbarContainer = ResourcesUtils.getToolbarContainer();
        toolBarSize = toolbarContainer.getHeight();

        setObservablesFromListView(listView);

        if(scrollObservable != null){
            subscribeScroll();
        }

        if(scrollStateObservable != null){
            subscribeScrollState();
        }
    }

    private void subscribeScroll() {
        Observable<Float> actionBarPositionObservable = scrollObservable.map(y -> toolbarContainer.getTranslationY() + y);

        createLimitedTranslationObservable(-1 * toolBarSize, 0, actionBarPositionObservable)
                .subscribe(position -> toolbarContainer.setTranslationY(position));
    }

    private void subscribeScrollState(){
        Observable.merge(
                scrollStateObservable
                        .filter(state -> ScrollObservableListView.SCROLL_DOWN == state)
                        .flatMap((state) -> Observable.just(-1 * toolBarSize)),
                scrollStateObservable
                        .filter(state -> ScrollObservableListView.SCROLL_UP == state)
                        .flatMap((state) -> Observable.just(0F))
        ).subscribe( moveTo->{
            Animator hideShowToolBar = ObjectAnimator.ofFloat(toolbarContainer, "translationY", moveTo);
            hideShowToolBar.setInterpolator(new DecelerateInterpolator());
            new DecelerateInterpolator();
            hideShowToolBar.setDuration(700);
            hideShowToolBar.start();
        });
    }

    private Observable<Float> createLimitedTranslationObservable(float top, float bottom, Observable<Float> positionObservable){
        Observable<Float> limitTop = positionObservable
                .filter(posY -> posY < top)
                .flatMap(posY -> Observable.just(top));

        Observable<Float> limitBottom = positionObservable
                .filter(posY -> posY > bottom)
                .flatMap(posY -> Observable.just(bottom));

        Observable<Float> scrollablePositionObservable = positionObservable
                .filter(posY -> posY >= top)
                .filter(posY -> posY <= bottom);

        return Observable.merge(limitTop, limitBottom, scrollablePositionObservable);
    }
}
