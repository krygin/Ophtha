package ru.krygin.smart_sight.snapshot.model;

import java.util.Date;

import javax.annotation.Nullable;

import ru.krygin.smart_sight.examination.model.Examination;
import ru.krygin.smart_sight.oculus.Oculus;

/**
 * Created by krygin on 09.09.17.
 */

public class Snapshot {

    private long UUID;
    private Date timestamp;
    private Oculus oculus;
    private String filename;
    private String comment;

    @Nullable
    private Examination examination;

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

    public void setExamination(@Nullable Examination examination) {
        this.examination = examination;
    }

    @Nullable
    public Examination getExamination() {
        return examination;
    }
}
