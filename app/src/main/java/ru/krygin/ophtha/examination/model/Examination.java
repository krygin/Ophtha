package ru.krygin.ophtha.examination.model;

import java.util.Date;
import java.util.List;

import ru.krygin.ophtha.snapshot.model.Snapshot;


/**
 * Created by krygin on 09.09.17.
 */

public class Examination {

    private long UUID;

    private List<Snapshot> mSnapshots;
    private String title;
    private Date mDate;
    private String comment;
    private String diagnosis;


    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return mDate;
    }

    public List<Snapshot> getSnapshots() {
        return mSnapshots;
    }

    public void setSnapshots(List<Snapshot> snapshots) {
        mSnapshots = snapshots;
    }

    public long getUUID() {
        return UUID;
    }

    public void setUUID(long UUID) {
        this.UUID = UUID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getComment() {
        return comment;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
}
