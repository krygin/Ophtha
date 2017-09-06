package ru.krygin.ophtha.patients.db;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by krygin on 20.08.17.
 */

public class PatientObject extends RealmObject {

    public static final String UUID_FIELD = "UUID";

    @PrimaryKey
    private long UUID;
    private String firstName;
    private String lastName;
    private String patronymic;
    private Date dateOfBirth;
    private Boolean gender; // true - male, false - female, null - undefinded
    private String patientId;


    public long getUUID() {
        return UUID;
    }

    public void setId(long UUID) {
        this.UUID = UUID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getBirthday() {
        return dateOfBirth;
    }

    public void setBirthday(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

}
