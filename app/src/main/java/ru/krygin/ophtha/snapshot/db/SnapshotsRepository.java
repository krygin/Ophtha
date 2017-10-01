package ru.krygin.ophtha.snapshot.db;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import ru.krygin.ophtha.DatabaseHelper;
import ru.krygin.ophtha.examination.db.ExaminationData;
import ru.krygin.ophtha.examination.model.Examination;
import ru.krygin.ophtha.patients.db.PatientData;
import ru.krygin.ophtha.snapshot.model.Snapshot;

import static ru.krygin.ophtha.patients.db.PatientsRepository.examinationTransformer;
import static ru.krygin.ophtha.patients.db.PatientsRepository.examinationTransformerReverse;
import static ru.krygin.ophtha.patients.db.PatientsRepository.snapshotTransformer;
import static ru.krygin.ophtha.patients.db.PatientsRepository.snapshotTransformerReverse;

/**
 * Created by Ivan on 01.10.2017.
 */

public class SnapshotsRepository {
    private final DatabaseHelper mDatabaseHelper;

    public SnapshotsRepository(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public Snapshot getSnapshot(long oculusSnapshotUUID) {
        RuntimeExceptionDao<SnapshotData, Long> snapshotsDao = mDatabaseHelper.getRuntimeExceptionDao(SnapshotData.class);
        SnapshotData snapshotData = snapshotsDao.queryForId(oculusSnapshotUUID);
        Snapshot snapshot = snapshotTransformer.apply(snapshotData);
        return snapshot;
    }

    public void createOrUpdateSnapshot(long examinationUUID, Snapshot snapshot) {
        RuntimeExceptionDao<SnapshotData, Long> snapshotDataDao = mDatabaseHelper.getRuntimeExceptionDao(SnapshotData.class);
        SnapshotData snapshotData = snapshotTransformerReverse.apply(snapshot);
        ExaminationData examinationData = new ExaminationData();
        examinationData.setUUID(examinationUUID);
        snapshotData.setExaminationData(examinationData);
        snapshotDataDao.createOrUpdate(snapshotData);
    }
}
