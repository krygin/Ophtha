package ru.krygin.ophtha.examination.db;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by krygin on 09.09.17.
 */

public class ExaminationObject extends RealmObject {

    @PrimaryKey
    private long UUID;

    private String title;
    private Date date;
    private RealmList<SnapshotObject> snapshots = new RealmList<>();
    private String comment;
    private String diagnosis;


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RealmList<SnapshotObject> getSnapshots() {
        return snapshots;
    }

    public void setUUID(long UUID) {
        this.UUID = UUID;
    }


    public long getUUID() {
        return UUID;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDiagnosis() {
        return diagnosis;
    }
}
