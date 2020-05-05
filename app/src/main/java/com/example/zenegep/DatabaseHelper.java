package com.example.zenegep;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME="Zenegep.db";
    public static final String TABLE_SERVER="Szerver";
    public static final String TABLE_CLIENT="Kliens";

    @Override
    public void onCreate(SQLiteDatabase db){}

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SERVER);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CLIENT);
        onCreate(db);
    }

    public DatabaseHelper(Context context ){
        super(context,DATABASE_NAME, null,1);
        SQLiteDatabase db=this.getWritableDatabase();
    }


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

    public void initDatabase(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: ezt a két sort ne felejtsük el majd kikommentelni, mert igy mindig dropolja a tablet
        //String dropdatabase="DROP TABLE IF EXISTS "+table;
        //db.execSQL(dropdatabase);

        String databaseCreate;
        String dataToInsert;
        if(!checkIfTableExists(db, table)) {
            databaseCreate = "CREATE TABLE IF NOT EXISTS "+table +
                    "(Video_ID TEXT PRIMARY KEY, " +
                    "Video_NAME TEXT, " +
                    "SentCount INTEGER DEFAULT 0, " +
                    "Timestamp TEXT DEFAULT '1970-01-01 00:00:00')";

            db.execSQL(databaseCreate);
            //TODO: feltölteni a zenékkel, amit akarunk, hogy benne legyen
            dataToInsert = "INSERT INTO "+table+" (Video_ID, Video_NAME) VALUES" +
                    "('fJ9rUzIMcZQ', 'Queen - Bohemian Rhapsody')," +
                    "('NyOGIsds2C4','Yung Gravy, bbno$ - Whip A Tesla')," +
                    "('zSISvlwYweI','BSW - $oha nem elég')," +
                    "('HarFaDxCoxE','Dzsúdló - LEJ ft. Lil Frakk')," +
                    "('kJNk4TW7RAk','Rilés - PESETAS')," +
                    "('MvjQTA81MhY','Yung Mavu - BLACK MAGIC')," +
                    "('W5j89pdFG98','BSW - HELLO')," +
                    "('zgtBk2IOOrA','BSW - YAAY')," +
                    "('7wtfhZwyrcc','Imagine Dragons - Believer')," +
                    "('r7qovpFAGrQ','Lil Nas X - Old Town Road ft. Billy Ray Cyrus')," +
                    "('mWRsgZuwf_8','Imagine Dragons - Demons')," +
                    "('ktvTqknDobU','Imagine Dragons - Radioactive')," +
                    "('o7X3LYGV_N8','The Automatic - Monster')," +
                    "('y9ANOzmSKQg','The White Stripes - Seven Nation Army')," +
                    "('5ZYgIrqELFw','All Star - Smash Mouth')," +
                    "('dQw4w9WgXcQ','Rick Astley - Never Gonna Give You Up')," +
                    "('q0hyYWKXF0Q','TONES AND I - DANCE MONKEY')," +
                    "('fH0A5WMVNos','Intim Torna Illegál - Csillagtengeren')," +
                    "('LqtCRoLtyNI','YUNGBLUD - California')," +
                    "('LaGIqPEZuVs','Patikadomb - Süssön')," +
                    "('POq2AznJO1Q','Backstreet Boys - Everybody')," +
                    "('i_e7OVZGXiQ','Patikadomb - Ringató')," +
                    "('Dhqd59onqQc','VALMAR - NYAKLEVES')," +
                    "('DdWZKb659K0','Alphaville - Big in Japan')," +
                    "('Jgj_TN8VnA4','VALMAR - Deák Téri Gyros Tál')," +
                    "('0rYVG2aB-9o','Rapülők - Zúg a Volga')," +
                    "('iPUmE-tne5U','Katrina & The Waves - Walking On Sunshine')," +
                    "('Td5vQYbNpek','VALMAR - 2x NEM')," +
                    "('NJzTaFYvnYw','bbno$ - tony thot')," +
                    "('N2Y2vQ-1m7M','Y2K, bbno$ - Lalala')," +
                    "('9s_HrcAjRj8','bbno$ - bad thoughts')," +
                    "('JnDR3HzQfPM','bbno$ - jimmy neutron ft. LIL MAYO')," +
                    "('dmsMGeim1So','bbno$ & so loki - who dat boi')," +
                    "('vDRBAPrCr2I','Carpe Diem - Álomhajó')," +
                    "('pXRviuL6vMY','twenty one pilots: Stressed Out')," +
                    "('hTWKbfoikeg','Nirvana - Smells Like Teen Spirit')," +
                    "('bvC_0foemLY','Robin Schulz - Sugar')," +
                    "('L3wKzyIN1yk','Rag n Bone Man - Human')," +
                    "('ir6nk2zrMG0','Calvin Harris, Rag n Bone Man - Giant')," +
                    "('pjTj-_55WZ8','Rudimental - These Days feat. Jess Glynne, Macklemore & Dan Caplen')," +
                    "('k2qgadSvNyU','Dua Lipa - New Rules')," +
                    "('LVEwL-sZzmM','Lost Frequencies & Zonderling - Crazy')," +
                    "('mmW1t4Y5UtE','ROBIN SCHULZ - SUN GOES DOWN')," +
                    "('RBumgq5yVrA','Passenger | Let Her Go')," +
                    "('3OnnDqH6Wj8','Flo Rida - Good Feeling')," +
                    "('NUVCQXMUVnI0','David Guetta Feat. Kid Cudi - Memories ')," +
                    "('EPo5wWmKEaI','Pitbull - Give Me Everything')," +
                    "('5dbEhBKGOtY','David Guetta - Play Hard ft. Ne-Yo, Akon')," +
                    "('hHUbLv4ThOo','Pitbull - Timber ft. Ke$ha')," +
                    "('or3U2rXxvQw','Project X - Yeah Yeah Yeahs - Heads Will Roll')," +
                    "('y5U-I5wk1uo','BLR x Rave & Crave - Taj')," +
                    "('nAYkXB5yoiw','The Prince Karma - Later Bitches')," +
                    "('Y1_VsyLAGuk','Burak Yeter - Tuesday')," +
                    "('EgPbNGUgTPE','Burak Yeter - Friday Night')," +
                    "('mNNfZuIA1GQ','Robin Schulz & Alligatoah - Willst Du')," +
                    "('P9-4xHVc7uk','Robin Schulz – OK')," +
                    "('FM7MFYoylVs','The Chainsmokers & Coldplay - Something Just Like This')," +
                    "('fRNkQH4DVg8','The Chainsmokers - Paris')," +
                    "('0zGcUoRlhmw','The Chainsmokers - Closer')," +
                    "('UprcpdwuwCg','twenty one pilots: Heathens')," +
                    "('IcrbM1l_BoI','Avicii - Wake Me Up')," +
                    "('EhEIHmhVd1s','ByeAlex és a Slepp x Hiro - Így hagysz el...')," +
                    "('rkPkkSHYeuE','ByeAlex és a Slepp feat. Manuel - Merülök')," +
                    "('EXEtRTmjr48','ManGoRise - A Szó Nem Elég')," +
                    "('iE8L4ibodtA','Konyha - Százszor visszajátszott')," +
                    "('6Cp6mKbRTQY','Avicii - Hey Brother')," +
                    "('cHHLHGNpCSA','Avicii - Waiting For Love')," +
                    "('BgfcToAjfdc','Kygo - Stole The Show feat. Parson James')," +
                    "('2GADx4Hy-Gg','Avicii - You Make Me')," +
                    "('jGflUbPQfW8','OMI - Cheerleader')," +
                    "('foE1mO2yM04','Mike Posner - I Took A Pill In Ibiza')," +
                    "('UtF6Jej8yb4','Avicii - The Nights')," +
                    "('lEU011dGjbk','Bagossy Brothers Company - Olyan Ő')," +
                    "('ebXbLfLACGM','Calvin Harris - Summer')," +
                    "('9Sc-ir2UwGU','Kygo - Firestone')," +
                    "('RTergPAxY5Y','ROBIN SCHULZ & MARC SCIBILIA - UNFORGETTABLE')," +
                    "('KrVC5dm5fFc','Nicky Romero - Toulouse')," +
                    "('GsF05B8TFWg','Axwell Λ Ingrosso - More Than You Know')," +
                    "('gCYcHz2k5x0','Martin Garrix - Animals')," +
                    "('4t6rQDxJwno','Lost Frequencies ft. James Blunt - Melody')," +
                    "('YqeW9_5kURI','Major Lazer & DJ Snake - Lean On')," +
                    "('el6gxwgLKV4','Pamkutya - Aranybula')," +
                    "('DyDfgMOUjCI','Billie Eilish - bad guy')," +
                    "('-PZsSWwc9xA','Billie Eilish - all the good girls go to hell')," +
                    "('mfJhMfOPWdE','Armin van Buuren - Blah Blah Blah')," +
                    "('__TvPr_Wtvc','Timmy Trumpet - Oracle')," +
                    "('ofmzX1nI7SE','Timmy Trumpet & Savage - Freaks')," +
                    "('BB3OGwplrLo','Carnage feat. Timmy Trumpet & KSHMR - Toca')," +
                    "('r5-T4oOCLoM','Gigi DAgostino - LAmour Toujours')," +
                    "('KQ6zr6kCPj8','LMFAO ft. Lauren Bennett, GoonRock - Party Rock Anthem')," +
                    "('iOxzG3jjFkY','Willy William - Ego')," +
                    "('1W5BA0lDVLM','Mahmut Orhan & Colonel Bagshot - 6 Days')," +
                    "('QtXby3twMmI','Coldplay - Adventure Of A Lifetime')," +
                    "('QK8mJJJvaes','MACKLEMORE & RYAN LEWIS - THRIFT SHOP FEAT. WANZ')," +
                    "('CY8E6N5Nzec','Marshmello & Anne-Marie - FRIENDS')," +
                    "('fKopy74weus','Imagine Dragons - Thunder')," +
                    "('YVkUvmDQ3HY','Eminem - Without Me')," +
                    "('_Yhyp-_hX2s','Eminem - Lose Yourself')," +
                    "('ywvRgGAd2XI','twenty one pilots: Lane Boy')," +
                    "('JGwWNGJdvx8','Ed Sheeran - Shape of You')," +
                    "('8UVNT4wvIGY','Gotye - Somebody That I Used To Know (feat. Kimbra)')," +
                    "('YlUKcNNmywk','Red Hot Chili Peppers - Californication')," +
                    "('Soa3gO7tL-c','Green Day: Boulevard Of Broken Dreams')," +
                    "('eVTXPUF4Oz4','Linkin Park - In The End')," +
                    "('GX8Hg6kWQYI','XXXTENTACION - MOONLIGHT')," +
                    "('wJGcwEv7838','XXXTENTACION - Look at me')," +
                    "('FAucVNRx_mU','XXXTENTACION - Jocelyn Flores')," +
                    "('pgN-vvVVxMA','XXXTENTACION - SAD!')," +
                    "('f0bbDFRYD_A','XXXTENTACION - changes')," +
                    "('LHCob76kigA','Lukas Graham - 7 Years')";

            db.execSQL(dataToInsert);
            db.close();

            getLastDayPopularList(table);
        }
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

    public ArrayList getMusicList(String table){
        ArrayList<String> musicList = new ArrayList<String>();
        String sql = "SELECT Video_NAME, Video_ID FROM "+table+" ORDER BY Video_NAME";
        Cursor c = getData(sql);
        c.moveToFirst();

        while(!c.isAfterLast()) {
            musicList.add(c.getString(c.getColumnIndex("Video_NAME")));
            c.moveToNext();
        }
        c.close();
        return musicList;
    }

    public ArrayList getVideoIDList(String table)
    {
        ArrayList<String> musicIDs = new ArrayList<String>();
        String sql = "SELECT Video_NAME, Video_ID FROM "+table+" ORDER BY Video_NAME";
        Cursor c = getData(sql);
        c.moveToFirst();

        while(!c.isAfterLast()) {
            musicIDs.add(c.getString(c.getColumnIndex("Video_ID")));
            c.moveToNext();
        }

        c.close();
        return musicIDs;
    }
    public ArrayList getVideoSentCount(String table)
    {
        ArrayList<Integer> musicSentcount = new ArrayList<Integer>();
        String sql = "SELECT Video_NAME, SentCount FROM "+table+" ORDER BY Video_NAME";
        Cursor c = getData(sql);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            musicSentcount.add(c.getInt(c.getColumnIndex("SentCount")));
            c.moveToNext();
        }
        c.close();
        return musicSentcount;
    }

    public ArrayList getAllTimePopularList(String table){
        ArrayList<String> list = new ArrayList<String>();
        String sql = "SELECT Video_NAME, SentCount FROM "+table+" ORDER BY SentCount DESC";
        Cursor c = getData(sql);
        c.moveToFirst();

        while(!c.isAfterLast()){
            String videoname = c.getString(c.getColumnIndex("Video_NAME"));
            int count = c.getInt(c.getColumnIndex("SentCount"));
            String stringToAdd = videoname + " - "+count;
            list.add(stringToAdd);
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public ArrayList getLastDayPopularList(String table){
        ArrayList<String> list = new ArrayList<String>();
        String sql = "SELECT Video_NAME, SentCount FROM "+table+" WHERE Timestamp >= datetime('now', '-1 days') AND Timestamp < datetime('now')" +
                " ORDER BY SentCount DESC";
        Cursor c = getData(sql);
        c.moveToFirst();

        while(!c.isAfterLast()){
            String videoname = c.getString(c.getColumnIndex("Video_NAME"));
            int count = c.getInt(c.getColumnIndex("SentCount"));
            String stringToAdd = videoname + " - "+count;
            list.add(stringToAdd);
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public void updateSql(String table, String videoid){
        String updateSql = "UPDATE "+table+" SET SentCount = SentCount + 1," +
                "Timestamp = datetime('now')" +
                "WHERE Video_ID = '"+videoid+"'";

        Query(updateSql);
    }

    public String suggestMusic(){
        Random rnd = new Random();
        String musicIdToSuggest;
        String[] musicList = new String[20];
        int i = 0;
        String sql = "SELECT Video_NAME FROM "+TABLE_CLIENT+" ORDER BY SentCount DESC LIMIT 20";
        Cursor c = getData(sql);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            musicList[i] = c.getString(c.getColumnIndex("Video_NAME"));
            c.moveToNext();
            i++;
        }
        c.close();
        musicIdToSuggest=musicList[rnd.nextInt(20)];

        return musicIdToSuggest;
    }

    public boolean isInDatabase(String musicId, String table){
        ArrayList<String> idList = getVideoIDList(table);
        return idList.contains(musicId);
    }
}
