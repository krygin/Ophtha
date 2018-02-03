package ru.krygin.smart_sight.patients;

import com.arellomobile.mvp.MvpView;

import ru.krygin.smart_sight.patients.model.Patient;

/**
 * Created by krygin on 20.08.17.
 */

public interface PatientView extends MvpView {
    void showPatient(Patient patient);

    void close(long uuid);
}
