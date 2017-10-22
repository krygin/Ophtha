package ru.krygin.smart_sight.examination.model;

import java.util.Date;
import java.util.List;

import ru.krygin.smart_sight.snapshot.model.Snapshot;


/**
 * Created by krygin on 09.09.17.
 */

public class Examination {

    private long UUID;

    private List<Snapshot> mSnapshots;
    private int mType;
    private Date mDate;
    private String mComment;


    public int getType() {
        return mType;
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

    public void setType(int title) {
        this.mType = title;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        this.mComment = comment;
    }

    public enum Type {
        UNDEFINDED("Тип исследования"),
        OCULAR_FUNDUS("Фоторегистрация глазного дна"),
        SEGMENT_ANTERIOR("Фоторегистрация переднего отрезка");

        private final String mExaminationType;

        Type(String examinationType) {
            mExaminationType = examinationType;
        }

        @Override
        public String toString() {
            return mExaminationType;
        }
    }

}
