package ru.krygin.smart_sight.snapshot.use_cases;

import java.io.File;
import java.io.FileNotFoundException;

import javax.inject.Inject;

import ru.krygin.smart_sight.SmartSightFileManager;
import ru.krygin.smart_sight.core.Injector;
import ru.krygin.smart_sight.core.async.UseCase;
import ru.krygin.smart_sight.snapshot.db.SnapshotsRepository;
import ru.krygin.smart_sight.snapshot.model.Snapshot;

/**
 * Created by Ivan on 01.10.2017.
 */

public class SaveSnapshotUseCase extends UseCase<SaveSnapshotUseCase.RequestValues, SaveSnapshotUseCase.ResponseValue> {

    @Inject
    SnapshotsRepository mSnapshotsRepository;

    @Inject
    SmartSightFileManager mSmartSightFileManager;

    public SaveSnapshotUseCase() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        try {
            mSmartSightFileManager.saveSnapshotPhotoFile(requestValues.getSnapshotData().getFilename(), requestValues.getData());
            mSnapshotsRepository.createOrUpdateSnapshot(requestValues.getExaminationUUID(), requestValues.getSnapshotData());
            getUseCaseCallback().onSuccess(new ResponseValue());
        } catch (FileNotFoundException e) {
            getUseCaseCallback().onError();
        }
    }

    public static class RequestValues implements UseCase.RequestValues {

        private final long mExaminationUUID;
        private final Snapshot mSnapshot;
        private byte[] mData;

        public RequestValues(long examinationUUID, Snapshot snapshot, byte[] data) {
            mExaminationUUID = examinationUUID;
            mSnapshot = snapshot;
            mData = data;
        }

        public long getExaminationUUID() {
            return mExaminationUUID;
        }

        public Snapshot getSnapshotData() {
            return mSnapshot;
        }

        public byte[] getData() {
            return mData;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {

    }
}
