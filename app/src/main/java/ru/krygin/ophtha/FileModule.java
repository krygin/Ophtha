package ru.krygin.ophtha;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by krygin on 17.08.17.
 */

@Module
public class FileModule {

    @Provides
    @Singleton
    FileUriProvider provideFileUriHelper(Context context) {
        return new FileUriProvider(context);
    }
}
