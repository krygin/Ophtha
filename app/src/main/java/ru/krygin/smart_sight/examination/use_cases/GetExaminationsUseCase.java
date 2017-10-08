package ru.krygin.smart_sight.examination.use_cases;

import javax.inject.Inject;

import ru.krygin.smart_sight.core.Injector;
import ru.krygin.smart_sight.core.async.UseCase;
import ru.krygin.smart_sight.examination.db.ExaminationsRepository;
import ru.krygin.smart_sight.examination.model.Examination;

/**
 * Created by krygin on 06.08.17.
 */

public class GetExaminationsUseCase extends UseCase<GetExaminationsUseCase.RequestValues, GetExaminationsUseCase.ResponseValue> {


    @Inject
    ExaminationsRepository mExaminationsRepository;

    public GetExaminationsUseCase() {
        Injector.getAppComponent().inject(this);
    }

    public static class RequestValues implements UseCase.RequestValues {

        private final long mExaminationUUID;

        public RequestValues(long examinationUUID) {
            mExaminationUUID = examinationUUID;
        }

        public long getExaminationId() {
            return mExaminationUUID;
        }
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        Examination examinations = mExaminationsRepository.getExamination(requestValues.getExaminationId());
        GetExaminationsUseCase.ResponseValue responseValue = new GetExaminationsUseCase.ResponseValue(examinations);
        getUseCaseCallback().onSuccess(responseValue);
    }

    public static class ResponseValue implements UseCase.ResponseValue {

        private final Examination mExamination;

        public ResponseValue(Examination examination) {
            mExamination = examination;
        }

        public Examination getExamination() {
            return mExamination;
        }
    }


}
