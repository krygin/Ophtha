package ru.krygin.ophtha.patients.db;

import android.content.Context;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

import javax.annotation.Nullable;

import ru.krygin.ophtha.DatabaseHelper;
import ru.krygin.ophtha.examination.db.ExaminationData;
import ru.krygin.ophtha.examination.db.SnapshotObject;
import ru.krygin.ophtha.examination.model.Examination;
import ru.krygin.ophtha.examination.model.Snapshot;
import ru.krygin.ophtha.oculus.Oculus;
import ru.krygin.ophtha.patients.model.Patient;

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
        RuntimeExceptionDao<PatientData, Long> patientDataDao = mDatabaseHelper.getRuntimeExceptionDao(PatientData.class);
        List<PatientData> patientDataList = patientDataDao.queryForAll();
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
//            patient.setExaminations(Lists.newArrayList(Iterables.transform(input.getExaminations(), examinationTransformer)));
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
            PatientData patientObject = new PatientData();
            patientObject.setUUID(input.getUUID());
            patientObject.setFirstName(input.getFirstName());
            patientObject.setLastName(input.getLastName());
            patientObject.setPatronymic(input.getPatronymic());
            patientObject.setGender(input.getGender().toBoolean());
            patientObject.setDateOfBirth(input.getBirthday());
            return patientObject;
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
            examination.setTitle(input.getTitle());
            examination.setComment(input.getComment());
            examination.setDiagnosis(input.getDiagnosis());
            examination.setDate(input.getDate());

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
            examinationObject.setTitle(input.getTitle());
            examinationObject.setComment(input.getComment());
            examinationObject.setDiagnosis(input.getDiagnosis());
            examinationObject.setDate(input.getDate());
            return examinationObject;
        }
    };

    public static Function<SnapshotObject, Snapshot> snapshotTransformer = new Function<SnapshotObject, Snapshot>() {
        @Nullable
        @Override
        public Snapshot apply(@Nullable SnapshotObject input) {
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

    public static Function<Snapshot, SnapshotObject> snapshotTransformerReverse = new Function<Snapshot, SnapshotObject>() {
        @Nullable
        @Override
        public SnapshotObject apply(@Nullable Snapshot input) {
            if (input == null) {
                return null;
            }
            SnapshotObject snapshotObject = new SnapshotObject();
            snapshotObject.setUUID(input.getUUID());
            snapshotObject.setFilename(input.getFilename());
            snapshotObject.setTimestamp(input.getTimestamp());
            snapshotObject.setComment(input.getComment());
            snapshotObject.setOculus(input.getOculus().toBoolean());
            return snapshotObject;
        }
    };
}
