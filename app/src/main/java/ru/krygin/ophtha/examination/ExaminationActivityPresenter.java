package ru.krygin.ophtha.examination;

import android.net.Uri;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.core.async.UseCaseHandler;
import ru.krygin.ophtha.examination.model.Examination;
import ru.krygin.ophtha.examination.model.Snapshot;
import ru.krygin.ophtha.oculus.Oculus;

/**
 * Created by krygin on 10.09.17.
 */

@InjectViewState
public class ExaminationActivityPresenter extends MvpPresenter<ExaminationView> {

    @Inject
    UseCaseHandler useCaseHandler;

    private Examination mExamination;
    private Oculus mPendingOculusToAdd;


    public ExaminationActivityPresenter() {
        Injector.getAppComponent().inject(this);
    }

    void loadExamination(long examinationUUID) {
        if (mExamination != null) {
            getViewState().showExamination(mExamination);
        }
        useCaseHandler.execute(new GetExaminationsUseCase(), new GetExaminationsUseCase.RequestValues(examinationUUID), new UseCase.UseCaseCallback<GetExaminationsUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetExaminationsUseCase.ResponseValue response) {
                mExamination = response.getExamination();
                if (mExamination == null) {
                    mExamination = new Examination();
                }
                getViewState().showExamination(mExamination);
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

    public void addNewSnapshot(String photoUri, long photoTimestamp) {
        Snapshot snapshot = new Snapshot();
        snapshot.setUUID(photoTimestamp);
        snapshot.setFilename(photoUri);
        snapshot.setOculus(mPendingOculusToAdd);
        mExamination.getSnapshots().add(snapshot);

        useCaseHandler.execute(new SaveExaminationUseCase(), new SaveExaminationUseCase.RequestValues(mExamination), new UseCase.UseCaseCallback<SaveExaminationUseCase.ResponseValue>() {
            @Override
            public void onSuccess(SaveExaminationUseCase.ResponseValue response) {
                getViewState().notifyChanges();
            }

            @Override
            public void onError() {

            }
        });
    }
}
