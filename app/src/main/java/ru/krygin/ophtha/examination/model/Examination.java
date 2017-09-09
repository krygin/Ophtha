package ru.krygin.ophtha.examination.model;

import java.util.Date;
import java.util.List;

import io.realm.annotations.PrimaryKey;


/**
 * Created by krygin on 09.09.17.
 */

public class Examination {
    @PrimaryKey
    private long UUID;

    private List<Snapshot> mSnapshots;
    private String mTitle;
    private Date mDate;


    public String getTitle() {
        return mTitle;
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
        mTitle = title;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
