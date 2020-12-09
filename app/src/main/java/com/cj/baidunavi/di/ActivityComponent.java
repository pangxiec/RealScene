package com.cj.baidunavi.di;

import com.cj.baidunavi.ui.Main2Activity;
import dagger.Subcomponent;

/**
 * Created by 大头 on 2020/5/7.
 */
@ActivityScope
@Subcomponent()
public interface ActivityComponent {
    void bind(Main2Activity mainActivity);
}
