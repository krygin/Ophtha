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

    private Patient mPatient;

    public CreateOrUpdatePatientPresenter() {
        Injector.getAppComponent().inject(this);
    }


    public void savePatient(long id, String lastName, String firstName, String patronymic, Patient.Gender gender, String patientId, Date birthday) {
        if (mPatient.getUUID() == 0) {
            mPatient.setUUID(System.currentTimeMillis());
        }
        mPatient.setLastName(lastName);
        mPatient.setFirstName(firstName);
        mPatient.setPatronymic(patronymic);
        mPatient.setGender(gender);
        mPatient.setPatientId(patientId);
        mPatient.setBirthday(birthday);

        SavePatientUseCase.RequestValues requestValues = new SavePatientUseCase.RequestValues(mPatient);

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
        if (mPatient != null) {
            getViewState().showPatient(mPatient);
        } else {
            mUseCaseHandler.execute(new GetPatientUseCase(), new GetPatientUseCase.RequestValues(patientUUID), new UseCase.UseCaseCallback<GetPatientUseCase.ResponseValue>() {
                @Override
                public void onSuccess(GetPatientUseCase.ResponseValue response) {
                    mPatient = response.getPatient();
                    if (mPatient == null) {
                        mPatient = new Patient();
                    }
                    getViewState().showPatient(mPatient);
                }

                @Override
                public void onError() {

                }
            });
        }
    }
}
