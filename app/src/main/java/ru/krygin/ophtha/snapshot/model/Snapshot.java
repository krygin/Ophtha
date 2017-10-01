package ru.krygin.ophtha.snapshot.model;

import android.net.Uri;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

import ru.krygin.ophtha.oculus.Oculus;

/**
 * Created by krygin on 09.09.17.
 */

public class Snapshot {

    private long UUID;
    private Date timestamp;
    private Oculus oculus;
    private String filename;
    private String comment;

    public Snapshot() {

    }

    public String getFilename() {
        return filename;
    }

    public String getComment() {
        return comment;
    }

    public long getUUID() {
        return UUID;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Oculus getOculus() {
        return oculus;
    }

    public void setUUID(long UUID) {
        this.UUID = UUID;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setOculus(Oculus oculus) {
        this.oculus = oculus;
    }
}
