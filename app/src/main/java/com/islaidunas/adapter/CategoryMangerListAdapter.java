package com.islaidunas.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.islaidunas.domain.Bucket;
import com.islaidunas.domain.Category;
import com.islaidunas.dto.CategorySignDto;
import com.islaidunas.ui.view.CategoryManagerListItemView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by daggreto on 2014.07.15.
 */
public class CategoryMangerListAdapter extends BaseAdapter{
    private Context context;
    private List<Category> categories;
    private List<Category> filteredCategories;

    public CategoryMangerListAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = new ArrayList<Category>(categories);
        this.filteredCategories = new ArrayList<Category>(categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null || isViewChanged(convertView, getItem(position))){
            convertView = new CategoryManagerListItemView(context, getItem(position));
        }

        return convertView;
    }

    private boolean isViewChanged(View view, Category currentCategory){
        CategoryManagerListItemView categoryView = (CategoryManagerListItemView) view;
        return !categoryView.getCategoryCode().equals(currentCategory.getCode());
    }

    @Override
    public int getCount() {
        return filteredCategories.size();
    }

    @Override
    public Category getItem(int position) {
        return filteredCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void  filterCategories(final Bucket bucket, final CategorySignDto categorySignDto){
        filteredCategories.clear();
        Observable
            .from(categories)
            .filter(category -> isNoBucketSelected(bucket) || category.getBucket().equals(bucket))
            .filter(category -> isNoSignSelected(categorySignDto) || categorySignDto.getSign() == category.getSign())
            .subscribe(category -> filteredCategories.add(category));
    }

    private boolean isNoBucketSelected(Bucket bucket){
        return bucket == null || bucket.getCode() == null;
    }

    private boolean isNoSignSelected(CategorySignDto categorySignDto){
        return categorySignDto == null || categorySignDto.getSign() == null;
    }

    public void setCategories(List<Category> categories){
        this.categories = categories;
    }

}
