package com.cj.baidunavi.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

/**
 * Created by 大头 on 2020/5/12.
 */
public abstract class BaseVM2Activity<VM extends Base2ViewModel> extends Base2Activity {
    protected VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initVM();
        super.onCreate(savedInstanceState);
    }

    private void initVM() {
        if(provideVM()!=null){
            viewModel= ViewModelProviders.of(this).get(provideVM());
        }
    }

    protected abstract Class<VM> provideVM();
}
