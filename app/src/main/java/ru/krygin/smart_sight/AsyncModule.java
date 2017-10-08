package ru.krygin.smart_sight;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.krygin.smart_sight.core.async.UseCaseHandler;
import ru.krygin.smart_sight.core.async.UseCaseThreadPoolScheduler;

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
