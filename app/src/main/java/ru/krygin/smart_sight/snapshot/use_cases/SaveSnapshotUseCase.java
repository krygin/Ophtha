package ru.krygin.smart_sight.snapshot.use_cases;

import javax.inject.Inject;

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

    public SaveSnapshotUseCase() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mSnapshotsRepository.createOrUpdateSnapshot(requestValues.getExaminationUUID(), requestValues.getSnapshotData());
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static class RequestValues implements UseCase.RequestValues {

        private final long mExaminationUUID;
        private final Snapshot mSnapshot;

        public RequestValues(long examinationUUID, Snapshot snapshot) {
            mExaminationUUID = examinationUUID;
            mSnapshot = snapshot;
        }

        public long getExaminationUUID() {
            return mExaminationUUID;
        }

        public Snapshot getSnapshotData() {
            return mSnapshot;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {

    }
}