package ru.krygin.ophtha.patients.use_cases;

import javax.inject.Inject;

import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.patients.db.PatientsRepository;
import ru.krygin.ophtha.patients.model.Patient;

/**
 * Created by krygin on 20.08.17.
 */

public class SavePatientUseCase extends UseCase<SavePatientUseCase.RequestValues, SavePatientUseCase.ResponseValue> {

    @Inject
    PatientsRepository mPatientsRepository;

    public SavePatientUseCase() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mPatientsRepository.createOrUpdatePatient(requestValues.getPatient());

        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static class RequestValues implements UseCase.RequestValues {

        private final Patient mPatient;

        public RequestValues(Patient patient) {
            mPatient = patient;
        }

        public Patient getPatient() {
            return mPatient;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {

    }
}
