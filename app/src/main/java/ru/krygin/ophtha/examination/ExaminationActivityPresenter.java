package ru.krygin.ophtha.examination;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import javax.inject.Inject;

import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.core.async.UseCaseHandler;
import ru.krygin.ophtha.examination.model.Examination;
import ru.krygin.ophtha.examination.model.Snapshot;
import ru.krygin.ophtha.oculus.Oculus;
import ru.krygin.ophtha.patients.GetPatientUseCase;
import ru.krygin.ophtha.patients.SavePatientUseCase;
import ru.krygin.ophtha.patients.model.Patient;

/**
 * Created by krygin on 10.09.17.
 */

@InjectViewState
public class ExaminationActivityPresenter extends MvpPresenter<ExaminationView> {

    @Inject
    UseCaseHandler useCaseHandler;

    private Oculus mPendingOculusToAdd;
    private Patient mPatient;


    public ExaminationActivityPresenter() {
        Injector.getAppComponent().inject(this);
    }

    void loadPatient(long patientUUID, long examinationUUID) {
        if (mPatient != null) {
            Examination examination = Iterables.find(mPatient.getExaminations(), input -> input.getUUID() == examinationUUID, null);
            if (examination != null) {
                getViewState().showExamination(examination);
            }
        }
        useCaseHandler.execute(new GetPatientUseCase(), new GetPatientUseCase.RequestValues(patientUUID), new UseCase.UseCaseCallback<GetPatientUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetPatientUseCase.ResponseValue response) {
                mPatient = response.getPatient();
                Examination examination = Iterables.find(mPatient.getExaminations(), input -> input.getUUID() == examinationUUID, null);
                if (examination != null) {
                    getViewState().showExamination(examination);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    public void addOculusSnapshot(Oculus oculus) {
        getViewState().requestNewSnapshot(oculus);
        mPendingOculusToAdd = oculus;
    }

    public void addNewSnapshot(String photoUri, long photoTimestamp, long examinationUUID) {
        Snapshot snapshot = new Snapshot();
        snapshot.setUUID(photoTimestamp);
        snapshot.setFilename(photoUri);
        snapshot.setOculus(mPendingOculusToAdd);
        Examination examination = Iterables.find(mPatient.getExaminations(), input -> input.getUUID() == examinationUUID, null);
        examination.getSnapshots().add(snapshot);

        useCaseHandler.execute(new SavePatientUseCase(), new SavePatientUseCase.RequestValues(mPatient), new UseCase.UseCaseCallback<SavePatientUseCase.ResponseValue>() {
            @Override
            public void onSuccess(SavePatientUseCase.ResponseValue response) {
                getViewState().notifyChanges();
            }

            @Override
            public void onError() {

            }
        });
    }
}