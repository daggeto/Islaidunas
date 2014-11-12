package com.islaidunas.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.islaidunas.R;
import com.islaidunas.domain.Category;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SingleStringArrayAdapter<T> extends ArrayAdapter<T> {

    Context context;
    List<T> items;

    @InjectView(android.R.id.text1)
    TextView labelText;

    public SingleStringArrayAdapter(Context context, List<T> resource) {
        super(context, R.layout.single_string_spinner, resource);
        this.context = context;
        this.items = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.single_string_spinner, parent, false);
        T item = getItem(position);
        inflateText(itemView, item);

        return itemView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.single_string_dropdown_item, parent, false);
        T item = getItem(position);
        inflateText(itemView, item);

        return itemView;
    }

    private void inflateText(View itemView, T item){
        ButterKnife.inject(this, itemView);
        labelText.setText(item.toString());
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