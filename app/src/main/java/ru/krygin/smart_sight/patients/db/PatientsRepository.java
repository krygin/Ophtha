package ru.krygin.smart_sight.patients.db;

import android.content.Context;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import ru.krygin.smart_sight.DatabaseHelper;
import ru.krygin.smart_sight.examination.db.ExaminationData;
import ru.krygin.smart_sight.examination.model.Examination;
import ru.krygin.smart_sight.oculus.Oculus;
import ru.krygin.smart_sight.patients.model.Patient;
import ru.krygin.smart_sight.snapshot.db.SnapshotData;
import ru.krygin.smart_sight.snapshot.model.Snapshot;

/**
 * Created by krygin on 20.08.17.
 */

public class PatientsRepository {

    private final DatabaseHelper mDatabaseHelper;

    public PatientsRepository(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public void createOrUpdatePatient(Patient patient) {
        RuntimeExceptionDao<PatientData, Long> patientDataDao = mDatabaseHelper.getRuntimeExceptionDao(PatientData.class);
        PatientData patientData = patientTransformerReverse.apply(patient);
        patientDataDao.createOrUpdate(patientData);
    }


    public List<Patient> getPatients() {
        return getPatients(null);
    }

    public List<Patient> getPatients(String query) {
        RuntimeExceptionDao<PatientData, Long> patientDataDao = mDatabaseHelper.getRuntimeExceptionDao(PatientData.class);
        List<PatientData> patientDataList;
        try {
            if (query != null && !query.isEmpty()) {
                patientDataList = patientDataDao.queryBuilder().where()
                        .like(PatientData.FIELD_FIRST_NAME, "%" + query + "%").or()
                        .like(PatientData.FIELD_LAST_NAME, "%" + query + "%").or()
                        .like(PatientData.FIELD_PATRONYMIC, "%" + query + "%")
                        .query();
            } else {
                patientDataList = patientDataDao.queryBuilder().orderBy(PatientData.FIELD_LAST_NAME, true).query();

            }
        } catch (SQLException e) {
            e.printStackTrace();
            patientDataList = new ArrayList<>();
        }
        Iterable<Patient> patients = Iterables.transform(patientDataList, patientTransformer);
        return Lists.newArrayList(patients);
    }

    public Patient getPatient(long patientUUID) {
        RuntimeExceptionDao<PatientData, Long> patientDataDao = mDatabaseHelper.getRuntimeExceptionDao(PatientData.class);
        PatientData patientData = patientDataDao.queryForId(patientUUID);
        Patient patient = patientTransformer.apply(patientData);

        return patient;
    }


    public static Function<PatientData, Patient> patientTransformer = new Function<PatientData, Patient>() {
        @javax.annotation.Nullable
        @Override
        public Patient apply(PatientData input) {
            if (input == null) {
                return null;
            }
            Patient patient = new Patient();
            patient.setUUID(input.getUUID());
            patient.setLastName(input.getLastName());
            patient.setFirstName(input.getFirstName());
            patient.setPatronymic(input.getPatronymic());
            patient.setGender(Patient.Gender.fromBoolean(input.getGender()));
            patient.setBirthday(input.getDateOfBirth());
            patient.setPatientId(input.getCardNumber());
            patient.setDiagnosis(input.getDiagnosis());
            patient.setExaminations(Lists.newArrayList(Iterables.transform(input.getExaminations(), examinationTransformer)));
            return patient;
        }
    };

    public static Function<Patient, PatientData> patientTransformerReverse = new Function<Patient, PatientData>() {
        @Nullable
        @Override
        public PatientData apply(@Nullable Patient input) {
            if (input == null) {
                return null;
            }
            PatientData patientData = new PatientData();
            patientData.setUUID(input.getUUID());
            patientData.setFirstName(input.getFirstName());
            patientData.setLastName(input.getLastName());
            patientData.setPatronymic(input.getPatronymic());
            patientData.setGender(input.getGender().toBoolean());
            patientData.setDateOfBirth(input.getBirthday());
            patientData.setCardNumber(input.getPatientId());
            patientData.setDiagnosis(input.getDiagnosis());
            return patientData;
        }
    };

    public static Function<ExaminationData, Examination> examinationTransformer = new Function<ExaminationData, Examination>() {
        @Nullable
        @Override
        public Examination apply(@Nullable ExaminationData input) {
            if (input == null) {
                return null;
            }
            Examination examination = new Examination();
            examination.setUUID(input.getUUID());
            examination.setType(input.getType());
            examination.setComment(input.getComment());
            examination.setDate(input.getDate());
            examination.setSnapshots(Lists.newArrayList(Iterables.transform(input.getSnapshots(), snapshotTransformer)));

//            Iterable<Snapshot> snapshots = Iterables.transform(input.getSnapshots(), snapshotTransformer);
//            List<Snapshot> filteredSnapshots = Lists.newArrayList(snapshots);
//            examination.setSnapshots(filteredSnapshots);
            return examination;
        }
    };

    public static Function<ExaminationData, Examination> extendedExaminationTransformer = new Function<ExaminationData, Examination>() {
        @Nullable
        @Override
        public Examination apply(@Nullable ExaminationData input) {
            if (input == null) {
                return null;
            }
            Examination examination = new Examination();
            examination.setUUID(input.getUUID());
            examination.setType(input.getType());
            examination.setComment(input.getComment());
            examination.setDate(input.getDate());
            examination.setSnapshots(Lists.newArrayList(Iterables.transform(input.getSnapshots(), snapshotTransformer)));
            examination.setPatient(patientTransformer.apply(input.getPatient()));
//            Iterable<Snapshot> snapshots = Iterables.transform(input.getSnapshots(), snapshotTransformer);
//            List<Snapshot> filteredSnapshots = Lists.newArrayList(snapshots);
//            examination.setSnapshots(filteredSnapshots);
            return examination;
        }
    };

    public static Function<Examination, ExaminationData> examinationTransformerReverse = new Function<Examination, ExaminationData>() {
        @Nullable
        @Override
        public ExaminationData apply(@Nullable Examination input) {
            if (input == null) {
                return null;
            }
            ExaminationData examinationObject = new ExaminationData();
            examinationObject.setUUID(input.getUUID());
            examinationObject.setType(input.getType());
            examinationObject.setComment(input.getComment());
            examinationObject.setDate(input.getDate());
            return examinationObject;
        }
    };

    public static Function<SnapshotData, Snapshot> snapshotTransformer = new Function<SnapshotData, Snapshot>() {
        @Nullable
        @Override
        public Snapshot apply(@Nullable SnapshotData input) {
            if (input == null) {
                return null;
            }
            Snapshot snapshot = new Snapshot();
            snapshot.setUUID(input.getUUID());
            snapshot.setFilename(input.getFilename());
            snapshot.setComment(input.getComment());
            snapshot.setTimestamp(input.getTimestamp());
            snapshot.setOculus(Oculus.fromBoolean(input.getOculus()));
            return snapshot;
        }
    };

    public static Function<SnapshotData, Snapshot> extendedSnapshotTransformer = new Function<SnapshotData, Snapshot>() {
        @Nullable
        @Override
        public Snapshot apply(@Nullable SnapshotData input) {
            if (input == null) {
                return null;
            }
            Snapshot snapshot = new Snapshot();
            snapshot.setUUID(input.getUUID());
            snapshot.setFilename(input.getFilename());
            snapshot.setComment(input.getComment());
            snapshot.setTimestamp(input.getTimestamp());
            snapshot.setOculus(Oculus.fromBoolean(input.getOculus()));
            snapshot.setExamination(extendedExaminationTransformer.apply(input.getExaminationData()));
            return snapshot;
        }
    };

    public static Function<Snapshot, SnapshotData> snapshotTransformerReverse = new Function<Snapshot, SnapshotData>() {
        @Nullable
        @Override
        public SnapshotData apply(@Nullable Snapshot input) {
            if (input == null) {
                return null;
            }
            SnapshotData snapshotData = new SnapshotData();
            snapshotData.setUUID(input.getUUID());
            snapshotData.setFilename(input.getFilename());
            snapshotData.setTimestamp(input.getTimestamp());
            snapshotData.setComment(input.getComment());
            snapshotData.setOculus(input.getOculus().toBoolean());
            snapshotData.setExaminationData(examinationTransformerReverse.apply(input.getExamination()));
            return snapshotData;
        }
    };
}
