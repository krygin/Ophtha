package ru.krygin.smart_sight.examination.db;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import ru.krygin.smart_sight.patients.db.PatientData;
import ru.krygin.smart_sight.snapshot.db.SnapshotData;

import static ru.krygin.smart_sight.examination.db.ExaminationData.TABLE_NAME_EXAMINATIONS;

/**
 * Created by Ivan on 01.10.2017.
 */

@DatabaseTable(tableName = TABLE_NAME_EXAMINATIONS)
public class ExaminationData {
    public static final String TABLE_NAME_EXAMINATIONS = "examinations";

    public static final String FIELD_ID = "id";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_COMMENT = "comment";

    @DatabaseField(columnName = FIELD_ID, id = true)
    private long UUID;

    @DatabaseField(columnName = FIELD_TYPE)
    private int type;

    @DatabaseField(columnName = FIELD_DATE)
    private Date date;

    @DatabaseField(columnName = FIELD_COMMENT)
    private String comment;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private PatientData patient;

    @ForeignCollectionField(eager = true)
    public ForeignCollection<SnapshotData> snapshots;

    public long getUUID() {
        return UUID;
    }

    public void setUUID(long UUID) {
        this.UUID = UUID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PatientData getPatient() {
        return patient;
    }

    public void setPatient(PatientData patient) {
        this.patient = patient;
    }

    public ForeignCollection<SnapshotData> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(ForeignCollection<SnapshotData> snapshots) {
        this.snapshots = snapshots;
    }
}
