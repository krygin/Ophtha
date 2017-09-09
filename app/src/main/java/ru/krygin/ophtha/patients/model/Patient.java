package ru.krygin.ophtha.patients.model;

import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

import ru.krygin.ophtha.examination.model.Examination;

/**
 * Created by krygin on 09.09.17.
 */

public class Patient {
    private long mUUID;
    private String mLastName;
    private String mFirstName;
    private String mPatronymic;
    private Gender mGender;
    private String mPatientId;
    private Date mBirthday;
    private List<Examination> mExaminations;

    public void setUUID(long UUID) {
        mUUID = UUID;
    }

    public long getUUID() {
        return mUUID;
    }

    public void setLastName(String lastname) {
        mLastName = lastname;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setPatronymic(String patronymic) {
        mPatronymic = patronymic;
    }

    public String getPatronymic() {
        return mPatronymic;
    }

    public void setGender(Gender gender) {
        mGender = gender;
    }

    public Gender getGender() {
        return mGender;
    }

    public void setPatientId(String patientId) {
        mPatientId = patientId;
    }

    public String getPatientId() {
        return mPatientId;
    }

    public void setBirthday(Date birthday) {
        mBirthday = birthday;
    }

    public Date getBirthday() {
        return mBirthday;
    }

    public void setExaminations(List<Examination> examinations) {
        mExaminations = examinations;
    }

    public List<Examination> getExaminations() {
        return mExaminations;
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
        public Boolean toBoolean() {
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
