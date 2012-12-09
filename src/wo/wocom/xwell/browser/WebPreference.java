package wo.wocom.xwell.browser;

import wo.wocom.xwell.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;

/**
 * @author unknown
 * @improve wuwenjie wuwenjie.tk
 * @version 1.3.1
 * @more
 */
public class WebPreference extends PreferenceActivity {

	SharedPreferences sp;
	public static Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		con = this;

		sp = PreferenceManager.getDefaultSharedPreferences(this);

		sp.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {

			// sharedPreferences:���
			// key: �ı� ��ֵ
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				// TODO Auto-generated method stub

				if (key.equals("checkbox_preference_1")) {
					Boolean flag = sharedPreferences.getBoolean(
							"checkbox_preference_1", false);
					browser_main_view.instance.setBlockImage(flag);
					if (flag) {
						Toast.makeText(WebPreference.this, "关闭图片",
								Toast.LENGTH_LONG).show();
					} else {

					}
				} else if (key.equals("checkbox_preference_2")) {
					Boolean flag = sharedPreferences.getBoolean(
							"checkbox_preference_2", false);
					browser_main_view.instance.setCacheMode(flag);
					if (flag) {
						Toast.makeText(WebPreference.this, "启用缓存",
								Toast.LENGTH_LONG).show();
					} else {
					}
				} else if (key.equals("checkbox_preference_3")) {
					Boolean flag = sharedPreferences.getBoolean(
							"checkbox_preference_3", false);
					browser_main_view.instance.setJavaScript(flag);
					if (flag) {
						Toast.makeText(WebPreference.this, "启用javascript",
								Toast.LENGTH_LONG).show();
					} else {
					}
				}

			}

		});
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference.getKey() != null) {
			if (preference.getKey().equals("checkbox_preference_1")) {
				boolean flag = preference.getSharedPreferences().getBoolean(
						preference.getKey(), false);
				browser_main_view.instance.setBlockImage(flag);
			} else if (preference.getKey().equals("checkbox_preference_2")) {
				boolean flag = preference.getSharedPreferences().getBoolean(
						preference.getKey(), false);
				browser_main_view.instance.setCacheMode(flag);
			} else if (preference.getKey().equals("checkbox_preference_3")) {
				boolean flag = preference.getSharedPreferences().getBoolean(
						preference.getKey(), false);
				browser_main_view.instance.setJavaScript(flag);
			}
		}

		return super.onPreferenceTreeClick(preferenceScreen, preference);

	}

}