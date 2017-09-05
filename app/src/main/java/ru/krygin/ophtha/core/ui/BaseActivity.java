package ru.krygin.ophtha.core.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.arellomobile.mvp.MvpAppCompatActivity;

import javax.inject.Inject;

import ru.krygin.ophtha.OphthaApplication;
import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.core.async.UseCaseHandler;

/**
 * Created by krygin on 04.08.17.
 */

public class BaseActivity extends MvpAppCompatActivity {

    @Inject
    UseCaseHandler mUseCaseHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.getAppComponent().inject(this);
    }

    protected UseCaseHandler getUseCaseHandler() {
        return mUseCaseHandler;
    }
}
