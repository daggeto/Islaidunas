package com.islaidunas.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.islaidunas.R;
import com.islaidunas.domain.Category;
import com.islaidunas.domain.CategorySign;
import com.islaidunas.utils.ResourcesUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by daggreto on 2014.07.15.
 */
public class CategoryManagerListItemView extends FrameLayout{

    @InjectView(R.id.cat_image)
    ImageView image;

    @InjectView(R.id.category_name)
    TextView name;

    @InjectView(R.id.category_sign)
    TextView sign;

    @InjectView(R.id.category_bucket)
    TextView bucket;

    private Category category;

    public CategoryManagerListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryManagerListItemView(Context context, Category category){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.category_manager_list_item, this, true);

        ButterKnife.inject(this);

        this.category = category;
        image.setImageDrawable(generateIcon());
        name.setText(category.getTitle());

        String signStr = CategorySign.getCategorySign(category.getSign()).toString();
        sign.setText(ResourcesUtils.addWhiteSpaceToEnd(signStr));
        if(category.getSign()){
            sign.setTextColor(ResourcesUtils.getColor(R.color.positive));
        }else{
            sign.setTextColor(ResourcesUtils.getColor(R.color.negative));
        }

        String bucketStr = category.getBucket().getName();
        bucket.setText(ResourcesUtils.addWhiteSpaceToEnd(bucketStr));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    private Drawable getDrawable(Category item){
        int resourceId = getContext().getResources().getIdentifier(item.getSrc(), "drawable", getContext().getPackageName());

        if(resourceId == 0){
            resourceId = getContext().getResources().getIdentifier("def", "drawable", getContext().getPackageName());
        }

        return getContext().getResources().getDrawable(resourceId);
    }

    private Drawable generateIcon(){
        Shape shape = new RectShape();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        return new ShapeDrawable();
    }

    private CategorySign convertSign(boolean sign){
        return sign ? CategorySign.positive : CategorySign.negative;
    }

    public String getCategoryCode(){
        return category.getCode();
    }
}
