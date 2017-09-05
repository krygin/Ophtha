package ru.krygin.ophtha.comparation;

import ru.krygin.ophtha.examination.GetExaminationsUseCase;

/**
 * Created by krygin on 06.08.17.
 */

public interface OnOculusSnapshotPreviewClickListener {
    void onOculusSnapshotPreviewClick(GetExaminationsUseCase.Snapshot snapshot);
}
