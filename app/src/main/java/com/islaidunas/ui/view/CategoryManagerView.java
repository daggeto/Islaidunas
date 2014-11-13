package com.islaidunas.ui.view;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.islaidunas.IslaidunasApplication;
import com.islaidunas.R;
import com.islaidunas.adapter.CategoryMangerListAdapter;
import com.islaidunas.adapter.SingleStringArrayAdapter;
import com.islaidunas.domain.Bucket;
import com.islaidunas.domain.Category;
import com.islaidunas.dto.CategorySignDto;
import com.islaidunas.listener.SwipeListViewListenerImpl;
import com.islaidunas.screen.CategoryManagerScreen;
import com.islaidunas.ui.view.button.FloatingActionButton;
import com.islaidunas.utils.ResourcesUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortar.Mortar;
import mortar.MortarScope;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.concurrency.AndroidSchedulers;
import rx.concurrency.Schedulers;

/**
 * Created by daggreto on 2014.07.09.
 */
public class CategoryManagerView extends LinearLayout{

    @Inject CategoryManagerScreen.Presenter presenter;

    private Subscription categorySub;

    @InjectView(R.id.buckets_spinner)
    Spinner buckets;

    @InjectView(R.id.sign_spinner)
    Spinner signs;

    @InjectView(R.id.category_list)
    SwipeListView categoriesListView;

    FloatingActionButton button;

    private CategoryMangerListAdapter categoryAdapter;


    public CategoryManagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        initCategoryAdapter();
        iniSignsAdapter();
        initBucketsAdapter();

        categorySub = presenter.getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    categoryAdapter.setCategories(list);
                });

        button = new FloatingActionButton.Builder((Activity)IslaidunasApplication.getActivityContext())
                .withDrawable(getResources().getDrawable(android.R.drawable.ic_menu_add))
                .withButtonColor(ResourcesUtils.getColor(R.color.colorAccent))
                .withGravity(Gravity.TOP | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();


        button.setOnClickListener(view -> addClicked(view));
        button.setX(button.getX() + 100f);
        button.dragFAB(-100f, 0, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                filterCategories();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        presenter.takeView(this);
    }

    private void addClicked(View view){
    }

    private void initBucketsAdapter() {
        List<Bucket> bucketsToShow = new ArrayList<Bucket>();
        bucketsToShow.add(new Bucket(getContext().getString(R.string.all_buckets)));
        bucketsToShow.addAll(presenter.findAllBuckets());

        final SingleStringArrayAdapter<Bucket> bucketAdapter = new SingleStringArrayAdapter<Bucket>(this.getContext(), bucketsToShow);
        buckets.setAdapter(bucketAdapter);
        buckets.setOnItemSelectedListener(new FilterListener());
    }

    private void initCategoryAdapter() {
        categoryAdapter = new CategoryMangerListAdapter(this.getContext(), new ArrayList<>());
        categoriesListView.setAdapter(categoryAdapter);
        categoriesListView.setSwipeMode(SwipeListView.SWIPE_MODE_RIGHT);
        categoriesListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_DISMISS);
        categoriesListView.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
        categoriesListView.setOffsetLeft(ResourcesUtils.convertDpToPixel(0f));
        categoriesListView.setOffsetRight(ResourcesUtils.convertDpToPixel(160f));
        categoriesListView.setSwipeOpenOnLongPress(true);
        categoriesListView.setAnimationTime(100);
        categoriesListView.setSwipeListViewListener(new SwipeListViewListenerImpl(categoriesListView));

        filterCategories();
    }

    private void iniSignsAdapter() {
        List<CategorySignDto> categorySignDtos = new ArrayList<CategorySignDto>();
        categorySignDtos.add(new CategorySignDto(getContext().getString(R.string.all_signs)));
        categorySignDtos.addAll(presenter.getSigns());

        SingleStringArrayAdapter<CategorySignDto> signsAdapter = new SingleStringArrayAdapter<CategorySignDto>(this.getContext(), categorySignDtos);
        signs.setAdapter(signsAdapter);
        signs.setOnItemSelectedListener(new FilterListener());
    }

    private void filterCategories(){
        CategoryMangerListAdapter adapter = (CategoryMangerListAdapter) categoriesListView.getAdapter();
        adapter.filterCategories(
                (Bucket) buckets.getSelectedItem(),
                ((CategorySignDto) signs.getSelectedItem()));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        categorySub.unsubscribe();
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

}
