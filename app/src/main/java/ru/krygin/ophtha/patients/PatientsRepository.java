package ru.krygin.ophtha.patients;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.krygin.ophtha.examination.db.ExaminationObject;
import ru.krygin.ophtha.examination.db.SnapshotObject;
import ru.krygin.ophtha.examination.model.Examination;
import ru.krygin.ophtha.examination.model.Snapshot;
import ru.krygin.ophtha.patients.db.PatientObject;
import ru.krygin.ophtha.patients.model.Patient;

/**
 * Created by krygin on 20.08.17.
 */

public class PatientsRepository {

    public void createOrUpdatePatient(Patient patient) {
        PatientObject patientObject = new PatientObject();
        patientObject.setUUID(patient.getUUID());
        patientObject.setFirstName(patient.getFirstName());
        patientObject.setLastName(patient.getLastName());
        patientObject.setPatronymic(patient.getPatronymic());
        patientObject.setGender(patient.getGender().toBoolean());
        patientObject.setPatientId(patient.getPatientId());
        patientObject.setBirthday(patient.getBirthday());

        SnapshotObject snapshotObject = new SnapshotObject();
        snapshotObject.setUUID(System.currentTimeMillis());
        snapshotObject.setFilename("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg");
        snapshotObject.setTimestamp(new Date(System.currentTimeMillis()));
        snapshotObject.setComment("Новый коммент");
        snapshotObject.setOculus(true);

        ExaminationObject examinationObject = new ExaminationObject();
        examinationObject.setUUID(System.currentTimeMillis());
        examinationObject.setTitle("Title 1");
        examinationObject.setComment("Коммент к исследованию");
        examinationObject.setDiagnosis("Миопия");
        examinationObject.setDate(new Date(System.currentTimeMillis()));

        examinationObject.getSnapshots().add(snapshotObject);

        patientObject.getExaminations().add(examinationObject);

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
            examination.setDate(input.getDate());

            Iterable<Snapshot> snapshots = Iterables.transform(input.getSnapshots(), snapshotTransformer);
            List<Snapshot> filteredSnapshots = Lists.newArrayList(snapshots);
            examination.setSnapshots(filteredSnapshots);
            return examination;
        }
    };

    public static Function<SnapshotObject, Snapshot> snapshotTransformer = new Function<SnapshotObject, Snapshot>() {
        @Nullable
        @Override
        public Snapshot apply(@Nullable SnapshotObject input) {
            if (input == null) {
                return null;
            }
            Snapshot snapshot = new Snapshot(input.getUUID(), input.getFilename(), input.getComment());
            return snapshot;
        }
    };
}
