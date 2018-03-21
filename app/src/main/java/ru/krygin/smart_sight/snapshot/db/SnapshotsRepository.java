package ru.krygin.smart_sight.snapshot.db;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.jetbrains.annotations.NotNull;

import ru.krygin.smart_sight.DatabaseHelper;
import ru.krygin.smart_sight.examination.db.ExaminationData;
import ru.krygin.smart_sight.snapshot.model.Snapshot;

import static ru.krygin.smart_sight.patients.db.PatientsRepository.extendedSnapshotTransformer;
import static ru.krygin.smart_sight.patients.db.PatientsRepository.snapshotTransformer;
import static ru.krygin.smart_sight.patients.db.PatientsRepository.snapshotTransformerReverse;

/**
 * Created by Ivan on 01.10.2017.
 */

public class SnapshotsRepository {
    private final DatabaseHelper mDatabaseHelper;

    public SnapshotsRepository(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public Snapshot getSnapshot(long oculusSnapshotUUID, boolean withRelatedExamination) {
        RuntimeExceptionDao<SnapshotData, Long> snapshotsDao = mDatabaseHelper.getRuntimeExceptionDao(SnapshotData.class);
        SnapshotData snapshotData = snapshotsDao.queryForId(oculusSnapshotUUID);
        snapshotData.getExaminationData().getPatient();
        Snapshot snapshot = withRelatedExamination ? extendedSnapshotTransformer.apply(snapshotData) : snapshotTransformer.apply(snapshotData);
        return snapshot;
    }

    public Snapshot getSnapshot(long oculusSnapshotUUID) {
        return getSnapshot(oculusSnapshotUUID, false);
    }

    public void createOrUpdateSnapshot(long examinationUUID, Snapshot snapshot) {
        RuntimeExceptionDao<SnapshotData, Long> snapshotDataDao = mDatabaseHelper.getRuntimeExceptionDao(SnapshotData.class);
        SnapshotData snapshotData = snapshotTransformerReverse.apply(snapshot);
        ExaminationData examinationData = new ExaminationData();
        examinationData.setUUID(examinationUUID);
        snapshotData.setExaminationData(examinationData);
        snapshotDataDao.createOrUpdate(snapshotData);
    }

    public void removeSnapshot(Snapshot snapshot) {
        SnapshotData snapshotData = snapshotTransformerReverse.apply(snapshot);
        mDatabaseHelper.getRuntimeExceptionDao(SnapshotData.class).delete(snapshotData);
    }

    public void updateSnapshot(@NotNull Snapshot snapshot) {
        SnapshotData snapshotData = snapshotTransformerReverse.apply(snapshot);
        int updated = mDatabaseHelper.getRuntimeExceptionDao(SnapshotData.class).update(snapshotData);
        new Integer(updated).toString();
    }
}
