package ru.krygin.smart_sight.snapshot.use_cases

import ru.krygin.smart_sight.core.Injector
import ru.krygin.smart_sight.core.async.UseCase
import ru.krygin.smart_sight.snapshot.db.SnapshotsRepository
import ru.krygin.smart_sight.snapshot.model.Snapshot
import javax.inject.Inject


class UpdateSnapshotUseCase : UseCase<UpdateSnapshotUseCase.RequestValues, UpdateSnapshotUseCase.ResponseValue>() {

    @Inject
    internal lateinit var snapshotsRepository: SnapshotsRepository

    init {
        Injector.getAppComponent().inject(this)
    }

    override fun executeUseCase(requestValues: RequestValues) {
        snapshotsRepository.updateSnapshot(requestValues.snapshot)
        useCaseCallback.onSuccess(ResponseValue())
    }

    class RequestValues(val snapshot: Snapshot): UseCase.RequestValues

    class ResponseValue: UseCase.ResponseValue
}