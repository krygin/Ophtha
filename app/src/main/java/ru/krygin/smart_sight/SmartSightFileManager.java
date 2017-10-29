package ru.krygin.smart_sight;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Ivan on 29.10.2017.
 */

public class SmartSightFileManager {
    private final File mRoot;

    public SmartSightFileManager(File root) {
        mRoot = root;
    }

    public void saveSnapshotPhotoFile(String filename, byte[] data) throws FileNotFoundException {
        if (!mRoot.exists()) {
            mRoot.mkdirs();
        }
        File photoFile = getSnapshotFile(filename);
        try (FileOutputStream fileOutputStream = new FileOutputStream(photoFile)) {
            fileOutputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getSnapshotFile(String filename) {
        return new File(mRoot, filename);
    }

    public void removeSnapshotPhotoFile(String filename) {
        File photoFile = getSnapshotFile(filename);
        photoFile.delete();
    }
}
