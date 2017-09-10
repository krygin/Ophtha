package ru.krygin.ophtha.examination;

import java.util.List;

import io.realm.Realm;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.examination.db.ExaminationObject;
import ru.krygin.ophtha.examination.model.Examination;
import ru.krygin.ophtha.oculus.Oculus;
import ru.krygin.ophtha.patients.PatientsRepository;

/**
 * Created by krygin on 06.08.17.
 */

public class GetExaminationsUseCase extends UseCase<GetExaminationsUseCase.RequestValues, GetExaminationsUseCase.ResponseValue> {

    public static class RequestValues implements UseCase.RequestValues {

        private final long mExaminationUUID;

        public RequestValues(long examinationUUID) {
            mExaminationUUID = examinationUUID;
        }

        public long getExaminationUUID() {
            return mExaminationUUID;
        }
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        Realm realm = Realm.getDefaultInstance();
        ExaminationObject examinationObject = realm.where(ExaminationObject.class).equalTo("UUID", requestValues.getExaminationUUID()).findFirst();
        Examination examination = PatientsRepository.examinationTransformer.apply(examinationObject);
        ResponseValue responseValue = new ResponseValue(examination);
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
