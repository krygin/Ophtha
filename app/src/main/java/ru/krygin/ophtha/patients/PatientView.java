package ru.krygin.ophtha.patients;

import com.arellomobile.mvp.MvpView;

import ru.krygin.ophtha.patients.model.Patient;

/**
 * Created by krygin on 20.08.17.
 */

public interface PatientView extends MvpView {
    void showPatient(Patient patient);

    void close();
}
