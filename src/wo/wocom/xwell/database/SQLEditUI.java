package wo.wocom.xwell.database;

import wo.wocom.xwell.CMDExecute;
import wo.wocom.xwell.R;
import wo.wocom.xwell.utility.XA_util_fileExits;
import wo.wocom.xwell.utility.XA_util_readStrByregEx;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.10.3.11:8
 * @more 打开sqlite文件，root 改变权限，执行sql命令
 */
public class SQLEditUI extends Activity {

	String TAG = "PAC_DB_SQUI";

	SQLiteDatabase db; // 数据库对象
	Cursor cur; // 数据库操作光标
	String sql = "select * from sqlite_master where type='table'";// 显示所有表
	String DB_Path, show, DB_Path_DIR = null;
	// 控件
	EditText et;
	Button bt;
	TextView tv;
	AlertDialog.Builder et_dialog;

	// 生命开始
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pac_db_sqleditui); // 设置主布局

		// 引用edittext.xml文件中的视图组件
		LayoutInflater inflater = (LayoutInflater) SQLEditUI.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		// View&xml关联
		final View et_dialog_v = inflater.inflate(R.layout.et_dialog, null);

		// db文件 对话框
		et_dialog = new AlertDialog.Builder(SQLEditUI.this); // 实例化
		et_dialog.setTitle(R.string.pac_db_dbname).setView(et_dialog_v);

		et_dialog.setPositiveButton(R.string.pac_b_bmv_ok_btn,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

						// xml中的控件
						EditText db_path_et = (EditText) et_dialog_v
								.findViewById(R.id.pac_db_ed_et);
						CharSequence edit_value = db_path_et.getText();
						arg0.dismiss();
						DB_Path = edit_value.toString();

						// 自定义文件是否存在
						if (!XA_util_fileExits.dofExits(DB_Path)) {
							DB_Path = null;

							Toast.makeText(
									SQLEditUI.this,
									" "
											+ SQLEditUI.this
													.getString(R.string.pac_db_dbname)
											+ SQLEditUI.this
													.getString(R.string.pac_db_outIsNull),
									Toast.LENGTH_LONG).show();// 提示
						}// if

						// -------生成界面---------
						if (DB_Path != null) { // 如果 db文件不存在

							// 控件
							et = (EditText) findViewById(R.id.pac_db_sqlui_et);
							bt = (Button) findViewById(R.id.pac_db_sqlui_bt);
							tv = (TextView) findViewById(R.id.pac_db_sqlui_tv);

							DB_Path_DIR = XA_util_readStrByregEx.readout(
									DB_Path, "(\\/.*\\/)", 0);// (\/.*\/) 获取文件目录
							Log.i(TAG, DB_Path_DIR);

							CMDExecute.Rootrun("chmod 777 " + DB_Path);
							// root权限改DB文件权限
							CMDExecute.Rootrun("chmod 777 " + DB_Path_DIR);
							// root权限改DB文件目录的权限
							// Failure 14 (unable to open database file) on
							// 0x60f110 when executing 。。。

							et.setText(sql);

						}

					}// onClick
				});

		et_dialog.setNegativeButton(R.string.pac_b_bmv_no_btn,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						SQLEditUI.this.finish(); // 退出
					}
				});

		et_dialog.create().show();

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

			// 如果是更新数据
			if (!sqlstmt.startsWith("select") & !sqlstmt.startsWith("SELECT")) {
				// 不【以select开头】并且【不以SELECT开头】
				Log.i(TAG, sqlstmt);

				try {
					db.execSQL(sqlstmt);
					resultSet = sql + "\nSuccess";
				} catch (SQLiteException e) {
					Toast.makeText(getApplicationContext(), e.toString(),
							Toast.LENGTH_LONG).show();
					resultSet = sql + "\nFailed.\n" + "e.toString()";
				}

			}
			// 如果是查询数据
			else {
				try {
					cur = db.rawQuery(sqlstmt, null);// 执行sql查询

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
					} else {
						resultSet = "???";
					}
					Log.i(TAG, "resultSet:\n" + resultSet);

				} catch (SQLiteException e) {
					Toast.makeText(getApplicationContext(), e.toString(),
							Toast.LENGTH_LONG).show();
				}
				cur.close();
				// android.database.sqlite.DatabaseObjectNotClosedException:
				// Application did not close the cursor or database object that
				// was
				// opened here

			}// else end

			// if()

			db.close();

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
