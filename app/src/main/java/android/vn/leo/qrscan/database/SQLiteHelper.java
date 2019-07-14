package android.vn.leo.qrscan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.vn.leo.qrscan.data.ScanResult;
import android.vn.leo.qrscan.interfaces.DatabaseHelper;
import android.vn.leo.qrscan.utils.CommonMethod;
import android.vn.leo.qrscan.utils.DateDescComparator;
import android.vn.leo.qrscan.utils.LocalStorageManager;

import com.google.zxing.client.result.ParsedResultType;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper implements DatabaseHelper {

    private static final String TAG = "SQLHelper";

    private static final String DATABASE_NAME = "qrscan.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "codes";
    private final Context context;
    private static SQLiteHelper instance;

    public static synchronized SQLiteHelper getInstance() {
        if (instance == null) {
            throw new NullPointerException("Database not initialize");
        }
        return instance;
    }

    public static void init(final Context context) {
        if (instance == null) {
            instance = new SQLiteHelper(context);
        }
    }

    private SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_NAME + "(" +
                "id integer primary key autoincrement," +
                "image text," +
                "code text," +
                "date text," +
                "type integer)");
    }

    @Override
    public List<ScanResult> read() {
        SQLiteDatabase db = getReadableDatabase();
        List<ScanResult> resultSet = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME, new String[] {
                "id", "image", "code", "date", "type"
        }, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ScanResult scanResult = new ScanResult();
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                Bitmap bitmap = getImageFromFileName(cursor.getString(cursor.getColumnIndex("image")));
                String code = cursor.getString(cursor.getColumnIndex("code"));
                Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex("date"))));
                ParsedResultType type = ParsedResultType.values()[cursor.getInt(cursor.getColumnIndex("type"))];
                scanResult.setId(id);
                scanResult.setResult(code);
                scanResult.setImage(bitmap);
                scanResult.setDate(date);
                scanResult.setType(type);
                resultSet.add(scanResult);
            } while (cursor.moveToNext());
        }
        assert cursor != null;
        cursor.close();
        db.close();
        Collections.sort(resultSet, new DateDescComparator());
        return resultSet;
    }

    public Bitmap getImageFromFileName(String fileName) {
        try {
            FileInputStream is = context.openFileInput(fileName);
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public long insert(ScanResult scanResult) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("image", LocalStorageManager.isEnableSaveCodeImage() ?
                CommonMethod.getNameOfImage(scanResult.getDate()) : "");
        contentValues.put("code", scanResult.getResult());
        contentValues.put("date", String.valueOf(scanResult.getDate().getTime()));
        contentValues.put("type", CommonMethod.getIndexOfType(scanResult.getType().toString()));

        long i = db.insert(TABLE_NAME, null, contentValues);

        db.close();

        return i;
    }

    @Override
    public boolean update(ScanResult scanResult) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("image", CommonMethod.getNameOfImage(scanResult.getDate()));
        contentValues.put("code", scanResult.getResult());
        contentValues.put("date", String.valueOf(scanResult.getDate().getTime()));
        contentValues.put("type", scanResult.getType().toString());

        long i = db.update(TABLE_NAME, contentValues, "id=?", new String[]{String.valueOf(scanResult.getId())});

        db.close();

        return i > 0;
    }

    @Override
    public boolean remove(ScanResult scanResult) {
        SQLiteDatabase db = getWritableDatabase();

        int i = db.delete(TABLE_NAME, "id=?", new String[] {String.valueOf(scanResult.getId())});

        db.close();

        return i > 0;
    }

    @Override
    public boolean remove(String id) {
        SQLiteDatabase db = getWritableDatabase();

        int i = db.delete(TABLE_NAME, "id=?", new String[] {id});

        db.close();

        return i > 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            // Do somethings
        }
    }
}
