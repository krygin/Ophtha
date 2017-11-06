package ru.krygin.smart_sight.snapshot.use_cases;

import javax.inject.Inject;

import ru.krygin.smart_sight.core.Injector;
import ru.krygin.smart_sight.core.async.UseCase;
import ru.krygin.smart_sight.snapshot.db.SnapshotsRepository;
import ru.krygin.smart_sight.snapshot.model.Snapshot;

/**
 * Created by Ivan on 06.11.2017.
 */

public class GetExtendedOculusSnapshotUseCase extends UseCase<GetExtendedOculusSnapshotUseCase.RequestValues, GetExtendedOculusSnapshotUseCase.ResponseValue> {

    @Inject
    SnapshotsRepository mSnapshotsRepository;

    public GetExtendedOculusSnapshotUseCase() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    protected void executeUseCase(GetExtendedOculusSnapshotUseCase.RequestValues requestValues) {
        Snapshot snapshot = mSnapshotsRepository.getSnapshot(requestValues.getOculusSnapshotUUID(), true);
        GetExtendedOculusSnapshotUseCase.ResponseValue responseValue = new GetExtendedOculusSnapshotUseCase.ResponseValue(snapshot);
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
