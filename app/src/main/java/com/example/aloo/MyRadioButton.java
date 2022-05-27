package com.example.aloo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatRadioButton;

public class MyRadioButton extends AppCompatRadioButton{

    private  OnCheckedChangeListener onCheckedChangeListener;

    public MyRadioButton(Context context) {
        super(context);
    }

    public MyRadioButton( Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRadioButton( Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        setOwnOnCheckedChangeListener();
        setButtonDrawable(null);

    }
    public  void setOwnOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener){
        this.onCheckedChangeListener = onCheckedChangeListener;
    }
    private void setOwnOnCheckedChangeListener() {
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
                }
            }
        });
    }
}
