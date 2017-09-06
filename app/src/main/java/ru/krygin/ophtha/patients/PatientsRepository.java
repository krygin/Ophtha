package ru.krygin.ophtha.patients;

import android.support.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Date;
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
        patientObject.setId(System.currentTimeMillis());
        patientObject.setFirstName(patient.getFirstName());
        patientObject.setLastName(patient.getLastName());
        patientObject.setPatronymic(patient.getPatronymic());
        patientObject.setGender(patient.getGender().toBoolean());
        patientObject.setPatientId(patient.getPatientId());
        patientObject.setBirthday(patient.getBirthday());

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        realm.insertOrUpdate(patientObject);
        realm.commitTransaction();
        realm.close();
    }


    public List<Patient> getPatients() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PatientObject> realmResults = realm.where(PatientObject.class).findAll();
        Iterable<Patient> patients = Iterables.transform(realmResults, patientTransformer);
        List<Patient> filteredPatients = Lists.newArrayList(patients);
        realm.close();
        return filteredPatients;
    }

    public Patient getPatient(long patientUUID) {
        Realm realm = Realm.getDefaultInstance();
        PatientObject patientObject = realm.where(PatientObject.class).equalTo(PatientObject.UUID_FIELD, patientUUID).findFirst();
        Patient patient = patientTransformer.apply(patientObject);
        realm.close();
        return patient;
    }

    public static class Patient {

        private final long mUUID;
        private final String mLastName;
        private final String mFirstName;
        private final String mPatronymic;
        private final Gender mGender;
        private final String mPatientId;
        private final Date mBirthday;

        public long getUUID() {
            return mUUID;
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

        public Patient(long UUID, String lastName, String firstName, String patronymic, Gender gender, String patientId, Date birthday) {
            mUUID = UUID;
            mLastName = lastName;
            mFirstName = firstName;
            mPatronymic = patronymic;
            mGender = gender;
            mPatientId = patientId;
            mBirthday = birthday;
        }

        public Date getBirthday() {
            return mBirthday;
        }

        public enum Gender {
            UNDEFINDED("Пол", "-"),
            M("Мужской", "М"),
            F("Женский", "Ж");

            private final String mGenderString;
            private final String mGenderShortString;

            Gender(String genderString, String genderShortString) {
                mGenderString = genderString;
                mGenderShortString = genderShortString;
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

            public String toShortString() {
                return mGenderShortString;
            }
        }
    }


    private static Function<PatientObject, Patient> patientTransformer = new Function<PatientObject, Patient>() {
        @javax.annotation.Nullable
        @Override
        public Patient apply(@javax.annotation.Nullable PatientObject input) {
            if (input == null) {
                return null;
            }
            Patient patient = new Patient(
                    input.getUUID(),
                    input.getLastName(),
                    input.getFirstName(),
                    input.getPatronymic(),
                    Patient.Gender.fromBoolean(input.getGender()),
                    input.getPatientId(),
                    input.getBirthday());
            return patient;
        }
    };
}
