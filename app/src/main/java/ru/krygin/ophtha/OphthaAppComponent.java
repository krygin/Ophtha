package ru.krygin.ophtha;

import javax.inject.Singleton;

import dagger.Component;
import ru.krygin.ophtha.core.ui.BaseActivity;
import ru.krygin.ophtha.core.ui.BaseFragment;
import ru.krygin.ophtha.patients.CreateOrUpdatePatientPresenter;
import ru.krygin.ophtha.patients.GetPatientUseCase;
import ru.krygin.ophtha.patients.GetPatientsUseCase;
import ru.krygin.ophtha.patients.PatientsModule;
import ru.krygin.ophtha.patients.SavePatientUseCase;

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
public interface OphthaAppComponent {
    void inject(BaseActivity activity);

    void inject(BaseFragment baseFragment);

    void inject(CreateOrUpdatePatientPresenter createOrUpdatePatientPresenter);

    void inject(SavePatientUseCase savePatientUseCase);

    void inject(GetPatientsUseCase getPatientsUseCase);

    void inject(GetPatientUseCase getPatientUseCase);
}
