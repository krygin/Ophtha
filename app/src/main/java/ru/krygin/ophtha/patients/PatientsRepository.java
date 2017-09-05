package ru.krygin.ophtha.patients;

import android.support.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.krygin.ophtha.patients.db.PatientObject;

/**
 * Created by krygin on 20.08.17.
 */

public class PatientsRepository {

    public void createOrUpdatePatient(Patient patient) {
        PatientObject patientObject = new PatientObject();
        patientObject.setId(patient.getId());
        patientObject.setFirstName(patient.getFirstName());
        patientObject.setLastName(patient.getLastName());
        patientObject.setPatronymic(patient.getPatronymic());
        patientObject.setGender(patient.getGender().toBoolean());
        patientObject.setPatientId(patient.getPatientId());

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(patientObject);
        realm.commitTransaction();
        realm.close();
    }


    public List<Patient> getPatients() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PatientObject> realmResults = realm.where(PatientObject.class).findAll();
        Iterable<Patient> patients = Iterables.transform(realmResults, new Function<PatientObject, Patient>() {
            @javax.annotation.Nullable
            @Override
            public Patient apply(@javax.annotation.Nullable PatientObject input) {
                if (input == null) {
                    return null;
                }
                Patient patient = new Patient(
                        input.getId(),
                        input.getLastName(),
                        input.getFirstName(),
                        input.getPatronymic(),
                        Patient.Gender.fromBoolean(input.getGender()),
                        input.getPatientId()
                );
                return patient;
            }
        });
        List<Patient> filteredPatients = Lists.newArrayList(patients);
        realm.close();
        return filteredPatients;
    }

    public static class Patient {

        private final long mId;
        private final String mLastName;
        private final String mFirstName;
        private final String mPatronymic;
        private final Gender mGender;
        private final String mPatientId;

        public long getId() {
            return mId;
        }

        public String getLastName() {
            return mLastName;
        }

        public String getFirstName() {
            return mFirstName;
        }

        public String getPatronymic() {
            return mPatronymic;
        }

        public Gender getGender() {
            return mGender;
        }

        public String getPatientId() {
            return mPatientId;
        }

        public Patient(long id, String lastName, String firstName, String patronymic, Gender gender, String patientId) {
            mId = id;
            mLastName = lastName;
            mFirstName = firstName;
            mPatronymic = patronymic;
            mGender = gender;
            mPatientId = patientId;
        }

        public enum Gender {
            UNDEFINDED("Пол"),
            M("Мужской"),
            F("Женский");

            private final String mGenderString;

            Gender(String genderString) {
                mGenderString = genderString;
            }

            @Override
            public String toString() {
                return mGenderString;
            }

            @Nullable
            Boolean toBoolean() {
                Boolean gender = null;
                switch (this) {
                    case M:
                        gender = true;
                        break;
                    case F:
                        gender = false;
                        break;
                    case UNDEFINDED:
                        gender = null;
                        break;
                }
                return gender;
            }

            public static Gender fromBoolean(Boolean genderBoolean) {
                Gender gender = UNDEFINDED;
                if (genderBoolean == Boolean.TRUE) {
                    gender = M;
                } else if (genderBoolean == Boolean.FALSE) {
                    gender = F;
                } else {
                    gender = UNDEFINDED;
                }
                return gender;
            }
        }
    }
}
