package ru.krygin.ophtha.patients;

import javax.inject.Inject;

import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.patients.model.Patient;

/**
 * Created by krygin on 06.09.17.
 */

public class GetPatientUseCase extends UseCase<GetPatientUseCase.RequestValues, GetPatientUseCase.ResponseValue> {
    @Inject
    PatientsRepository mPatientsRepository;


    public GetPatientUseCase() {
        Injector.getAppComponent().inject(this);
    }


    @Override
    protected void executeUseCase(GetPatientUseCase.RequestValues requestValues) {
        Patient patient = mPatientsRepository.getPatient(requestValues.getPatientUUID());
        GetPatientUseCase.ResponseValue responseValue = new GetPatientUseCase.ResponseValue(patient);
        getUseCaseCallback().onSuccess(responseValue);
    }

    public static class RequestValues implements UseCase.RequestValues {

        private final long mPatientUUID;

        public RequestValues(long patientUUID) {
            mPatientUUID = patientUUID;
        }

        public long getPatientUUID() {
            return mPatientUUID;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {

        private final Patient mPatient;

        public ResponseValue(Patient patient) {
            mPatient = patient;
        }

        public Patient getPatient() {
            return mPatient;
        }
    }
}