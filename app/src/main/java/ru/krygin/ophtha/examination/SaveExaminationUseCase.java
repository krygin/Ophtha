package ru.krygin.ophtha.examination;

import io.realm.Realm;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.examination.db.ExaminationObject;
import ru.krygin.ophtha.examination.model.Examination;
import ru.krygin.ophtha.patients.PatientsRepository;

/**
 * Created by krygin on 11.09.17.
 */

public class SaveExaminationUseCase extends UseCase<SaveExaminationUseCase.RequestValues,SaveExaminationUseCase.ResponseValue> {


    @Override
    protected void executeUseCase(RequestValues requestValues) {
        ExaminationObject examinationObject = PatientsRepository.examinationTransformerReverse.apply(requestValues.getExamination());

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        realm.insertOrUpdate(examinationObject);
        realm.commitTransaction();
        realm.close();


        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static class RequestValues implements UseCase.RequestValues {

        private final Examination mExamination;

        public RequestValues(Examination examination) {
            mExamination = examination;
        }

        public Examination getExamination() {
            return mExamination;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {

    }
}
