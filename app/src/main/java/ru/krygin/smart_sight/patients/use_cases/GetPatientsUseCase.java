package ru.krygin.smart_sight.patients.use_cases;

import java.util.List;

import javax.inject.Inject;

import ru.krygin.smart_sight.core.Injector;
import ru.krygin.smart_sight.core.async.UseCase;
import ru.krygin.smart_sight.patients.PatientsFilter;
import ru.krygin.smart_sight.patients.db.PatientsRepository;
import ru.krygin.smart_sight.patients.model.Patient;

/**
 * Created by krygin on 04.08.17.
 */

public class GetPatientsUseCase extends UseCase<GetPatientsUseCase.RequestValues, GetPatientsUseCase.ResponseValue> {

    @Inject
    PatientsRepository mPatientsRepository;

    public GetPatientsUseCase() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    protected void executeUseCase(GetPatientsUseCase.RequestValues requestValues) {
        List<Patient> patients = mPatientsRepository.getPatients(requestValues.getPatientsFilter().getSearchQuery());
        ResponseValue responseValue = new ResponseValue(patients);
        getUseCaseCallback().onSuccess(responseValue);
    }

    public static class RequestValues implements UseCase.RequestValues {

        private final PatientsFilter mPatientsFilter;

        public RequestValues(PatientsFilter patientsFilter) {
            mPatientsFilter = patientsFilter;
        }

        public PatientsFilter getPatientsFilter() {
            return mPatientsFilter;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {

        private final List<Patient> mPatients;

        public ResponseValue(List<Patient> patientItems) {
            mPatients = patientItems;
        }

        public List<Patient> getPatients() {
            return mPatients;
        }
    }

}
