package ru.krygin.ophtha.patients;

import javax.inject.Inject;

import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.core.async.UseCase;

/**
 * Created by krygin on 20.08.17.
 */

public class SavePatientUseCase extends UseCase<SavePatientUseCase.RequestValues, SavePatientUseCase.ResponseValue> {

    @Inject PatientsRepository mPatientsRepository;

    public SavePatientUseCase() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mPatientsRepository.createOrUpdatePatient(requestValues.getPatient());
    }

    public static class RequestValues implements UseCase.RequestValues {

        private final PatientsRepository.Patient mPatient;

        public RequestValues(PatientsRepository.Patient patient) {
            mPatient = patient;
        }

        public PatientsRepository.Patient getPatient() {
            return mPatient;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {

    }
}
