package ru.krygin.ophtha.oculus;

import io.realm.Realm;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.examination.db.SnapshotObject;
import ru.krygin.ophtha.examination.model.Snapshot;
import ru.krygin.ophtha.patients.PatientsRepository;

/**
 * Created by krygin on 13.09.17.
 */

public class GetOculusSnapshotUseCase extends UseCase<GetOculusSnapshotUseCase.RequestValues, GetOculusSnapshotUseCase.ResponseValue> {

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        Realm realm = Realm.getDefaultInstance();
        SnapshotObject snapshotObject = realm.where(SnapshotObject.class).equalTo("UUID", requestValues.getOculusSnapshotUUID()).findFirst();
        Snapshot snapshot = PatientsRepository.snapshotTransformer.apply(snapshotObject);
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
