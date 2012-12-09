package wo.wocom.xwell.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * @author unknown
 * @improve wuwenjie wuwenjie.tk
 * @version 1.3.1
 * @more 自定义SQLiteOpenHelper，用于数据库的操作管理
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "historyDB";
	public static final String TB_NAME = "historyTB";

	public SQLiteHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	// 数据库第一次产生调用方法，可创建新表
	public void onCreate(SQLiteDatabase db) {
		// CREATE TABLE IF NOT EXISTS historyTB(_id integer primary key,url
		// varchar,time integer,name varchar)
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME + "("
				+ HistoryBean.ID + " integer primary key," + HistoryBean.URL
				+ " varchar," + HistoryBean.TIME + " integer,"
				+ HistoryBean.NAME + " varchar" + ")");
		Log.i(DB_NAME, "onCreate");
	}

	// 当数据库需要升级,当检测与前一次创建数据库版本不一样时，先删除表再创建新表
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		onCreate(db);
		Log.i(DB_NAME, "onUpgrade");
	}

	// 当打开数据库时的回调函数
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i(DB_NAME, "onOpen");
	}

	/**
	 * 变更列名
	 */
	public void updateColumn(SQLiteDatabase db, String oldColumn,
			String newColumn, String typeColumn) {
		try {
			db.execSQL("ALTER TABLE " + TB_NAME + " CHANGE " + oldColumn + " "
					+ newColumn + " " + typeColumn);
			Log.i("updateColumn", "ALTER TABLE " + TB_NAME + " CHANGE "
					+ oldColumn + " " + newColumn + " " + typeColumn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}