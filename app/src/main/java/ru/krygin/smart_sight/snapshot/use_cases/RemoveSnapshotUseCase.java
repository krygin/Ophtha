package ru.krygin.smart_sight.snapshot.use_cases;

import javax.inject.Inject;

import ru.krygin.smart_sight.SmartSightFileManager;
import ru.krygin.smart_sight.core.Injector;
import ru.krygin.smart_sight.core.async.UseCase;
import ru.krygin.smart_sight.snapshot.db.SnapshotsRepository;
import ru.krygin.smart_sight.snapshot.model.Snapshot;

/**
 * Created by Ivan on 29.10.2017.
 */

public class RemoveSnapshotUseCase extends UseCase<RemoveSnapshotUseCase.RequestValues, RemoveSnapshotUseCase.ResponseValue> {

    @Inject
    SnapshotsRepository snapshotsRepository;

    @Inject
    SmartSightFileManager fileManager;

    public RemoveSnapshotUseCase() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        fileManager.removeSnapshotPhotoFile(requestValues.getSnapshot().getFilename());
        snapshotsRepository.removeSnapshot(requestValues.getSnapshot());
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static class RequestValues implements UseCase.RequestValues {

        private final Snapshot mSnapshot;

        public RequestValues(Snapshot snapshot) {
            mSnapshot = snapshot;
        }

        public Snapshot getSnapshot() {
            return mSnapshot;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {

    }
}
