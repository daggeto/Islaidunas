package com.islaidunas.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.islaidunas.R;
import com.islaidunas.domain.Category;

import java.util.List;

/**
 * Created by daggreto on 2014.05.08.
 */
public class CategorySpinnerAdapter extends ArrayAdapter<Category> {

    Context context;
    List<Category> categories;

    public CategorySpinnerAdapter(Context context, List<Category> resource) {
        super(context, R.layout.category_list_item, resource);
        this.context = context;
        this.categories = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    //TODO: implement ButterKnife
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        Category item = getItem(position);

        View spinner = inflater.inflate(R.layout.category_list_item, parent, false);
        TextView text = (TextView) spinner.findViewById(R.id.category_name);
        text.setText(item.getTitle());

        ImageView image = (ImageView) spinner.findViewById(R.id.category_image);
        image.setImageDrawable(getDrawable(item));

        return spinner;
    }

    //TODO: implement converter
    private Drawable getDrawable(Category item){
        int resourceId = context.getResources().getIdentifier(item.getSrc(), "drawable", context.getPackageName());

        if(resourceId == 0){
            resourceId = context.getResources().getIdentifier("def", "drawable", context.getPackageName());
        }

        return context.getResources().getDrawable(resourceId);
    }
}