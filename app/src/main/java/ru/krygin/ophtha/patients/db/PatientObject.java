package ru.krygin.ophtha.patients.db;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by krygin on 20.08.17.
 */

public class PatientObject extends RealmObject {

    private long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private Date dateOfBirth;
    private Boolean gender; // true - male, false - female, null - undefinded
    @PrimaryKey
    private String patient_id;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getPatientId() {
        return patient_id;
    }

    public void setPatientId(String patient_id) {
        this.patient_id = patient_id;
    }
}
