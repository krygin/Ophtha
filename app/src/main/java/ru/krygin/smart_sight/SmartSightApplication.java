package ru.krygin.smart_sight;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import ru.krygin.smart_sight.core.Injector;

/**
 * Created by krygin on 02.08.17.
 */

public class SmartSightApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        SmartSightAppComponent appComponent = DaggerSmartSightAppComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .asyncModule(new AsyncModule())
                .build();
        Injector.setAppComponent(appComponent);
    }
}
