package ru.krygin.ophtha;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;

import ru.krygin.ophtha.oculus.Oculus;

/**
 * Created by krygin on 17.08.17.
 */

public class FileUriProvider {

    private final Context mContext;

    public FileUriProvider(Context context) {
        mContext = context;
    }

    public Uri getNewOculusSnapshotUri(String examination, Oculus oculus) {
        File oculusSnapshot = getNewOculusSnapshotFile(examination, oculus);
        return FileProvider.getUriForFile(mContext, "ru.krygin.ophtha.fileprovider", oculusSnapshot);
    }

    public File getNewOculusSnapshotFile(String examination, Oculus oculus) {
        File oculusSnapshots = getOculusSnapshotsFile(examination, oculus);
        return new File(oculusSnapshots, String.valueOf(Long.valueOf(System.currentTimeMillis())));
    }

    public Uri getOculusSnapshotsUri(String examination, Oculus oculus) {
        return FileProvider.getUriForFile(mContext, "ru.krygin.ophtha.fileprovider", getOculusSnapshotsFile(examination, oculus));
    }

    public File getOculusSnapshotsFile(String examination, Oculus oculus) {
        File examinationsFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "examinations");
        File examinationFolder = new File(examinationsFolder, examination);
        File snapshotsFolder = new File(examinationFolder, "oculus_snapshots");
        File oculusFolder = new File(snapshotsFolder, oculus.name());
        oculusFolder.mkdirs();
        return oculusFolder;
    }
}
