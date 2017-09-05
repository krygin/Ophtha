package ru.krygin.ophtha;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.krygin.ophtha.core.async.UseCaseHandler;
import ru.krygin.ophtha.core.async.UseCaseThreadPoolScheduler;

/**
 * Created by krygin on 04.08.17.
 */
@Module
public class AsyncModule {

    @Singleton
    @Provides
    UseCaseHandler provideUseCaseHandler(Context context) {
        return new UseCaseHandler(new UseCaseThreadPoolScheduler());
    }
}
