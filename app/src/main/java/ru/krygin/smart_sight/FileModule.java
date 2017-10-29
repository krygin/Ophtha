package ru.krygin.smart_sight;

import android.content.Context;
import android.os.Environment;

import java.io.File;

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
    FileUriProvider provideFileUriHelper(Context context, SmartSightFileManager fileManager) {
        return new FileUriProvider(context, fileManager);
    }

    @Provides
    @Singleton
    SmartSightFileManager provideFileManager(Context context) {
        return new SmartSightFileManager(new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "smart_sight_snapshots"));
    }
}
