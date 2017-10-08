package ru.krygin.smart_sight.patients;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.krygin.smart_sight.examination.db.ExaminationsRepository;
import ru.krygin.smart_sight.patients.db.PatientsRepository;
import ru.krygin.smart_sight.snapshot.db.SnapshotsRepository;

/**
 * Created by krygin on 20.08.17.
 */

@Module
public class PatientsModule {

    @Provides
    @Singleton
    PatientsRepository providePatientRepository(Context context) {
        return new PatientsRepository(context);
    }

    @Provides
    @Singleton
    ExaminationsRepository provideExaminationRepository(Context context) {
        return new ExaminationsRepository(context);
    }

    @Provides
    @Singleton
    SnapshotsRepository provideSnapshotsRepository(Context context) {
        return new SnapshotsRepository(context);
    }
}
