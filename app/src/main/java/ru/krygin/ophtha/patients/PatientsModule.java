package ru.krygin.ophtha.patients;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by krygin on 20.08.17.
 */

@Module
public class PatientsModule {

    @Provides
    @Singleton
    PatientsRepository providePatientRepository() {
        return new PatientsRepository();
    }
}
