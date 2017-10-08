package ru.krygin.smart_sight.examination.db;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import ru.krygin.smart_sight.DatabaseHelper;
import ru.krygin.smart_sight.examination.model.Examination;
import ru.krygin.smart_sight.patients.db.PatientData;

import static ru.krygin.smart_sight.patients.db.PatientsRepository.examinationTransformer;
import static ru.krygin.smart_sight.patients.db.PatientsRepository.examinationTransformerReverse;

/**
 * Created by Ivan on 01.10.2017.
 */

public class ExaminationsRepository {


    private final DatabaseHelper mDatabaseHelper;

    public ExaminationsRepository(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public Examination getExamination(long examinationId) {
        RuntimeExceptionDao<ExaminationData, Long> patientDataDao = mDatabaseHelper.getRuntimeExceptionDao(ExaminationData.class);
        ExaminationData examinationData = patientDataDao.queryForId(examinationId);
        Examination examination = examinationTransformer.apply(examinationData);
        return examination;
    }

    public void createOrUpdateExamination(long patientUUID, Examination examination) {
        RuntimeExceptionDao<ExaminationData, Long> examinationDataDao = mDatabaseHelper.getRuntimeExceptionDao(ExaminationData.class);
        ExaminationData examinationData = examinationTransformerReverse.apply(examination);
        PatientData patientData = new PatientData();
        patientData.setUUID(patientUUID);
        examinationData.setPatient(patientData);
        examinationDataDao.createOrUpdate(examinationData);
    }
}
