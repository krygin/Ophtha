package ru.krygin.ophtha.patients;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.krygin.ophtha.examination.db.ExaminationObject;
import ru.krygin.ophtha.examination.db.SnapshotObject;
import ru.krygin.ophtha.examination.model.Examination;
import ru.krygin.ophtha.examination.model.Snapshot;
import ru.krygin.ophtha.oculus.Oculus;
import ru.krygin.ophtha.patients.db.PatientObject;
import ru.krygin.ophtha.patients.model.Patient;

/**
 * Created by krygin on 20.08.17.
 */

public class PatientsRepository {

    public void createOrUpdatePatient(Patient patient) {
        PatientObject patientObject = patientTransformerReverse.apply(patient);

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        realm.insertOrUpdate(patientObject);
        realm.commitTransaction();
        realm.close();
    }


    public List<Patient> getPatients() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PatientObject> realmResults = realm.where(PatientObject.class).findAll();
        Iterable<Patient> patients = Iterables.transform(realmResults, patientTransformer);
        List<Patient> filteredPatients = Lists.newArrayList(patients);
        realm.close();
        return filteredPatients;
    }

    public Patient getPatient(long patientUUID) {
        Realm realm = Realm.getDefaultInstance();
        PatientObject patientObject = realm.where(PatientObject.class).equalTo(PatientObject.UUID_FIELD, patientUUID).findFirst();
        Patient patient = patientTransformer.apply(patientObject);
        realm.close();
        return patient;
    }


    public static Function<PatientObject, Patient> patientTransformer = new Function<PatientObject, Patient>() {
        @javax.annotation.Nullable
        @Override
        public Patient apply(@javax.annotation.Nullable PatientObject input) {
            if (input == null) {
                return null;
            }
            Patient patient = new Patient();
            patient.setUUID(input.getUUID());
            patient.setLastName(input.getLastName());
            patient.setFirstName(input.getFirstName());
            patient.setPatronymic(input.getPatronymic());
            patient.setGender(Patient.Gender.fromBoolean(input.getGender()));
            patient.setPatientId(input.getPatientId());
            patient.setBirthday(input.getBirthday());

            Iterable<Examination> examinations = Iterables.transform(input.getExaminations(), examinationTransformer);
            List<Examination> filteredExaminations = Lists.newArrayList(examinations);
            patient.setExaminations(filteredExaminations);
            return patient;
        }
    };

    public static Function<Patient, PatientObject> patientTransformerReverse = new Function<Patient, PatientObject>() {
        @Nullable
        @Override
        public PatientObject apply(@Nullable Patient input) {
            if (input == null) {
                return null;
            }
            PatientObject patientObject = new PatientObject();
            patientObject.setUUID(input.getUUID());
            patientObject.setFirstName(input.getFirstName());
            patientObject.setLastName(input.getLastName());
            patientObject.setPatronymic(input.getPatronymic());
            patientObject.setGender(input.getGender().toBoolean());
            patientObject.setPatientId(input.getPatientId());
            patientObject.setBirthday(input.getBirthday());

            Iterable<ExaminationObject> examinationObjects = Iterables.transform(input.getExaminations(), examinationTransformerReverse);
            List<ExaminationObject> filteredExaminationObjects = Lists.newArrayList(examinationObjects);
            patientObject.getExaminations().addAll(filteredExaminationObjects);
            return patientObject;
        }
    };

    public static Function<ExaminationObject, Examination> examinationTransformer = new Function<ExaminationObject, Examination>() {
        @Nullable
        @Override
        public Examination apply(@Nullable ExaminationObject input) {
            if (input == null) {
                return null;
            }
            Examination examination = new Examination();
            examination.setUUID(input.getUUID());
            examination.setTitle(input.getTitle());
            examination.setComment(input.getComment());
            examination.setDiagnosis(input.getDiagnosis());
            examination.setDate(input.getDate());

            Iterable<Snapshot> snapshots = Iterables.transform(input.getSnapshots(), snapshotTransformer);
            List<Snapshot> filteredSnapshots = Lists.newArrayList(snapshots);
            examination.setSnapshots(filteredSnapshots);
            return examination;
        }
    };

    public static Function<Examination, ExaminationObject> examinationTransformerReverse = new Function<Examination, ExaminationObject>() {
        @Nullable
        @Override
        public ExaminationObject apply(@Nullable Examination input) {
            if (input == null) {
                return null;
            }
            ExaminationObject examinationObject = new ExaminationObject();
            examinationObject.setUUID(input.getUUID());
            examinationObject.setTitle(input.getTitle());
            examinationObject.setComment(input.getComment());
            examinationObject.setDiagnosis(input.getDiagnosis());
            examinationObject.setDate(input.getDate());
            Iterable<SnapshotObject> snapshotObjects = Iterables.transform(input.getSnapshots(), snapshotTransformerReverse);
            List<SnapshotObject> filteredSnapshotObjects = Lists.newArrayList(snapshotObjects);
            examinationObject.getSnapshots().addAll(filteredSnapshotObjects);
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
