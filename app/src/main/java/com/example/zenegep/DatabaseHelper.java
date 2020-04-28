package com.example.zenegep;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME="Zenegep.db";
    public static final String TABLE_NAME="Zenekeres";

    private boolean checkIfTableExists(SQLiteDatabase db, String table){
        String check = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='"+table+"'";
        Cursor c = db.rawQuery(check, null);
        if (!c.moveToFirst()) {
            c.close();
            return false;
        }
        int count = c.getInt(0);
        c.close();
        return count>0;
    }

    public void initDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
       /* String dropdatabase="DROP TABLE Zenekeres";
        db.execSQL(dropdatabase);*/
        if(!checkIfTableExists(db, TABLE_NAME)) {
            String databaseCreate = "CREATE TABLE IF NOT EXISTS Zenekeres" +
                    "(Video_ID TEXT PRIMARY KEY, " +
                    "Video_NAME TEXT, " +
                    "SentCount INTEGER DEFAULT 0, " +
                    "Timestamp TEXT DEFAULT '1970-01-01');";

            db.execSQL(databaseCreate);
            //TODO: feltölteni a zenékkel, amit akarunk, hogy benne legyen
            String dataToInsert = "INSERT INTO Zenekeres (Video_ID, Video_NAME) VALUES" +
                    "('fJ9rUzIMcZQ', 'Queen - Bohemian Rhapsody')," +
                    "('NyOGIsds2C4','Yung Gravy, bbno$ - Whip A Tesla')," +
                    "('zSISvlwYweI','BSW - $oha nem elég')";

            db.execSQL(dataToInsert);

            db.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        initDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        //ez mit csinál?
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public DatabaseHelper(Context context ){
        super(context,DATABASE_NAME, null,1);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    private Cursor getData(String sql){
        SQLiteDatabase dbR=this.getReadableDatabase();
        return dbR.rawQuery(sql,null);
    }

    private void Query(String sql){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public void insertData(String videoid, String videoname)
    {
        String getSql = "SELECT * FROM Zenekeres WHERE Video_ID='"+videoid+"'";

        if(getData(getSql).getCount()>0){      //ha már szerepel az adatbázisban az adott videoid
            String updateSql = "UPDATE Zenekeres SET SentCount = SentCount + 1," +
                                                "Timestamp = Date('now')" +
                                                "WHERE Video_ID = '"+videoid+"'";
            Query(updateSql);
        }
        else {              //ha nem szerepel a megadott videoid
            String sql = "INSERT INTO Zenekeres VALUES" +
                    "("+videoid+", "+videoname+",1,date('now'))";
            Query(sql);
        }
    }

    public ArrayList getMusicList(){
        ArrayList<String> musicList = new ArrayList<String>();
        String sql = "SELECT Video_NAME FROM "+TABLE_NAME;
        Cursor c = getData(sql);
        c.moveToFirst();

        while(!c.isAfterLast()) {
            musicList.add(c.getString(c.getColumnIndex("Video_NAME")));
            c.moveToNext();
        }
        return musicList;
    }
    public ArrayList getVideoIDList()
    {
        ArrayList<String> musicIDs = new ArrayList<String>();
        String sql = "SELECT Video_ID FROM "+TABLE_NAME;
        Cursor c = getData(sql);
        c.moveToFirst();

        while(!c.isAfterLast()) {
            musicIDs.add(c.getString(c.getColumnIndex("Video_ID")));
            c.moveToNext();
        }
        return musicIDs;
    }

    public ArrayList getAllTimePopularList(){
        ArrayList<String> list = new ArrayList<String>();
        String sql = "SELECT Video_NAME, SentCount FROM Zenekeres ORDER BY SentCount DESC";
        Cursor c = getData(sql);
        c.moveToFirst();

        while(!c.isAfterLast()){
            String videoname = c.getString(c.getColumnIndex("Video_NAME"));
            int count = c.getInt(c.getColumnIndex("SentCount"));
            String stringToAdd = videoname + " - "+count;
            list.add(stringToAdd);
            c.moveToNext();
        }
        return list;
    }

    public ArrayList getLastDayPopularList(){
        ArrayList<String> list = new ArrayList<String>();
        String sql = "SELECT Video_NAME, SentCount FROM Zenekeres ORDER BY SentCount DESC " +
                "WHERE Timestamp >= date('now', '-1 days') AND Timestamp < date('now')";
        Cursor c = getData(sql);
        c.moveToFirst();

        while(!c.isAfterLast()){
            String videoname = c.getString(c.getColumnIndex("Video_NAME"));
            int count = c.getInt(c.getColumnIndex("SentCount"));
            String stringToAdd = videoname + " - "+count;
            list.add(stringToAdd);
            c.moveToNext();
        }
        return list;
    }
}
