package ru.krygin.smart_sight;

import javax.inject.Singleton;

import dagger.Component;
import ru.krygin.smart_sight.core.ui.BaseActivity;
import ru.krygin.smart_sight.core.ui.BaseFragment;
import ru.krygin.smart_sight.examination.CreateOrUpdateExaminationPresenter;
import ru.krygin.smart_sight.examination.ExaminationActivityPresenter;
import ru.krygin.smart_sight.examination.use_cases.GetExaminationsUseCase;
import ru.krygin.smart_sight.examination.use_cases.SaveExaminationUseCase;
import ru.krygin.smart_sight.oculus.GetOculusSnapshotUseCase;
import ru.krygin.smart_sight.patients.CreateOrUpdatePatientPresenter;
import ru.krygin.smart_sight.patients.use_cases.GetPatientUseCase;
import ru.krygin.smart_sight.patients.use_cases.GetPatientsUseCase;
import ru.krygin.smart_sight.patients.PatientsModule;
import ru.krygin.smart_sight.patients.use_cases.SavePatientUseCase;
import ru.krygin.smart_sight.snapshot.use_cases.SaveSnapshotUseCase;

/**
 * Created by krygin on 02.08.17.
 */
@Singleton
@Component(modules = {
        ApplicationModule.class,
        AsyncModule.class,
        FileModule.class,
        PatientsModule.class
})
public interface SmartSightAppComponent {
    void inject(BaseActivity activity);

    void inject(BaseFragment baseFragment);

    void inject(CreateOrUpdatePatientPresenter createOrUpdatePatientPresenter);

    void inject(SavePatientUseCase savePatientUseCase);

    void inject(GetPatientsUseCase getPatientsUseCase);

    void inject(GetPatientUseCase getPatientUseCase);

    void inject(ExaminationActivityPresenter examinationPresenter);

    void inject(CreateOrUpdateExaminationPresenter createOrUpdateExaminationPresenter);

    void inject(GetExaminationsUseCase getExaminationsUseCase);

    void inject(SaveExaminationUseCase saveExaminationUseCase);

    void inject(GetOculusSnapshotUseCase getOculusSnapshotUseCase);

    void inject(SaveSnapshotUseCase saveSnapshotUseCase);
}