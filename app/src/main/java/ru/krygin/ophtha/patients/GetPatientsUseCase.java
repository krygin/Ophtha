package ru.krygin.ophtha.patients;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.core.async.UseCase;

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
        List<PatientsRepository.Patient> patients = mPatientsRepository.getPatients();

        patientItems.addAll(patients);
        ResponseValue responseValue = new ResponseValue(patients);
        getUseCaseCallback().onSuccess(responseValue);
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValue implements UseCase.ResponseValue {

        private final List<PatientsRepository.Patient> mPatients;

        public ResponseValue(List<PatientsRepository.Patient> patientItems) {
            mPatients = patientItems;
        }

        public List<PatientsRepository.Patient> getPatients() {
            return mPatients;
        }
    }


    public static List<PatientsRepository.Patient> patientItems = new ArrayList<PatientsRepository.Patient>() {{
        add(new PatientsRepository.Patient(1, "Путин", "Владимир", "Владимирович", PatientsRepository.Patient.Gender.M, "123123123"));
        add(new PatientsRepository.Patient(2, "Медведев", "Дмитрий", "Анатольевич", PatientsRepository.Patient.Gender.M, "12323"));
        add(new PatientsRepository.Patient(3, "Крыгин", "Иван", "Александрович", PatientsRepository.Patient.Gender.M, "123123"));
    }};

}
