package com.cj.baidunavi.di;

import android.app.Application;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by 大头 on 2020/5/7.
 */
@Module
public class AppModule {
    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public Application provideApp(){
        return  application;
    }
}
