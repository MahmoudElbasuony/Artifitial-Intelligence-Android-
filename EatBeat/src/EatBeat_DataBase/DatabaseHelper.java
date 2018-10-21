package EatBeat_DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	


	//Set up the Database
	public DatabaseHelper(Context context) {
		super(context, "EatBeatDB", null, 1);		
	}

	//Set up the table
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(" create table IF NOT EXISTS EatBeat(Player_ID INTEGER PRIMARY KEY AUTOINCREMENT,Player_Name TEXT, Player_Score INTEGER)");
		db.execSQL(" create table IF NOT EXISTS options (option_id INTEGER PRIMARY KEY AUTOINCREMENT, option INTEGER)");
		db.execSQL(" create table IF NOT EXISTS levels (level INTEGER PRIMARY KEY AUTOINCREMENT, block_level TEXT)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

	//Insert Data to Database
	public void insertData(String player,int score){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues vals = new ContentValues();
		vals.put("Player_Name", player);
		vals.put("Player_Score", score);
		db.execSQL("INSERT OR IGNORE INTO EatBeat (Player_Name, Player_Score) VALUES ('"+player+"',"+score+"); ");

	}

	public boolean insertOptionData(int opt){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues vals = new ContentValues();
		vals.put("option", opt);
		long res = db.insert("options", null, vals);
		return res == -1 ? false : true;
	}

	public boolean insertLevelData(String blck_lev){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues vals = new ContentValues();
		vals.put("block_level", blck_lev);
		long res = db.insert("levels", null, vals);
		return res == -1 ? false : true;
	}

	public Cursor viewScoreData(){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = db.rawQuery("select Player_Score from EatBeat order by Player_Score desc", null);
		return res;
	}

	//Get the Data
	public Cursor viewAllData(){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = db.rawQuery("select * from EatBeat", null);
		return res;
	}
	//Get the Data
	public Cursor viewAllOptions(){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = db.rawQuery("select * from options ", null);

		return res;
	}

	public Cursor viewAllLevels(){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = db.rawQuery("select * from levels ", null);

		return res;
	}

	public boolean update_Levels( int lev, String blc_lev){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE levels SET block_level='"+blc_lev+"' WHERE level= "+lev+"; ");
		return true;
	}

	//Update the options 
	public boolean update_options(int opt_id, int opt){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE options SET option="+opt+" WHERE option_id= "+opt_id+"; ");
		return true;
	}

	//Delete the Data
	public Integer deleteData(String id){
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete("EatBeat", "Player_ID = ?", new String[] { id });
	}

}
