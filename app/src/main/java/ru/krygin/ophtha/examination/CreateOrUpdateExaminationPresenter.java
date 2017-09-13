package ru.krygin.ophtha.examination;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Date;

import javax.inject.Inject;

import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.core.async.UseCaseHandler;
import ru.krygin.ophtha.examination.model.Examination;
import ru.krygin.ophtha.patients.SavePatientUseCase;

/**
 * Created by krygin on 13.09.17.
 */

@InjectViewState
public class CreateOrUpdateExaminationPresenter extends MvpPresenter<CreateOrUpdateExaminationView> {
    @Inject
    UseCaseHandler mUseCaseHandler;
    private Examination mExamination;

    public CreateOrUpdateExaminationPresenter() {
        Injector.getAppComponent().inject(this);
    }

    void loadExamination(long examinationUUID) {
        if (mExamination != null) {
            getViewState().showExamination(mExamination);
        } else {
            mUseCaseHandler.execute(new GetExaminationsUseCase(), new GetExaminationsUseCase.RequestValues(examinationUUID), new UseCase.UseCaseCallback<GetExaminationsUseCase.ResponseValue>() {
                @Override
                public void onSuccess(GetExaminationsUseCase.ResponseValue response) {
                    mExamination = response.getExamination();
                    if (mExamination != null) {
                        getViewState().showExamination(mExamination);
                    }
                }

                @Override
                public void onError() {

                }
            });
        }

    }

    void saveExamination(String title, Date date, String diagnosis, String comment) {

        if (mExamination.getUUID() == 0) {
            mExamination.setUUID(System.currentTimeMillis());
        }
        mExamination.setTitle(title);
        mExamination.setDate(date);
        mExamination.setDiagnosis(diagnosis);
        mExamination.setComment(comment);

        SaveExaminationUseCase.RequestValues requestValues = new SaveExaminationUseCase.RequestValues(mExamination);

        mUseCaseHandler.execute(new SaveExaminationUseCase(), requestValues, new UseCase.UseCaseCallback<SaveExaminationUseCase.ResponseValue>() {
            @Override
            public void onSuccess(SaveExaminationUseCase.ResponseValue response) {
                getViewState().close();
            }

            @Override
            public void onError() {

            }
        });

    }

}
