package software.pipas.oprecox.modules.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

import software.pipas.oprecox.modules.dataType.AdPreview;
import software.pipas.oprecox.util.Util;

import static android.R.attr.id;

public class DatabaseHandler
{
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = { DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_TITLE, DatabaseHelper.COLUMN_DESCRIPTION, DatabaseHelper.COLUMN_THUMBNAIL };

    public DatabaseHandler(Context context)
    {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException
    {
        database = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }

    public AdPreview createAd (String title, String description, Bitmap thumbnail)
    {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_THUMBNAIL, Util.bitmapToByteArray(thumbnail));
        long insertId = database.insert(DatabaseHelper.ADS_TABLE, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.ADS_TABLE,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        AdPreview newAd = cursorToAd(cursor);
        cursor.close();
        return newAd;
    }

    public void deleteComment(AdPreview add)
    {
        long id = add.getId();
        Log.d("DATABASE", "Ad deleted with id: " + id);
        database.delete(DatabaseHelper.ADS_TABLE, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<AdPreview> getAllComments()
    {
        ArrayList<AdPreview> ads = new ArrayList<AdPreview>();

        Cursor cursor = database.query(DatabaseHelper.ADS_TABLE, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            AdPreview newAd = cursorToAd(cursor);
            ads.add(newAd);
            cursor.moveToNext();
        }

        cursor.close();
        return ads;
    }

    private AdPreview cursorToAd(Cursor cursor)
    {
        AdPreview ad = new AdPreview();
        ad.setId(cursor.getLong(0));
        ad.setTitle(cursor.getString(1));
        ad.setDescription(cursor.getString(2));
        ad.setThumbnail(cursor.getBlob(3));
        Log.d("DATABASE", "Ad added with id: " + id + " and title: " + ad.getTitle());
        return ad;
    }
}
