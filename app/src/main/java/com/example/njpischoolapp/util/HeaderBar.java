package com.example.njpischoolapp.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.njpischoolapp.R;
import com.example.njpischoolapp.fragment.Fragment4;

/**
 * author：   wdl
 * time： 2018/9/17 16:27
 * des：    TODO
 */
public class HeaderBar extends FrameLayout {
    private Boolean isShowBack;
    private ImageView tLeftIv;
    private String titleText;
    private String rightText;

    public HeaderBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderBarWithLayout);

        isShowBack = typedArray.getBoolean(R.styleable.HeaderBarWithLayout_isShowBack, false);
        titleText = typedArray.getString(R.styleable.HeaderBarWithLayout_titleText);
        rightText = typedArray.getString(R.styleable.HeaderBarWithLayout_rightText);

        typedArray.recycle();
        View view = View.inflate(context, R.layout.layout_header_bar, this);
        view.findViewById(R.id.tLeftIv).setVisibility(isShowBack ? VISIBLE : GONE);

        TextView title = view.findViewById(R.id.tTitleTv);
        title.setText(titleText);
        TextView right = view.findViewById(R.id.tRightTv);
        right.setText(rightText);
        right.setVisibility(TextUtils.isEmpty(rightText)?GONE:VISIBLE);
        tLeftIv = view.findViewById(R.id.tLeftIv);
        tLeftIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment4.ab.dismiss();
            }
        });
    }
}
