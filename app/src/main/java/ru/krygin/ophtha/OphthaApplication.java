package ru.krygin.ophtha;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.krygin.ophtha.core.Injector;

/**
 * Created by krygin on 02.08.17.
 */

public class OphthaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        Realm.init(this);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.deleteRealm(realmConfiguration); // Clean slate
//        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default

        OphthaAppComponent appComponent = DaggerOphthaAppComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .asyncModule(new AsyncModule())
                .build();
        Injector.setAppComponent(appComponent);
    }
}
