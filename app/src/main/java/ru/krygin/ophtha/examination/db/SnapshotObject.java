package ru.krygin.ophtha.examination.db;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by krygin on 09.09.17.
 */

public class SnapshotObject extends RealmObject {

    @PrimaryKey
    private long UUID;

    private String filename;
    private Date timestamp;
    private boolean oculus; // true - dexter, false - sinister
    private String comment;

    public void setUUID(long UUID) {
        this.UUID = UUID;
    }

    public long getUUID() {
        return UUID;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setOculus(boolean oculus) {
        this.oculus = oculus;
    }

    public boolean getOculus() {
        return oculus;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
