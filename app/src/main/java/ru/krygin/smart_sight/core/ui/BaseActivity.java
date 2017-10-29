package ru.krygin.smart_sight.core.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatActivity;

import javax.inject.Inject;

import ru.krygin.smart_sight.core.Injector;
import ru.krygin.smart_sight.core.async.UseCaseHandler;

/**
 * Created by krygin on 04.08.17.
 */

public class BaseActivity extends MvpAppCompatActivity {

    @Inject
    UseCaseHandler mUseCaseHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Injector.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    protected UseCaseHandler getUseCaseHandler() {
        return mUseCaseHandler;
    }
}
