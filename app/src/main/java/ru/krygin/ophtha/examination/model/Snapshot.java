package ru.krygin.ophtha.examination.model;

import android.net.Uri;

/**
 * Created by krygin on 09.09.17.
 */

public class Snapshot {
    private final String mFilename;
    private final String mComment;
    private final long mUUID;

    public Snapshot(long UUID, String filename, String comment) {
        mUUID = UUID;
        mFilename = filename;
        mComment = comment;
    }

    public String getSnapshotUri() {
        return mFilename;
    }

    public String getComment() {
        return mComment;
    }
}
