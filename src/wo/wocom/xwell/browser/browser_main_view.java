package wo.wocom.xwell.browser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wo.wocom.xwell.R;
import wo.wocom.xwell.database.HistoryBean;
import wo.wocom.xwell.database.SQLiteHelper;
import wo.wocom.xwell.utility.XA_util_ProgressDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * @author unknown
 * @improve wuwenjie wuwenjie.tk
 * @version 1.3.1
 * @more 自定义webbrowser的主界面
 */
public class browser_main_view extends Activity {

	private static String TAG = "XA_pac_b_bmv";
	private WebView mWebView = null; // 网页视图，WebKit渲染引擎
	final Activity context = this;
	private SQLiteHelper mOpenHelper; // 自定义SQLiteOpenHelper，用于数据库的操作管理
	public static Cursor myCursor_one;// 光标，提供随机读写访问【由数据库查询返回的】结果集
	Intent directCall;// 自定义 意图
	private WriteFavoriteXml writeXml = new WriteFavoriteXml();//
	private EditText edit = null; // 导航栏
	private ImageButton btn, forwardBtn, backBtn, menuBtn = null;
	private ListView list = null;// 历史纪录列表
	private Button go_back = null;
	private XA_util_ProgressDialog XA_util_pd;// 自定义进度对话框

	private final static int HISTORY_ITEM = 0; //
	private final static int HTTP_ITEM = 1; //
	private final static int SHORTCUT_ITEM = 2; //
	private final static int ADD_FAVORITE = 3; //
	private final static int FAVORITE_ITEM = 4; //
	private final static int PREFERENCE_ITEM = 5; //
	private final static int EXIT_ITEM = 7; //

	private String cur_url = "http://www.wuwenjie.tk"; // 主页，当前链接
	private final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	// 添加桌面快捷方式
	List<Map<String, Object>> history_data = new ArrayList<Map<String, Object>>();
	List<HistoryBean> xml_data = new ArrayList<HistoryBean>();
	String[] dialog_data = new String[] {};
	public int selectId = 0;

	SharedPreferences sp;// 共享首选项，用于存改游览器配置
	Drawable drawable;

	private static String SAVE_KEY = "save-view";

	public static browser_main_view instance;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Log.w(TAG, "onCreate");

		// requestWindowFeature(Window.FEATURE_PROGRESS);// 进度指示器
		// 全屏显示窗口
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mOpenHelper = new SQLiteHelper(this);
		directCall = new Intent(Intent.ACTION_MAIN);
		// 标识Activity为一程序的开始，Start as main entry point, does not receive data.
		onInit();
		instance = this;
		mWebView.requestFocus(); // 要求聚焦，可对webview进行操作

		if (savedInstanceState == null) {// savedInstanceState 保存当时的状态
			deleteTable();// 自定义方法
		} else {
			Bundle map = savedInstanceState.getBundle(SAVE_KEY);
			// getBundle，返回【与给定的键对应的】值，无则返回null，
			if (map != null) {
				restoreState(map);// 自定义方法
			}
		}
	}

	// 其他生命周期
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
	}

	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
	}

	protected void onStart() {
		super.onStart();
		Log.i(TAG, "onStart");
	}

	protected void onRestart() {
		super.onRestart();
		Log.i(TAG, "onRestart");
	}

	protected void onStop() {
		super.onStop();
		Log.i(TAG, "onStop");
	}

	protected void onDestroy() {
		showDialog(EXIT_ITEM);
		// super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

	// 初始化
	private void onInit() {

		setContentView(R.layout.browser_main_view); // 设置主布局

		edit = (EditText) findViewById(R.id.pac_b_bmv_edit_1);
		mWebView = (WebView) findViewById(R.id.pac_b_bmv_wv1);
		btn = (ImageButton) findViewById(R.id.pac_b_bmv_button_1);
		forwardBtn = (ImageButton) findViewById(R.id.pac_b_bmv_forward_btn);
		backBtn = (ImageButton) findViewById(R.id.pac_b_bmv_back_btn);
		menuBtn = (ImageButton) findViewById(R.id.pac_b_bmv_menu_btn);

		XA_util_pd = new XA_util_ProgressDialog(context);

		// 按钮监听
		btn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				String str = edit.getText().toString();
				if (str != "") {
					cur_url = str;
					setTitle("正在载入...");

					// 进度对话框
					XA_util_pd.setloadingstyle(str, "loading...",
							R.drawable.browser_icon);
					XA_util_pd.show();

					mWebView.loadUrl(str);// ？？？？？？？？？？？？？？？？？？？？？？？？

				}
			}//btn onclick end
		});
		forwardBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (mWebView.canGoForward())
					mWebView.goForward();
			}
		});
		backBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (mWebView.canGoBack())
					mWebView.goBack();
			}
		});
		menuBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				context.openOptionsMenu();
			}
		});

		// 在WebView中显示网页，而不是在内置浏览器中浏览
		mWebView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				mWebView.loadUrl(url);
				cur_url = url;
				setTitle("shouldOverrideUrlLoading");
				// insertTable(url,1,mWebView.getTitle());
				return true;
			}// shouldOverrideUrlLoading

			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				XA_util_pd.util_pDialogCancel();// 对话框取消
			}

		});

		//
		mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// ActivityWebview
				//
				context.setProgress(progress * 100);
				if (progress >= 100) {
					insertTable(cur_url, 1, view.getTitle());
				}
				// Log.d("TTTTTTTTT",progress+","+view.getTitle());
			}
		});

		mWebView.loadUrl(cur_url);
		setFavicon();

		Log.i(TAG, cur_url);
	}// oninit end

	// 菜单
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		menu.add(0, HISTORY_ITEM, HISTORY_ITEM, R.string.pac_b_bmv_history)
				.setIcon(R.drawable.history); // setIcon,setText
		menu.add(0, HTTP_ITEM, HTTP_ITEM, R.string.pac_b_bmv_http_name)
				.setIcon(R.drawable.about);
		menu.add(0, SHORTCUT_ITEM, SHORTCUT_ITEM, R.string.pac_b_bmv_shortcut)
				.setIcon(R.drawable.browser_icon);
		menu.add(0, ADD_FAVORITE, ADD_FAVORITE, R.string.pac_b_bmv_addFavorite)
				.setIcon(R.drawable.add_favorite);
		menu.add(0, FAVORITE_ITEM, FAVORITE_ITEM, R.string.pac_b_bmv_favorite)
				.setIcon(R.drawable.favorite);
		menu.add(1, PREFERENCE_ITEM, PREFERENCE_ITEM,
				R.string.pac_b_bmv_preference).setIcon(R.drawable.help);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case HISTORY_ITEM:
			goto_history_view();
			break;
		case HTTP_ITEM:
			showDialog(HTTP_ITEM);
			break;
		case SHORTCUT_ITEM:
			createShortcut();
			break;
		case ADD_FAVORITE:
			add_favorite();
			break;
		case FAVORITE_ITEM:
			open_favorite();
			break;
		case PREFERENCE_ITEM:
			goto_help_act();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		Log.i(TAG, "onCreateContextMenu" + v.toString());
		Log.i(TAG, "onCreateContextMenu" + String.valueOf(v.getId()));
	}

	private void setFavicon() {
		Bitmap bitmap = mWebView.getFavicon();
		drawable = new BitmapDrawable(bitmap);
		edit.setCompoundDrawables(drawable, null, null, null);
		// drawable = this.getResources().getDrawable(R.drawable.history);
		// edit.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null,
		// null);
		edit.setText(cur_url);
	}

	private void goto_history_view() {
		getHistory();

		setContentView(R.layout.pac_b_bmv_history);
		list = (ListView) findViewById(R.id.pac_b_bmv_list);
		go_back = (Button) findViewById(R.id.pac_b_bmv_go_back);

		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				android.R.layout.simple_list_item_2, new String[] { "��ҳ",
						"��ַ" }, new int[] { android.R.id.text1,
						android.R.id.text2 });

		// final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, history_data);

		list.setAdapter(adapter);
		go_back.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				onInit();
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				cur_url = history_data.get(position).get("��ַ").toString();
				onInit();
			}
		});
	}

	public void copyHistoryData(WebBackForwardList mylist) {
		int i;
		for (i = 0; i < mylist.getSize(); i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("��ҳ", mylist.getItemAtIndex(i).getTitle());
			item.put("��ַ", mylist.getItemAtIndex(i).getUrl());
			history_data.add(item);

			// history_data.add(mylist.getItemAtIndex(i).getUrl().toString());
			//
		}
	}

	private List<Map<String, Object>> getData() {
		return history_data;
	}

	protected Dialog onCreateDialog(int id) {
		if (id == FAVORITE_ITEM) {
			return new AlertDialog.Builder(browser_main_view.this)
					.setTitle(R.string.pac_b_bmv_fav_name)
					.setSingleChoiceItems(dialog_data, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									selectId = whichButton;
								}
							})
					.setNeutralButton(R.string.pac_b_bmv_open_btn,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (selectId >= 0) {
										cur_url = xml_data.get(selectId)
												.getURL();
										edit.setText(cur_url);
										mWebView.loadUrl(cur_url);
									}
								}
							})
					.setPositiveButton(R.string.pac_b_bmv_del_btn,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (selectId >= 0)
										writeXml.Write(
												context,
												"history.xml",
												writeXml.deleteElement(dialog_data[selectId]));
									selectId = 0;
									removeDialog(FAVORITE_ITEM);
								}
							})
					.setNegativeButton(R.string.pac_b_bmv_close_btn,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									removeDialog(FAVORITE_ITEM);
								}
							}).create();
		}
		if (id == HTTP_ITEM) {
			return new AlertDialog.Builder(browser_main_view.this)
					.setTitle(R.string.pac_b_bmv_http_name)
					.setItems(R.array.http_array,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent();
									intent.setClass(browser_main_view.this,
											HttpData.class);
									Bundle b = new Bundle();
									b.putInt("id", which + 1);
									b.putString("url", cur_url);
									intent.putExtras(b);
									startActivity(intent);

								}
							}).create();
		}

		if (id == EXIT_ITEM) {
			return new AlertDialog.Builder(browser_main_view.this)
					.setIcon(R.drawable.browser_icon)
					.setTitle(R.string.pac_b_bmv_exit_title)
					.setMessage(R.string.pac_b_bmv_exit_message)
					.setPositiveButton(R.string.pac_b_bmv_ok_btn,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									finish();
								}
							})
					.setNegativeButton(R.string.pac_b_bmv_no_btn,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							}).create();
		}
		return null;
	}

	/*  */
	private void insertTable(String url, int time, String title) {
		time = (int) Math.floor(System.currentTimeMillis() / 1000);
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		myCursor_one = db.rawQuery("SELECT * FROM " + SQLiteHelper.TB_NAME
				+ " where name=?", new String[] { String.valueOf(title) });
		String sql;
		String tip;
		if (myCursor_one.moveToFirst()) {
			sql = "update " + SQLiteHelper.TB_NAME + " set " + HistoryBean.TIME
					+ "=" + time + " where " + HistoryBean.NAME + "='" + title
					+ "'";
			Log.i("update", title);
			tip = "update";
		} else {
			sql = "insert into " + SQLiteHelper.TB_NAME + " ("
					+ HistoryBean.URL + ", " + HistoryBean.TIME + ", "
					+ HistoryBean.NAME + ") " + "values('" + url + "','" + time
					+ "','" + title + "');";
			Log.i("insert", title);
			tip = "insert into";
		}

		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			Toast.makeText(browser_main_view.this, tip, Toast.LENGTH_LONG)
					.show();
			return;
		}

		Toast.makeText(browser_main_view.this, tip + " insertTable()",
				Toast.LENGTH_LONG).show();
	}

	/**/
	private void deleteTable() {
		int time = (int) Math.floor(System.currentTimeMillis() / 1000) - 86400;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		String sql = "delete from " + SQLiteHelper.TB_NAME + " where " + time
				+ ">" + HistoryBean.TIME;
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			Toast.makeText(browser_main_view.this, "deleteTable，SQLException",
					Toast.LENGTH_LONG).show();
		}
	}

	private void getHistory() {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		myCursor_one = db.rawQuery("SELECT * FROM " + SQLiteHelper.TB_NAME,
				null);
		int url = myCursor_one.getColumnIndex(HistoryBean.URL);
		int name = myCursor_one.getColumnIndex(HistoryBean.NAME);
		history_data.clear();
		if (myCursor_one.moveToFirst()) {
			do {
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("��ҳ", myCursor_one.getString(name));
				item.put("��ַ", myCursor_one.getString(url));
				history_data.add(item);
				// history_data.add(myCursor_one.getString(url));
			} while (myCursor_one.moveToNext());
		}
		myCursor_one.close();
	}

	private void createShortcut() {
		Intent addShortcut = new Intent(ACTION_ADD_SHORTCUT);
		String numToDial = null;
		Parcelable icon = null;

		numToDial = "TBrowser";
		icon = Intent.ShortcutIconResource.fromContext(this,
				R.drawable.browser_icon);

		addShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, numToDial);

		directCall.addCategory(Intent.CATEGORY_LAUNCHER);
		directCall.setComponent(new ComponentName(this.getPackageName(), this
				.getPackageName() + ".browser.browser_main_view"));// 设置组件

		addShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, directCall);
		addShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		sendBroadcast(addShortcut);
	}

	private void add_favorite() {
		String name = mWebView.getTitle();
		String url = mWebView.getUrl();
		if (name != "" && url != "") {
			writeXml.Write(context, "history.xml",
					writeXml.insertElement(name, url));
			writeXml.onReadXml();
			dialog_data = writeXml.getDialogData();
			xml_data = writeXml.getXmlData();
			showDialog(FAVORITE_ITEM);
		}
	}

	private void open_favorite() {
		writeXml.onReadXml();
		dialog_data = writeXml.getDialogData();
		xml_data = writeXml.getXmlData();
		showDialog(FAVORITE_ITEM);
	}

	private void goto_help_act() {
		Intent intent = new Intent();
		intent.setClass(context, HelpTabAct.class);
		startActivity(intent);
	}

	//
	public void setBlockImage(boolean flag) {
		Log.e("setBlockImage", flag == true ? "true" : "false");
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setBlockNetworkImage(flag);
	}

	public void setCacheMode(boolean flag) {
		Log.e("setCacheMode", flag == true ? "true" : "false");
		WebSettings webSettings = mWebView.getSettings();
		if (flag)
			webSettings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
		else
			webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void setJavaScript(boolean flag) {
		Log.e("setJavaScript", flag == true ? "true" : "false");
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(flag);
	}

	protected void onSaveInstanceState(Bundle outState) {
		outState.putBundle(SAVE_KEY, saveState());
		Log.w(TAG, "onSaveInstanceState");
	}

	public void restoreState(Bundle icicle) {
		cur_url = icicle.getString("URL");
		mWebView.loadUrl(cur_url);
		setFavicon();
	}

	public Bundle saveState() {
		Bundle map = new Bundle();
		map.putString("URL", cur_url);
		return map;
	}

	// 返回按钮按下
	public void onBackPressed() {

		AlertDialog.Builder builder = new Builder(browser_main_view.this);
		builder.setIcon(R.drawable.browser_icon);
		builder.setTitle(R.string.pac_b_bmv_exit_title);
		builder.setMessage(R.string.pac_b_bmv_exit_message);
		builder.setPositiveButton(R.string.pac_b_bmv_ok_btn,
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						android.os.Process.killProcess(android.os.Process
								.myPid());
					}
				});
		builder.setNegativeButton(R.string.pac_b_bmv_no_btn,
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

}
