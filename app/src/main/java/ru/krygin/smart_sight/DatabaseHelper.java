package ru.krygin.smart_sight;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ru.krygin.smart_sight.examination.db.ExaminationData;
import ru.krygin.smart_sight.patients.db.PatientData;
import ru.krygin.smart_sight.snapshot.db.SnapshotData;


/**
 * Created by Ivan on 01.10.2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static int DATABASE_VERSION = 3;
    private static String DATABASE_NAME = "ophtha_database.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, PatientData.class);
            TableUtils.createTable(connectionSource, ExaminationData.class);
            TableUtils.createTable(connectionSource, SnapshotData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, SnapshotData.class, true);
            TableUtils.dropTable(connectionSource, ExaminationData.class, true);
            TableUtils.dropTable(connectionSource, PatientData.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
