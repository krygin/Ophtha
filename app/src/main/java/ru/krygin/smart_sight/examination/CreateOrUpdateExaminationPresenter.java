package ru.krygin.smart_sight.examination;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Date;

import javax.inject.Inject;

import ru.krygin.smart_sight.core.Injector;
import ru.krygin.smart_sight.core.async.UseCase;
import ru.krygin.smart_sight.core.async.UseCaseHandler;
import ru.krygin.smart_sight.examination.model.Examination;
import ru.krygin.smart_sight.examination.use_cases.GetExaminationsUseCase;
import ru.krygin.smart_sight.examination.use_cases.SaveExaminationUseCase;

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

    void saveExamination(long patientUUID, Examination.Type type, Date date, String comment) {
        if (mExamination == null) {
            mExamination = new Examination();
            mExamination.setUUID(System.currentTimeMillis());
        }
        mExamination.setType(type.ordinal());
        mExamination.setDate(date);
        mExamination.setComment(comment);

        SaveExaminationUseCase.RequestValues requestValues = new SaveExaminationUseCase.RequestValues(patientUUID, mExamination);

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
