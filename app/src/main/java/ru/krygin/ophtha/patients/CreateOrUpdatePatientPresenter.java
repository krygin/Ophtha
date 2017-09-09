package ru.krygin.ophtha.patients;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Date;

import javax.inject.Inject;

import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.core.async.UseCaseHandler;
import ru.krygin.ophtha.patients.model.Patient;

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


    public void savePatient(long id, String lastName, String firstName, String patronymic, Patient.Gender gender, String patientId, Date birthday) {

        Patient patient = new Patient();
        patient.setUUID(id);
        patient.setLastName(lastName);
        patient.setFirstName(firstName);
        patient.setPatronymic(patronymic);
        patient.setGender(gender);
        patient.setPatientId(patientId);
        patient.setBirthday(birthday);

        SavePatientUseCase.RequestValues requestValues = new SavePatientUseCase.RequestValues(patient);

        mUseCaseHandler.execute(new SavePatientUseCase(), requestValues, new UseCase.UseCaseCallback<SavePatientUseCase.ResponseValue>() {
            @Override
            public void onSuccess(SavePatientUseCase.ResponseValue response) {
                getViewState().close();
            }

            @Override
            public void onError() {

            }
        });
    }

    public void loadPatient(long patientUUID) {
        mUseCaseHandler.execute(new GetPatientUseCase(), new GetPatientUseCase.RequestValues(patientUUID), new UseCase.UseCaseCallback<GetPatientUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetPatientUseCase.ResponseValue response) {
                Patient patient = response.getPatient();
                getViewState().showPatient(patient);
            }

            @Override
            public void onError() {

            }
        });
    }
}
