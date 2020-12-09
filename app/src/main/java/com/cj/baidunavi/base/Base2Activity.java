package com.cj.baidunavi.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by 大头 on 2020/5/6.
 */
public abstract class Base2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        initView();
        initData();
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract int layoutId();
}
