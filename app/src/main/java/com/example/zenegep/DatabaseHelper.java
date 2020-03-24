package com.example.zenegep;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
public static final String DATABASE_NAME="Zenegep";
    public static final String TABLE_NAME="Video";
    public static final String COL_1="VIDEO_ID";
    public static final String COL_2="SENTQUANTITY";
    public static final String COL_3="CHANNELNAME";
    public static final String COL_4="TYPEOFMUSIC";

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " +TABLE_NAME+"(Video_ID TEXT PRIMARY KEY,SENTQUANTITY INTEGER, CHANNELNAME TEXT,TYPEOFMUSIC TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public DatabaseHelper(Context context ){
        super(context,DATABASE_NAME, null,1);
        SQLiteDatabase db=this.getWritableDatabase();
    }
    public void insertData(String videoid, int quantity, String channame, String typeofmusic  )
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,videoid);
        contentValues.put(COL_2,quantity);
        contentValues.put(COL_3,channame);
        contentValues.put(COL_4,typeofmusic);

    }
}
