package com.islaidunas.ui.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.islaidunas.IslaidunasApplication;
import com.islaidunas.R;
import com.islaidunas.adapter.CategoryMangerListAdapter;
import com.islaidunas.adapter.SingleStringArrayAdapter;
import com.islaidunas.core.ui.MainActivity;
import com.islaidunas.domain.Bucket;
import com.islaidunas.dto.CategorySignDto;
import com.islaidunas.screen.CategoryManagerScreen;
import com.islaidunas.ui.view.button.FloatingActionButton;
import com.islaidunas.utils.ResourcesUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mortar.Mortar;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by daggreto on 2014.07.09.
 */
public class CategoryManagerView extends FrameLayout{

    @Inject CategoryManagerScreen.Presenter presenter;

    private Subscription categorySub;

    @InjectView(R.id.buckets_spinner)
    Spinner buckets;

    @InjectView(R.id.sign_spinner)
    Spinner signs;

    @InjectView(R.id.category_list)
    ScrollObservableListView categoriesListView;

    @InjectView(R.id.filter_spinners)
    RelativeLayout filterBar;

    private FloatingActionButton button;
    private CategoryMangerListAdapter categoryAdapter;

    private Toolbar actionBar;
    private float actionBarSize;

    private Observable<Float> scrollObservable;
    private Observable<Integer> scrollStateObservable;
    private List<Subscription> subscriptions = new ArrayList<>();

    public CategoryManagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);


        scrollObservable = categoriesListView.createScrollObservable();
        scrollStateObservable = categoriesListView.createScrollStateObservable();

        initCategoryListView();
        initSignsAdapter();
        initBucketsAdapter();
        initAddButton();

        actionBar = getActionBar();
        actionBarSize =  actionBar.getHeight();

        subscribeTopBarScroll();
        subscribeScrollAnimation();

        presenter.takeView(this);
    }

    private void subscribeScrollAnimation(){
        scrollObservable
                .map(delta -> -1 * delta)
                .filter(delta -> {
                    PointF screenSize = getScreenSize();
                    PointF buttonPoint = new PointF(button.getX(), button.getY());

                    if(screenSize.y < buttonPoint.y && delta > 0){
                        return false;
                    }

                    if(screenSize.y > buttonPoint.y
                            && buttonPoint.y <  screenSize.y - 200f
                            && delta < 0){
                        return false;
                    }
                    return true;
                  }
                ).subscribe(delta ->  button.setTranslationY(button.getTranslationY() + delta));
//TODO: lagging, no animation sometimes
        scrollObservable
            .filter(state -> ScrollObservableListView.SCROLL_DOWN == state)
            .flatMap((state) -> Observable.just(-1 * actionBarSize))
            .subscribe( moveTo->{
                AnimatorSet scrollBarsTogether = new AnimatorSet();
                scrollBarsTogether.playTogether(
                        ObjectAnimator.ofFloat(filterBar, "translationY", moveTo * 2),
                        ObjectAnimator.ofFloat(actionBar, "translationY", moveTo));

                AnimatorSet scrollBarsSequentially = new AnimatorSet();
                scrollBarsSequentially.playSequentially(
                        ObjectAnimator.ofFloat(filterBar, "translationY", moveTo),
                        scrollBarsTogether
                );
                scrollBarsSequentially.setInterpolator(new DecelerateInterpolator());
                scrollBarsSequentially.setDuration(500);
                scrollBarsSequentially.start();
            });


        scrollObservable
            .filter(state -> ScrollObservableListView.SCROLL_UP == state)
            .flatMap((state) -> Observable.just(-1 * actionBarSize))
            .subscribe( moveTo->{
                AnimatorSet scrollBarsTogether = new AnimatorSet();
                scrollBarsTogether.playTogether(
                        ObjectAnimator.ofFloat(filterBar, "translationY", 0F),
                        ObjectAnimator.ofFloat(actionBar, "translationY", 0F));

                AnimatorSet scrollBarsSequentially = new AnimatorSet();
                scrollBarsSequentially.playSequentially(
                        ObjectAnimator.ofFloat(filterBar, "translationY", moveTo),
                        scrollBarsTogether
                );
                scrollBarsSequentially.setInterpolator(new DecelerateInterpolator());
                scrollBarsSequentially.setDuration(500);
                scrollBarsSequentially.start();
            });
    }

    private void subscribeTopBarScroll() {

        /*
            Filter Bar Scroll Observation
         */
        Observable<Float> filterBarPositionObservable =
                scrollObservable
                .map(y -> filterBar.getTranslationY() + y);

        createLimitedTransaltionObservable(-1 * actionBarSize * 2, 0, filterBarPositionObservable)
                .subscribe(newPosition -> filterBar.setTranslationY(newPosition));


        /*
            Action Bar Scroll Observation
         */
        Observable<Float> whenActionBarOverScreenObservable =
                scrollObservable.filter(y -> filterBar.getTranslationY() > -1 * actionBarSize
                        && actionBar.getTranslationY() < 0);

        Observable<Float> actionBarScrollObservable = scrollObservable
                .filter(y -> filterBar.getTranslationY() <= -1 * actionBarSize)
                .filter(y -> filterBar.getTranslationY() > -1 * actionBarSize * 2);

        Observable<Float> actionBarPositionObservable = Observable.merge(actionBarScrollObservable, whenActionBarOverScreenObservable)
                .map(y -> actionBar.getTranslationY() + y);

        createLimitedTransaltionObservable(-1 * actionBarSize, 0, actionBarPositionObservable)
                .subscribe(position -> actionBar.setTranslationY(position));
    }

    private Observable<Float> createLimitedTransaltionObservable(float top, float bottom, Observable<Float> positionObservable){
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

    private void initAddButton() {
        button = new FloatingActionButton.Builder((Activity) IslaidunasApplication.getActivityContext())
                .withDrawable(getResources().getDrawable(android.R.drawable.ic_menu_add))
                .withButtonColor(ResourcesUtils.getColor(R.color.colorAccent))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 20, 20)
                .create();
        button.setY(button.getY() + 20f);
        dragFAB(button, 0, -20f, new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                filterCategories();
            }
        });
    }

    private void subscribeScroll(Action1<Float> func){
        subscriptions.add(scrollObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(func));
    }

    private void initBucketsAdapter() {
        List<Bucket> bucketsToShow = new ArrayList<Bucket>();
        bucketsToShow.add(new Bucket(getContext().getString(R.string.all_buckets)));
        bucketsToShow.addAll(presenter.findAllBuckets());

        final SingleStringArrayAdapter<Bucket> bucketAdapter = new SingleStringArrayAdapter<>(this.getContext(), bucketsToShow);
        buckets.setAdapter(bucketAdapter);
        buckets.setOnItemSelectedListener(new FilterListener());
    }

    private void initCategoryListView() {
        categoriesListView.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.category_list_header, null));
        categoriesListView.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.category_list_header, null));

        categoryAdapter = new CategoryMangerListAdapter(this.getContext(), new ArrayList<>());
        categorySub = presenter.getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    categoryAdapter.setCategories(list);
                });
        categoriesListView.setAdapter(categoryAdapter);

        filterCategories();
    }

    private void initSignsAdapter() {
        List<CategorySignDto> categorySignDtos = new ArrayList<>();
        categorySignDtos.add(new CategorySignDto(getContext().getString(R.string.all_signs)));
        categorySignDtos.addAll(presenter.getSigns());

        SingleStringArrayAdapter<CategorySignDto> signsAdapter = new SingleStringArrayAdapter<CategorySignDto>(this.getContext(), categorySignDtos);
        signs.setAdapter(signsAdapter);
        signs.setOnItemSelectedListener(new FilterListener());
    }

    private void filterCategories(){
        CategoryMangerListAdapter adapter = getCategoryListAdapter();
        adapter.filterCategories(
                (Bucket) buckets.getSelectedItem(),
                ((CategorySignDto) signs.getSelectedItem()));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        categorySub.unsubscribe();
        for(Subscription sub : subscriptions){
            sub.unsubscribe();
        }
    }

    public void dragFAB(View button, float toX, float toY, AnimationEndListener listener){
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "translationX", toX);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "translationY", toY);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(scaleX);
        animSetXY.playTogether(scaleY);
        animSetXY.setInterpolator(new DecelerateInterpolator());
        animSetXY.setDuration(800);
        animSetXY.addListener(listener);
        animSetXY.start();
    }

    private class FilterListener implements AdapterView.OnItemSelectedListener {
        private int count = 0;
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(count == 0){
                count++;
                return;
            }
            filterCategories();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private abstract class AnimationEndListener implements Animator.AnimatorListener{
        @Override
        public  void onAnimationStart(Animator animator) {}
        @Override
        public void onAnimationCancel(Animator animator) {}
        @Override
        public void onAnimationRepeat(Animator animator) {}
    }

    private PointF getScreenSize(){
        WindowManager windowManager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return new PointF(new Float(point.x), new Float(point.y));
    }

    private CategoryMangerListAdapter getCategoryListAdapter(){
        HeaderViewListAdapter wrapperAdapter = (HeaderViewListAdapter) categoriesListView.getAdapter();
        return (CategoryMangerListAdapter) wrapperAdapter.getWrappedAdapter();
    }

    private Toolbar getActionBar(){
        MainActivity activity = (MainActivity) IslaidunasApplication.getActivityContext();
        return activity.getToolbar();
    }
}
