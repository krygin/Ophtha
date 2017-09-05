package ru.krygin.ophtha.patients;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.core.async.UseCaseHandler;

/**
 * Created by krygin on 20.08.17.
 */

@InjectViewState
public class CreateOrUpdatePatientPresenter extends MvpPresenter<PatientView> {

    @Inject
    UseCaseHandler mUseCaseHandler;

    public CreateOrUpdatePatientPresenter() {
        Injector.getAppComponent().inject(this);
    }


    public void savePatient(int id, String lastName, String firstName, String patronymic, PatientsRepository.Patient.Gender gender, String patientId) {
        PatientsRepository.Patient patient = new PatientsRepository.Patient(id, lastName, firstName, patronymic, gender, patientId);


        SavePatientUseCase.RequestValues requestValues = new SavePatientUseCase.RequestValues(patient);

        mUseCaseHandler.execute(new SavePatientUseCase(), requestValues, new UseCase.UseCaseCallback<SavePatientUseCase.ResponseValue>() {
            @Override
            public void onSuccess(SavePatientUseCase.ResponseValue response) {

            }

            @Override
            public void onError() {

            }
        });
    }
}
