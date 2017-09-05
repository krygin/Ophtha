package ru.krygin.ophtha.patients;

import com.arellomobile.mvp.MvpView;

/**
 * Created by krygin on 20.08.17.
 */

public interface PatientView extends MvpView {
    void showPatient(PatientsRepository.Patient patient);
}
