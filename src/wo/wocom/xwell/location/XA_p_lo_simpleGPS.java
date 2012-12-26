package wo.wocom.xwell.location;

import wo.wocom.xwell.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.2
 * @more GPS信息
 */
public class XA_p_lo_simpleGPS extends Activity {

	LocationManager locationManager;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pac_lo_simplegps);

		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(serviceName);
		// String provider = LocationManager.GPS_PROVIDER;

		// 判断GPS是否正常启动
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
			// 返回开启GPS导航设置界面
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, 0);
			return;
		}

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);

		String provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);
		updateWithNewLocation(location);
		locationManager.requestLocationUpdates(provider, 2000, 10,
				locationListener);
	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	private void updateWithNewLocation(Location location) {
		String latLongString;
		TextView myLocationText;
		myLocationText = (TextView) findViewById(R.id.pac_lo_sg_tv);
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			double alt = location.getAltitude();
			float bea = location.getBearing();
			String pro = location.getProvider();
			float spe = location.getSpeed();
			long tim = location.getTime();
			// getElapsedRealtimeNanos();
			/*
			 * Bundle getExtras() Returns additional provider-specific
			 * information about the location fix as a Bundle.
			 */

			latLongString = "纬度:" + lat + "\n经度:" + lng + "\n海拔:" + alt
					+ "\n方位:" + bea + "\n速度:" + spe + "\n时间:" + tim + "\n供应商:"
					+ pro;

		} else {
			latLongString = "无法获取地理信息";
		}
		myLocationText.setText("您当前的位置是:\n" + latLongString);
	}

	//生命周期
	protected void onDestroy() {
		super.onDestroy();
		locationManager.removeUpdates(locationListener);
	}
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(locationListener);
	}
	protected void onRestart() {
		super.onRestart();
		Toast.makeText(this, "simpleGPS onRestart", Toast.LENGTH_SHORT).show();
		// initCriteria(criteria);
		// locationManager.requestLocationUpdates(provider, 2000, 10,
		// locationListener);
	}
	protected void onResume() {
		super.onResume();
		Toast.makeText(this, "simpleGPS onResume", Toast.LENGTH_SHORT).show();
		// initCriteria(criteria);
		// locationManager.requestLocationUpdates(provider, 2000, 10,
		// locationListener);
	}
	protected void onStop() {
		super.onStop();
		locationManager.removeUpdates(locationListener);

	}

	/**
	 * LocationListener
	 * 
	 * 译者署名：熊猫82
	 * 
	 * 译者链接：http://wisekingokok.cnblogs.com
	 * 
	 * 版本：Android 3.2 r1
	 * 
	 * 
	 * 
	 * 结构
	 * 
	 * 继承关系
	 * 
	 * public interface LocationListener
	 * 
	 * 
	 * 
	 * android.location.LocationListener
	 * 
	 * 
	 * 
	 * 类概述
	 * 
	 * 用于接收从LocationManager的位置发生改变时的通知。如果LocationListener被注册添加到LocationManager对象
	 * ，并且此LocationManager对象调用了requestLocationUpdates(String, long, float,
	 * LocationListener)方法，那么接口中的相关方法将会被调用
	 * 
	 * 
	 * 
	 * 公共方法
	 * 
	 * public abstract void onLocationChanged (Location location)
	 * 
	 * 此方法在当位置发生改变后被调用。这里可以没有限制的使用Location对象。
	 * 
	 * 参数
	 * 
	 * 位置发生变化后的新位置。
	 * 
	 * 
	 * 
	 * public abstract void onProviderDisabled(String provider)
	 * 
	 * 此方法在provider被用户关闭后被调用，如果基于一个已经关闭了的provider调用requestLocationUpdates方法被调用，
	 * 那么这个方法理解被调用。
	 * 
	 * 参数
	 * 
	 * 与之关联的location provider名称。
	 * 
	 * 
	 * 
	 * public abstract void onPorviderEnabled (Location location)
	 * 
	 * 此方法在provider被用户开启后调用。
	 * 
	 * 参数
	 * 
	 * provider 与之关联的location provider名称。
	 * 
	 * 
	 * 
	 * public abstract void onStatusChanged (String provider, int Status, Bundle
	 * extras)
	 * 
	 * 此方法在Provider的状态在可用、暂时不可用和无服务三个状态直接切换时被调用。
	 * 
	 * 参数
	 * 
	 * provider 与变化相关的location provider名称。
	 * 
	 * status 如果服务已停止，并且在短时间内不会改变，状态码为OUT_OF_SERVICE；如果服务暂时停止，并且在短时间内会恢复，
	 * 状态码为TEMPORARILY_UNAVAILABLE；如果服务正常有效，状态码为AVAILABLE。
	 * 
	 * extras 一组可选参数，其包含provider的特定状态。下面列出一组共用的键值对，其实任何键的provider都需要提供的值。•定位卫星 –
	 * 一组用于设备定位的卫星
	 */

	
}