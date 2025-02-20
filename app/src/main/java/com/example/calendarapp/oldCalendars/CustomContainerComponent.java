package com.example.calendarapp.oldCalendars;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.calendarapp.R;

abstract public class CustomContainerComponent extends LinearLayout{
    protected LinearLayout scrollerContent;
    protected LinearLayout containerTitle;
    protected  CustomContainerComponent(Context context) {
        super(context);
        init(context);
    }

    protected  CustomContainerComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    protected  CustomContainerComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected  CustomContainerComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.container_component,this);

        this.scrollerContent = findViewById(R.id.scrollerContent);
        this.containerTitle = findViewById(R.id.containerTitle);
    }

}
