package software.pipas.oprecox.modules.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import software.pipas.oprecox.modules.dataType.Ad;
import software.pipas.oprecox.modules.dataType.AdPreview;
import software.pipas.oprecox.util.Util;

public class DatabaseHandler
{
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = { DatabaseHelper.COLUMN_ID,
                                    DatabaseHelper.COLUMN_TITLE,
                                    DatabaseHelper.COLUMN_DESCRIPTION,
                                    DatabaseHelper.COLUMN_THUMBNAIL,
                                    DatabaseHelper.COLUMN_URL };

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

    public AdPreview createAd (Ad ad)
    {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, ad.getTitle());
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, getStringPrice(ad.getPrice()) + " \u00b7 " + ad.getDescription());
        values.put(DatabaseHelper.COLUMN_THUMBNAIL, Util.bitmapToByteArray(Util.bitmapToThumbnail(ad.getImages().get(0), 56)));
        values.put(DatabaseHelper.COLUMN_URL, ad.getUrl());

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

    private String getStringPrice(float price)
    {
        if (price == (int) price)
            return String.format("%d€", (int) price);
        else
            return String.format("%.2f€", price);
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
        ad.setUrl(cursor.getString(4));
        Log.d("DATABASE", "Ad added with id: " + ad.getId() + " and title: " + ad.getTitle());
        return ad;
    }
}
