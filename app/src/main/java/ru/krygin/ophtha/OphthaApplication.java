package ru.krygin.ophtha;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.examination.db.ExaminationObject;
import ru.krygin.ophtha.examination.db.SnapshotObject;
import ru.krygin.ophtha.patients.db.PatientObject;
import ru.krygin.ophtha.patients.model.Patient;

/**
 * Created by krygin on 02.08.17.
 */

public class OphthaApplication extends Application {

    private AtomicLong UUIDGenerator = new AtomicLong();

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default

        Calendar calendar = Calendar.getInstance();
        calendar.set(1993, 3, 28);

        PatientObject patientObject = new PatientObject();
        patientObject.setUUID(UUIDGenerator.incrementAndGet());
        patientObject.setFirstName("Крыгин");
        patientObject.setLastName("Иван");
        patientObject.setPatronymic("Александрович");
        patientObject.setGender(Patient.Gender.M.toBoolean());
        patientObject.setPatientId("123123123");
        patientObject.setBirthday(calendar.getTime());

        SnapshotObject snapshotObject = new SnapshotObject();
        snapshotObject.setUUID(UUIDGenerator.incrementAndGet());
        snapshotObject.setFilename("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg");
        snapshotObject.setTimestamp(new Date(System.currentTimeMillis()));
        snapshotObject.setComment("Левый глаз 0");
        snapshotObject.setOculus(true);

        SnapshotObject snapshotObject1 = new SnapshotObject();
        snapshotObject1.setUUID(UUIDGenerator.incrementAndGet());
        snapshotObject1.setFilename("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg");
        snapshotObject1.setTimestamp(new Date(System.currentTimeMillis()));
        snapshotObject1.setComment("Левый глаз 1");
        snapshotObject1.setOculus(true);

        SnapshotObject snapshotObject2 = new SnapshotObject();
        snapshotObject2.setUUID(UUIDGenerator.incrementAndGet());
        snapshotObject2.setFilename("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg");
        snapshotObject2.setTimestamp(new Date(System.currentTimeMillis()));
        snapshotObject2.setComment("Левый глаз 2");
        snapshotObject2.setOculus(true);

        SnapshotObject snapshotObject3 = new SnapshotObject();
        snapshotObject3.setUUID(UUIDGenerator.incrementAndGet());
        snapshotObject3.setFilename("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg");
        snapshotObject3.setTimestamp(new Date(System.currentTimeMillis()));
        snapshotObject3.setComment("Правый глаз 3");
        snapshotObject3.setOculus(false);

        ExaminationObject examinationObject = new ExaminationObject();
        examinationObject.setUUID(UUIDGenerator.incrementAndGet());
        examinationObject.setTitle("Title 1");
        examinationObject.setComment("Коммент к исследованию");
        examinationObject.setDiagnosis("Миопия");
        examinationObject.setDate(new Date(System.currentTimeMillis()));

        examinationObject.getSnapshots().add(snapshotObject);
        examinationObject.getSnapshots().add(snapshotObject1);
        examinationObject.getSnapshots().add(snapshotObject2);
        examinationObject.getSnapshots().add(snapshotObject3);



        SnapshotObject snapshotObject4 = new SnapshotObject();
        snapshotObject4.setUUID(UUIDGenerator.incrementAndGet());
        snapshotObject4.setFilename("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg");
        snapshotObject4.setTimestamp(new Date(System.currentTimeMillis()));
        snapshotObject4.setComment("Левый глаз 4");
        snapshotObject4.setOculus(true);

        SnapshotObject snapshotObject5 = new SnapshotObject();
        snapshotObject5.setUUID(UUIDGenerator.incrementAndGet());
        snapshotObject5.setFilename("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg");
        snapshotObject5.setTimestamp(new Date(System.currentTimeMillis()));
        snapshotObject5.setComment("Правый глаз 5");
        snapshotObject5.setOculus(true);

        SnapshotObject snapshotObject6 = new SnapshotObject();
        snapshotObject6.setUUID(UUIDGenerator.incrementAndGet());
        snapshotObject6.setFilename("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg");
        snapshotObject6.setTimestamp(new Date(System.currentTimeMillis()));
        snapshotObject6.setComment("Правый глаз 6");
        snapshotObject6.setOculus(false);

        SnapshotObject snapshotObject7 = new SnapshotObject();
        snapshotObject7.setUUID(UUIDGenerator.incrementAndGet());
        snapshotObject7.setFilename("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg");
        snapshotObject7.setTimestamp(new Date(System.currentTimeMillis()));
        snapshotObject7.setComment("Правый глаз 7");
        snapshotObject7.setOculus(false);


        ExaminationObject examinationObject1 = new ExaminationObject();
        examinationObject1.setUUID(UUIDGenerator.incrementAndGet());
        examinationObject1.setTitle("Title 2");
        examinationObject1.setComment("Коммент к исследованию 2");
        examinationObject1.setDiagnosis("Катаракта");
        examinationObject1.setDate(new Date(System.currentTimeMillis()));

        examinationObject1.getSnapshots().add(snapshotObject4);
        examinationObject1.getSnapshots().add(snapshotObject5);
        examinationObject1.getSnapshots().add(snapshotObject6);
        examinationObject1.getSnapshots().add(snapshotObject7);


        patientObject.getExaminations().add(examinationObject);
        patientObject.getExaminations().add(examinationObject1);

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        realm.insertOrUpdate(patientObject);
        realm.commitTransaction();
        realm.close();

        OphthaAppComponent appComponent = DaggerOphthaAppComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .asyncModule(new AsyncModule())
                .build();
        Injector.setAppComponent(appComponent);
    }
}
