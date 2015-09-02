package jp.co.sprix.singleormlitesqliteconnection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "singleormlitesqliteconnection.db";

    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(final Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db, final ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, TestData.class);

        } catch (SQLException sqle) {
            Log.e(getClass().getName(), "Can't create database", sqle);
        }
    }

    @Override
    public void onUpgrade(
            final SQLiteDatabase db,
            final ConnectionSource connectionSource,
            final int oldVersion,
            final int newVersion) {

        if (newVersion > oldVersion) {

            try {
                Log.d(Constant.LOG_TAG, getClass().getName() + " onUpgrade ");

                TableUtils.dropTable(connectionSource, TestData.class, true);

                onCreate(db, connectionSource);

            } catch (SQLException sqle) {
                Log.d(Constant.LOG_TAG, getClass().getName() + " Can't drop databases " + sqle);

                throw new RuntimeException(sqle);
            }
        }
    }




}
