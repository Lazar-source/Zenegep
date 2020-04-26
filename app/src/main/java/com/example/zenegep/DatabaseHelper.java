package com.example.zenegep;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME="Zenegep.db";
    public static final String TABLE_NAME="Zenekeres";
    public static final String COL_1="BEKULDES";
    public static final String COL_2="VIDEO_ID";
    public static final String COL_3="VIDEO_NAME";


    private boolean checkIfTableExists(SQLiteDatabase db, String table){
        //TODO befejezni ezt a szart

        return false;
    }
    private void initDatabase(SQLiteDatabase db){

        if(checkIfTableExists(db, TABLE_NAME)) {
            String database = "CREATE TABLE IF NOT EXISTS Zenekeres"
                    + "(BEKULDES INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "Video_ID TEXT,"
                    + "Video_NAME TEXT)";
            db.execSQL(database);

            String dataToInsert = "INSERT INTO Zenekeres (Video_ID, Video_NAME) VALUES" +
                    "('fJ9rUzIMcZQ', 'Queen - Bohemian Rhapsody')," +
                    "('NyOGIsds2C4','Yung Gravy, bbno$ - Whip A Tesla')," +
                    "('zSISvlwYweI','BSW - $oha nem elég')";

            db.execSQL(dataToInsert);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        initDatabase(db);
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
    public boolean insertData(String videoid, String videoname, String bekuldes)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,videoid);
        contentValues.put(COL_2,videoname);
        contentValues.put(COL_3,bekuldes);

        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
        {
            return false;
        }
       else {
            return true;
        }

    }
}
