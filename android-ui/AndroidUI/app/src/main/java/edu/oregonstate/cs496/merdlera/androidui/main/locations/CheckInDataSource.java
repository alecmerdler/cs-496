package edu.oregonstate.cs496.merdlera.androidui.main.locations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alec on 8/4/17.
 */

public class CheckInDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_COMMENT };

    public CheckInDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public CheckIn createCheckIn(CheckIn checkIn) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_COMMENT, checkIn.getComment());
        long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                                       allColumns,
                                       MySQLiteHelper.COLUMN_ID + " = " + insertId,
                                       null, null, null, null);
        cursor.moveToFirst();
        CheckIn newCheckIn = cursorToCheckIn(cursor);
        cursor.close();

        return newCheckIn;
    }

    public void deleteCheckIn(CheckIn checkIn) {
        long id = checkIn.getId();
        System.out.println("CheckIn deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<CheckIn> getAllCheckIns() {
        List<CheckIn> comments = new ArrayList<CheckIn>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CheckIn comment = cursorToCheckIn(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return comments;
    }

    private CheckIn cursorToCheckIn(Cursor cursor) {
        CheckIn checkIn = new CheckIn();
        checkIn.setId(cursor.getLong(0));
        checkIn.setComment(cursor.getString(1));

        return checkIn;
    }
}
