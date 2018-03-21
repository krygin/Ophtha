package ru.krygin.smart_sight;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Component;
import ru.krygin.smart_sight.comparation.OculusSnapshotFragment;
import ru.krygin.smart_sight.core.ui.BaseActivity;
import ru.krygin.smart_sight.core.ui.BaseFragment;
import ru.krygin.smart_sight.examination.CreateOrUpdateExaminationPresenter;
import ru.krygin.smart_sight.examination.ExaminationActivityPresenter;
import ru.krygin.smart_sight.examination.ExaminationSection;
import ru.krygin.smart_sight.examination.OculusSnapshotsAdapter;
import ru.krygin.smart_sight.examination.use_cases.GetExaminationsUseCase;
import ru.krygin.smart_sight.examination.use_cases.SaveExaminationUseCase;
import ru.krygin.smart_sight.snapshot.PrintReportAdapter;
import ru.krygin.smart_sight.snapshot.use_cases.GetExtendedOculusSnapshotUseCase;
import ru.krygin.smart_sight.snapshot.use_cases.GetOculusSnapshotUseCase;
import ru.krygin.smart_sight.patients.CreateOrUpdatePatientPresenter;
import ru.krygin.smart_sight.patients.use_cases.GetPatientUseCase;
import ru.krygin.smart_sight.patients.use_cases.GetPatientsUseCase;
import ru.krygin.smart_sight.patients.PatientsModule;
import ru.krygin.smart_sight.patients.use_cases.SavePatientUseCase;
import ru.krygin.smart_sight.snapshot.ViewSnapshotActivity;
import ru.krygin.smart_sight.snapshot.use_cases.RemoveSnapshotUseCase;
import ru.krygin.smart_sight.snapshot.use_cases.SaveSnapshotUseCase;
import ru.krygin.smart_sight.snapshot.use_cases.UpdateSnapshotUseCase;

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

    void inject(RemoveSnapshotUseCase removeSnapshotUseCase);

    void inject(OculusSnapshotsAdapter oculusSnapshotsAdapter);

    void inject(ExaminationSection examinationSection);

    void inject(ViewSnapshotActivity viewSnapshotActivity);

    void inject(OculusSnapshotFragment oculusSnapshotFragment);

    void inject(ru.krygin.smart_sight.comparation.ExaminationSection examinationSection);

    void inject(GetExtendedOculusSnapshotUseCase getExtendedOculusSnapshotUseCase);

    void inject(PrintReportAdapter printReportAdapter);

    void inject(@NotNull UpdateSnapshotUseCase updateSnapshotUseCase);
}
