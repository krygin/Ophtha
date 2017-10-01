package ru.krygin.ophtha.examination.use_cases;

import javax.inject.Inject;

import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.examination.db.ExaminationsRepository;
import ru.krygin.ophtha.examination.model.Examination;

/**
 * Created by krygin on 11.09.17.
 */

public class SaveExaminationUseCase extends UseCase<SaveExaminationUseCase.RequestValues, SaveExaminationUseCase.ResponseValue> {

    @Inject
    ExaminationsRepository mExaminationRepository;

    public SaveExaminationUseCase() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mExaminationRepository.createOrUpdateExamination(requestValues.getExamination());
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static class RequestValues implements UseCase.RequestValues {

        private final Examination mExamination;
        private final long mPatientUUID;

        public RequestValues(long patientUUID, Examination examination) {
            mPatientUUID = patientUUID;
            mExamination = examination;
        }

        public Examination getExamination() {
            return mExamination;
        }

        public long getPatientUUID() {
            return mPatientUUID;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {

    }
}
