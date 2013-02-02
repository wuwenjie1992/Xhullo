package wo.wocom.xwell.database;

import wo.wocom.xwell.CMDExecute;
import wo.wocom.xwell.R;
import wo.wocom.xwell.utility.XA_util_fileExits;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.10.3.11:5
 * @more 打开sqlite文件，root 改变权限，执行sql命令
 */
public class SQLEditUI extends Activity {

	String TAG = "PAC_DB_SQUI";
	String DB_Path = "/data/data/com.og.danjiddz/databases/ddz.LandLords";// 数据库文件

	SQLiteDatabase db; // 数据库对象
	Cursor cur; // 数据库操作光标
	String sql = "select * from sqlite_master where type='table'";// 显示所有表
	String show = null;
	Toast to;
	// 控件
	EditText et;
	Button bt;
	TextView tv;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pac_db_sqleditui); // 设置主布局

		// 控件
		et = (EditText) findViewById(R.id.pac_db_sqlui_et);
		bt = (Button) findViewById(R.id.pac_db_sqlui_bt);
		tv = (TextView) findViewById(R.id.pac_db_sqlui_tv);

		CMDExecute.Rootrun("chmod 777 " + DB_Path);// root 权限 改DB权限
		// boolean

		et.setText(sql);

	}// oncreat end

	// Button bt 监听方法 android:onClick="bt_onClick"
	public void bt_onClick(View v) {
		// 缺了 View v
		// java.lang.IllegalStateException: Could not find a method
		// bt_onClick(View) in the activity class
		// wo.wocom.xwell.database.SQLEditUI for onClick handler on view class
		// android.widget.Button with id 'pac_db_sqlui_bt'

		sql = et.getText().toString();// 获得sql命令

		show = rawQOutToS(sql); // 执行sql

		if (show == null) {
			tv.setText(R.string.pac_db_outIsNull);
		} else
			tv.setText(show); // 显示结果

		et.setVisibility(View.GONE);// View.GONE移除控件
		bt.setVisibility(View.GONE);
	}

	// 执行sql返回字符串
	public String rawQOutToS(String sqlstmt) {

		String resultSet = null;

		if (XA_util_fileExits.dofExits(DB_Path)) {// 是否存在 数据库文件

			CursorFactory cursor_f = null;
			db = SQLiteDatabase.openDatabase(DB_Path, cursor_f,
					SQLiteDatabase.OPEN_READWRITE);// 打开数据库,path,CursorFactory,flags,读写
			try {
				cur = db.rawQuery(sqlstmt, null);// 执行sql查询
			} catch (SQLiteException e) {
				Toast.makeText(getApplicationContext(), e.toString(),
						Toast.LENGTH_LONG).show();
			}

			if (cur.moveToFirst()) {// 是否 移动到第一行

				Log.i(TAG,
						"行count" + cur.getCount() + "Column:"
								+ cur.getColumnCount() + "\n");

				do {
					for (int i = 0; i <= cur.getColumnCount() - 1; i++) {
						resultSet = resultSet + cur.getString(i) + "\t";
					}
					resultSet = resultSet + "\n\n";

				} while (cur.moveToNext());// 是否 可以移动到下一行
			}

			Log.i(TAG, "resultSet:\n" + resultSet);

			db.close();
			cur.close();
			// android.database.sqlite.DatabaseObjectNotClosedException:
			// Application did not close the cursor or database object that was
			// opened here

		}

		return resultSet;

	}// rawQOutToS end

	// 菜单键 按下
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// 显示控件
		et.setVisibility(View.VISIBLE);
		bt.setVisibility(View.VISIBLE);
		return false;

	}

}
