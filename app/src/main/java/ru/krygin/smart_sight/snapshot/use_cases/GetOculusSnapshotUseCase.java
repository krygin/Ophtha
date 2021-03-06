package ru.krygin.smart_sight.snapshot.use_cases;

import javax.inject.Inject;

import ru.krygin.smart_sight.core.Injector;
import ru.krygin.smart_sight.core.async.UseCase;
import ru.krygin.smart_sight.snapshot.db.SnapshotsRepository;
import ru.krygin.smart_sight.snapshot.model.Snapshot;

/**
 * Created by krygin on 13.09.17.
 */

public class GetOculusSnapshotUseCase extends UseCase<GetOculusSnapshotUseCase.RequestValues, GetOculusSnapshotUseCase.ResponseValue> {

    @Inject
    SnapshotsRepository mSnapshotsRepository;

    public GetOculusSnapshotUseCase() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        Snapshot snapshot = mSnapshotsRepository.getSnapshot(requestValues.getOculusSnapshotUUID());
        ResponseValue responseValue = new ResponseValue(snapshot);
        getUseCaseCallback().onSuccess(responseValue);
    }

    public static class RequestValues implements UseCase.RequestValues {

        private final long mOculusSnapshotUUID;

        public RequestValues(long oculusSnapshotUUID) {
            mOculusSnapshotUUID = oculusSnapshotUUID;
        }

        public long getOculusSnapshotUUID() {
            return mOculusSnapshotUUID;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {

        private final Snapshot mSnapshot;

        public ResponseValue(Snapshot snapshot) {
            mSnapshot = snapshot;
        }

        public Snapshot getSnapshot() {
            return mSnapshot;
        }
    }
}
