package com.islaidunas.ui.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.Spinner;

import com.islaidunas.IslaidunasApplication;
import com.islaidunas.R;
import com.islaidunas.adapter.CategoryMangerListAdapter;
import com.islaidunas.adapter.SingleStringArrayAdapter;
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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by daggreto on 2014.07.09.
 */
public class CategoryManagerView extends ToolbarScrollableView {

    @Inject CategoryManagerScreen.Presenter presenter;

    private Subscription categorySub;

    @InjectView(R.id.category_list)
    ScrollObservableListView categoriesListView;

    private View filterSpinners;
    private Spinner buckets;
    private Spinner signs;

    private FloatingActionButton button;
    private CategoryMangerListAdapter categoryAdapter;

    public CategoryManagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        subscribeToolbarToListView(categoriesListView);

        ViewGroup toolbarContent = ResourcesUtils.getToolBarContent();
        filterSpinners = inflate(getContext(), R.layout.category_filter_spinner, toolbarContent);

        initSignsAdapter();
        initBucketsAdapter();
        initCategoryListView();
        initAddButton();

        subscribeScrollAnimation();

        presenter.takeView(this);
    }

    private void subscribeScrollAnimation(){
//        Observable<Float> buttonPosition = scrollObservable
//                .map(y -> button.getTranslationY() -1 * y);
//
//        float bottomLimit = getScreenSize().y - button.getY();
//        createLimitedTranslationObservable(0 , bottomLimit, buttonPosition)
//                .subscribe(delta ->  button.setTranslationY(delta));
    }

    private void initAddButton() {
        button = new FloatingActionButton.Builder((Activity) IslaidunasApplication.getActivityContext())
                .withDrawable(getResources().getDrawable(android.R.drawable.ic_menu_add))
                .withButtonColor(ResourcesUtils.getColor(R.color.colorAccent))
                .withGravity(Gravity.TOP | Gravity.RIGHT)
                .create();
        button.setY(button.getY() + 20f);
        dragFAB(button, 0, -20f, new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                filterCategories();
            }
        });
    }

    private void initBucketsAdapter() {
        buckets = ButterKnife.findById(filterSpinners, R.id.buckets_spinner);

        List<Bucket> bucketsToShow = new ArrayList<Bucket>();
        bucketsToShow.add(new Bucket(getContext().getString(R.string.all_buckets)));
        bucketsToShow.addAll(presenter.findAllBuckets());

        final SingleStringArrayAdapter<Bucket> bucketAdapter = new SingleStringArrayAdapter<>(this.getContext(), bucketsToShow);
        buckets.setAdapter(bucketAdapter);
        buckets.setOnItemSelectedListener(new FilterListener());
    }

    private void initCategoryListView() {
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
        signs = ButterKnife.findById(filterSpinners, R.id.sign_spinner);

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

        ViewGroup toolbarContent = ResourcesUtils.getToolBarContent();
        toolbarContent.removeAllViews();
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

    private CategoryMangerListAdapter getCategoryListAdapter(){
        HeaderViewListAdapter wrapperAdapter = (HeaderViewListAdapter) categoriesListView.getAdapter();
        return (CategoryMangerListAdapter) wrapperAdapter.getWrappedAdapter();
    }
}
