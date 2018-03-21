package ru.krygin.smart_sight.snapshot.use_cases

import java.io.FileNotFoundException

import javax.inject.Inject

import ru.krygin.smart_sight.SmartSightFileManager
import ru.krygin.smart_sight.core.Injector
import ru.krygin.smart_sight.core.async.UseCase
import ru.krygin.smart_sight.snapshot.db.SnapshotsRepository
import ru.krygin.smart_sight.snapshot.model.Snapshot


class SaveSnapshotUseCase : UseCase<SaveSnapshotUseCase.RequestValues, SaveSnapshotUseCase.ResponseValue>() {

    @Inject
    internal lateinit var mSnapshotsRepository: SnapshotsRepository

    @Inject
    internal lateinit var mSmartSightFileManager: SmartSightFileManager

    init {
        Injector.getAppComponent().inject(this)
    }

    override fun executeUseCase(requestValues: RequestValues) {
        try {
            mSmartSightFileManager.saveSnapshotPhotoFile(requestValues.snapshotData.filename, requestValues.data)
            mSnapshotsRepository.createOrUpdateSnapshot(requestValues.examinationUUID, requestValues.snapshotData)
            useCaseCallback.onSuccess(ResponseValue())
        } catch (e: FileNotFoundException) {
            useCaseCallback.onError()
        }

    }

    class RequestValues(val examinationUUID: Long, val snapshotData: Snapshot, val data: ByteArray) : UseCase.RequestValues

    class ResponseValue : UseCase.ResponseValue
}
