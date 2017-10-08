package ru.krygin.smart_sight.examination;

import com.arellomobile.mvp.MvpView;

import ru.krygin.smart_sight.examination.model.Examination;
import ru.krygin.smart_sight.oculus.Oculus;

/**
 * Created by krygin on 10.09.17.
 */

public interface ExaminationView extends MvpView {

    void showExamination(Examination examination);

    void requestNewSnapshot(Oculus oculus);

    void notifyChanges();
}
