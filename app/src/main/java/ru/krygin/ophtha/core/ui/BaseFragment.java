package ru.krygin.ophtha.core.ui;

import android.content.Context;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import ru.krygin.ophtha.FileUriProvider;
import ru.krygin.ophtha.OphthaApplication;
import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.core.async.UseCaseHandler;

/**
 * Created by krygin on 06.08.17.
 */

public class BaseFragment extends Fragment {

    @Inject
    UseCaseHandler mUseCaseHandler;

    @Inject
    FileUriProvider mFileUriProvider;


    @Override
    public void onAttach(Context context) {
        Injector.getAppComponent().inject(this);
        super.onAttach(context);
    }

    protected UseCaseHandler getUseCaseHandler() {
        return mUseCaseHandler;
    }

    public FileUriProvider getFileUriProvider() {
        return mFileUriProvider;
    }
}
