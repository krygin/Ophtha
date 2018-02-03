package ru.krygin.smart_sight.examination;

import com.arellomobile.mvp.MvpView;

import ru.krygin.smart_sight.examination.model.Examination;

/**
 * Created by krygin on 13.09.17.
 */

public interface CreateOrUpdateExaminationView extends MvpView {
    void showExamination(Examination examination);
    void close(Long patientUUID, Long examinationUUID);
}
