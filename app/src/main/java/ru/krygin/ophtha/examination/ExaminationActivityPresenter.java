package ru.krygin.ophtha.examination;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.common.collect.Iterables;

import javax.inject.Inject;

import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.core.async.UseCaseHandler;
import ru.krygin.ophtha.examination.model.Examination;
import ru.krygin.ophtha.snapshot.model.Snapshot;
import ru.krygin.ophtha.oculus.Oculus;
import ru.krygin.ophtha.patients.use_cases.GetPatientUseCase;
import ru.krygin.ophtha.patients.use_cases.SavePatientUseCase;
import ru.krygin.ophtha.patients.model.Patient;
import ru.krygin.ophtha.snapshot.use_cases.SaveSnapshotUseCase;

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
        useCaseHandler.execute(new SaveSnapshotUseCase(), new SaveSnapshotUseCase.RequestValues(examinationUUID, snapshot), new UseCase.UseCaseCallback<SaveSnapshotUseCase.ResponseValue>() {
            @Override
            public void onSuccess(SaveSnapshotUseCase.ResponseValue response) {
                getViewState().notifyChanges();
            }

            @Override
            public void onError() {

            }
        });
    }
}
