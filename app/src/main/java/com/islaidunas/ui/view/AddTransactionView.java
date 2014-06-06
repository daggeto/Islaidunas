package com.islaidunas.ui.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.islaidunas.R;
import com.islaidunas.adapter.CategorySpinnerAdapter;
import com.islaidunas.domain.Category;
import com.islaidunas.dto.TransactionAddViewDto;
import com.islaidunas.screen.AddTransactionScreen;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Required;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import mortar.Mortar;

/**
 * Created by daggreto on 2014.05.15.
 */
public class AddTransactionView extends LinearLayout implements DatePickerDialog.OnDateSetListener,
        Validator.ValidationListener{

    @Inject AddTransactionScreen.Presenter presenter;

    @Required(order = 1, messageResId = R.string.title_required)
    @InjectView(R.id.transaction_title)
    EditText title;

    @NumberRule(order = 1, messageResId = R.string.number_required, gt = 0, type = NumberRule.NumberType.DOUBLE)
    @InjectView(R.id.amount)
    EditText amount;

    @InjectView(R.id.transaction_category)
    Spinner category;

    @InjectView(R.id.transaction_date)
    Button date;

    private DatePickerDialog datePickerDialog;
    private TransactionAddViewDto transactionAddViewDto;

    GestureDetector detector;
    public boolean isScrolling = false;

    float dragSpeed = 1.5F;

    public AddTransactionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
        detector = new GestureDetector(context, new SwipeListener());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        presenter.takeView(this);
        presenter.initValidator(this);

        transactionAddViewDto = new TransactionAddViewDto(presenter.getTransaction());

        title.setText(transactionAddViewDto.getTitle());
        amount.setText(transactionAddViewDto.getAmount());
        initCategorySpinner();
        Calendar c = transactionAddViewDto.getCalendar();
        datePickerDialog = new DatePickerDialog(getContext(), this,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        date.setText(transactionAddViewDto.getDateString());

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        hideKeyboardFrom(amount);
    }

    private void initCategorySpinner() {
        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(getContext(), presenter.getCategories());
        category.setAdapter(adapter);
        category.setSelection(adapter.getPosition(transactionAddViewDto.getCategory()));
    }

    private void hideKeyboardFrom(View v){
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @OnItemSelected(R.id.transaction_category)
    public void onItemSelected(int position) {
        transactionAddViewDto.setCategory((Category) category.getAdapter().getItem(position));
    }

    @OnTextChanged(R.id.amount)
    public void onTextChange(CharSequence text) {
        transactionAddViewDto.setAmount(text.toString());
    }

    @OnTextChanged(R.id.transaction_title)
    public void onTitleChanged(CharSequence text){
        transactionAddViewDto.setTitle(text.toString());
    }

    @OnClick(R.id.transaction_date)
    public void onDateButtonClick(View view) {
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
        Calendar c = Calendar.getInstance();
        c.set(i, i2, i3);
        transactionAddViewDto.setDate(c.getTime());
        date.setText(transactionAddViewDto.getDateString());
    }

    @OnClick(R.id.add_transaction_view)
    public void click(View v){
        if(v.getId() != R.id.amount){
            hideKeyboardFrom(amount);
            return;
        }

        amount.selectAll();
    }


    @Override
    public void onValidationSucceeded() {
        presenter.setFormValid(true);
    }

    @Override
    public void onValidationFailed(View view, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        if (view instanceof EditText) {
            view.requestFocus();
            ((EditText) view).setError(message);
        } else {
            Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
        }
        presenter.setFormValid(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);

        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(isScrolling) {
                log("OnTouchListener --> onTouch ACTION_UP");
                isScrolling  = false;
                if(this.getTranslationX() < this.getWidth()/3){
                    this.setTranslationX(0);
                }
            };
        }

        return super.onTouchEvent(event);
    }

        class SwipeListener extends GestureDetector.SimpleOnGestureListener{

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                log("Fling: x=" + velocityX);
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                log("onLongPress");
                super.onLongPress(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                log("onSingleTapUp");
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                isScrolling = true;
                View currentView = AddTransactionView.this;

                float stopPos = 100;

                float cPos = currentView.getTranslationX();
                float nPos = cPos - distanceX/ dragSpeed;
                if(nPos > 0 && nPos < stopPos){
                    currentView.setTranslationX(nPos);
                } else if (nPos > stopPos){
                    presenter.goBack();
                }

                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        }

    private void log(String message){
        Log.d("GestureTest", message);
    }

}
