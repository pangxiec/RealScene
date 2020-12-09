package com.cj.baidunavi.di;

import android.app.Application;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Created by 大头 on 2020/5/7.
 */
@Singleton
@Component(modules = {AppModule.class, RepositoryModule.class})
public interface AppComponent {
    Application getApplication();
    ActivityComponent getActivityComponent();
    FragmentComponent getFragmentComponent();
    VMAComponent getVmaComponent();
    VMFComponent getVmfComponent();
}
