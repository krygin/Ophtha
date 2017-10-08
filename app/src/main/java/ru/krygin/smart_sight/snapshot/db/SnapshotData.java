package ru.krygin.smart_sight.snapshot.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import ru.krygin.smart_sight.examination.db.ExaminationData;

import static ru.krygin.smart_sight.snapshot.db.SnapshotData.TABLE_NAME_SNAPSHOTS;

/**
 * Created by Ivan on 01.10.2017.
 */

@DatabaseTable(tableName = TABLE_NAME_SNAPSHOTS)
public class SnapshotData {

    public static final String TABLE_NAME_SNAPSHOTS = "snapshots";

    public static final String FIELD_ID = "id";
    public static final String FIELD_FILENAME = "filename";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_OCULUS = "oculus";
    public static final String FIELD_COMMENT = "comment";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private long UUID;

    @DatabaseField(columnName = FIELD_FILENAME)
    private String filename;

    @DatabaseField(columnName = FIELD_DATE)
    private Date timestamp;

    @DatabaseField(columnName = FIELD_OCULUS)
    private Boolean oculus; // true - dexter, false - sinister

    @DatabaseField(columnName = FIELD_COMMENT)
    private String comment;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private ExaminationData examination;

    public long getUUID() {
        return UUID;
    }

    public void setUUID(long UUID) {
        this.UUID = UUID;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getOculus() {
        return oculus;
    }

    public void setOculus(Boolean oculus) {
        this.oculus = oculus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ExaminationData getExaminationData() {
        return examination;
    }

    public void setExaminationData(ExaminationData examinationData) {
        this.examination = examinationData;
    }
}
