package ru.krygin.ophtha;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import ru.krygin.ophtha.core.Injector;
import ru.krygin.ophtha.examination.db.ExaminationData;
import ru.krygin.ophtha.examination.model.Examination;
import ru.krygin.ophtha.patients.db.PatientData;
import ru.krygin.ophtha.patients.model.Patient;
import ru.krygin.ophtha.snapshot.db.SnapshotData;
import ru.krygin.ophtha.snapshot.model.Snapshot;

/**
 * Created by krygin on 02.08.17.
 */

public class OphthaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        OphthaAppComponent appComponent = DaggerOphthaAppComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .asyncModule(new AsyncModule())
                .build();
        Injector.setAppComponent(appComponent);
    }
}
