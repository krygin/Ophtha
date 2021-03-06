package ru.krygin.smart_sight;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;

import ru.krygin.smart_sight.camera.TakePhotoActivity;
import ru.krygin.smart_sight.oculus.Oculus;

/**
 * Created by krygin on 17.08.17.
 */

public class FileUriProvider {

    private final Context mContext;
    private final SmartSightFileManager mFileManager;

    public FileUriProvider(Context context, SmartSightFileManager fileManager) {
        mContext = context;
        mFileManager = fileManager;
    }

    public Uri getNewOculusSnapshotUri(String examination, Oculus oculus) {
        File oculusSnapshot = getNewOculusSnapshotFile(examination, oculus);
        return FileProvider.getUriForFile(mContext, "ru.krygin.smart_sight.fileprovider", oculusSnapshot);
    }

    public File getNewOculusSnapshotFile(String examination, Oculus oculus) {
        File oculusSnapshots = getOculusSnapshotsFile(examination, oculus);
        return new File(oculusSnapshots, String.valueOf(Long.valueOf(System.currentTimeMillis())));
    }

    public Uri getUriForSnapshotFilename(String filename) {
        File snapshotFile = mFileManager.getSnapshotFile(filename);
        return FileProvider.getUriForFile(mContext,
                "ru.krygin.smart_sight.fileprovider",
                snapshotFile);
    }

    public Uri getOculusSnapshotsUri(String examination, Oculus oculus) {
        return FileProvider.getUriForFile(mContext, "ru.krygin.smart_sight.fileprovider", getOculusSnapshotsFile(examination, oculus));
    }

    public File getOculusSnapshotsFile(String examination, Oculus oculus) {
        File examinationsFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "examinations");
        File examinationFolder = new File(examinationsFolder, examination);
        File snapshotsFolder = new File(examinationFolder, "smart_sight_snapshots");
        File oculusFolder = new File(snapshotsFolder, oculus.name());
        oculusFolder.mkdirs();
        return oculusFolder;
    }
}
