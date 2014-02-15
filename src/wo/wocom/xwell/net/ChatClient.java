package wo.wocom.xwell.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import wo.wocom.xwell.R;
import wo.wocom.xwell.utility.XA_util_ADialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author wuwenjie
 * @date 20130405
 * @version 1.3.10.3.18:5
 * @more Socket 编程
 * 
 */
public class ChatClient extends Activity {

	private static String IpAddress, imei, localIP = null;//
	private static int TSend_Port = 6777; // tcp 发送的 服务器目标口
	private static int TListen_Port = 6778; // tcp 监听目标口
	int UDP_PORT = 56085; // UDP 端口
	private EditText sendtext = null;
	private Button send = null;
	private TextView showMesg = null;
	AlertDialog.Builder et_dialog;

	Socket socket = null;
	ServerSocket serversocket = null;
	public String sendMesg = null;

	TCPSendThread SendThd; // TCP 发送 线程
	TCPRecvThread trt; // TCP 接收
	UDPRecvThread urt; // UDP 接收
	UDPSendThread ust; // UDP 发送

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pac_net_cc);

		sendtext = (EditText) findViewById(R.id.pac_net_cc_et1);
		send = (Button) findViewById(R.id.pac_net_cc_bt1);
		showMesg = (TextView) findViewById(R.id.pac_net_cc_tv1);

		imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
				.getDeviceId();
		// DeviceId IMEI

		// 获得本机地址
		localIP = isConnectNet.getIPAddress(true);
		setTitle("Local IP:" + localIP);

		writeIP_SendTU();// 填写发送地址

		// send 按钮 监听,TCP发送消息
		send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				sendMesg = sendtext.getText().toString();
				TCPsendMsg(sendMesg);
				showMesg.append("\nSEND:" + sendMesg);

				sendMesg = null;
				sendtext.setText("");

			}
		});// bt end

		UDPRecvMsg();

		trt = new TCPRecvThread();
		trt.start();

	}// onCreate

	// TCP 发送
	public void TCPsendMsg(String text) {

		SendThd = new TCPSendThread(text);
		SendThd.start();

	}

	// UDP 接收
	public void UDPRecvMsg() {

		urt = new UDPRecvThread();
		urt.start();
		showMesg.append("\nRECV:" + urt.Recvstr);

	}

	// UDP 发送
	public void UDPSendMsg(String s) {
		ust = new UDPSendThread(s);
		ust.start();
	}

	// ///////////----------线程--------------//////////////

	// 自定义 TCP 发送信息 线程
	class TCPSendThread extends Thread {

		private String text;

		// public int port;

		public TCPSendThread(String t) {
			super();
			text = t;
		}

		// public void getTCPP() {
		// UDP_PORT = this.port;
		// }

		public void run() {

			try {
				// 创建socket对象，指定服务器端地址和端口号
				socket = new Socket(IpAddress, TSend_Port);
				socket.setKeepAlive(true);
				// 获取 Client 端的输出流
				PrintWriter pw = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				// 填充信息
				pw.println(text);
				// this.port = socket.getPort();

				Log.i("TCPSendThread", "activeCount:" + activeCount());
				// 关闭
				// socket.close();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
				Log.i("TCPSendThread", "UnknownHostException");
				showMesg.append("UnknownHost");
			} catch (IOException e1) {
				e1.printStackTrace();
				Log.i("TCPSendThread", "IOException");
				// showMesg.append("\nSys Err:IOException"+e1.getLocalizedMessage()+"\n");
				// android.view.ViewRoot$CalledFromWrongThreadException:
				// Only the original thread that created a view hierarchy can
				// touch its views.

				// 需要Handler来【更新、通知】UI线程
			}
		}
	}// SendThread

	// 自定义 TCP 接收 线程
	class TCPRecvThread extends Thread {

		public void run() {
			// 创建一个serversocket对象，并让他在Port端口监听
			try {
				serversocket = new ServerSocket(TListen_Port);
				while (true) {

					Socket socket_tr = serversocket.accept();

					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(socket_tr.getInputStream()));

					// 读取数据
					String msg = buffer.readLine();
					Log.i("TCPRecvThread", msg);
					// serversocket.close();
				} // while end
			} catch (IOException e) {

				e.printStackTrace();
			}

		}// run end

	}

	// 自定义 UDP 接收 线程
	class UDPRecvThread extends Thread {

		String Recvstr;

		public UDPRecvThread() {
			super();

		}

		public void run() {
			DatagramSocket ds = null;

			try {
				Log.i("UDPRecvThread", "UDP_PORT=" + UDP_PORT);
				ds = new DatagramSocket(UDP_PORT);
				// 创建一个DatagramSocket实例，并将该对象绑定到本机默认IP地址、指定端口。
			} catch (SocketException e) {
				e.printStackTrace();
			}

			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			// 以一个空数组来创建DatagramPacket对象，
			// 该对象的作用是接收DatagramSocket中的数据

			try {
				ds.receive(dp);
				// 从该DatagramSocket中接收数据报
			} catch (IOException e) {
				e.printStackTrace();
			}
			String str = new String(dp.getData(), 0, dp.getLength());
			// getData返回DatagramPacket对象里封装的字节数组

			Log.i("UDPRecvThread", str);
			Log.i("UDPRecvThread", "IP:" + dp.getAddress().getHostAddress()
					+ ",PORT:" + dp.getPort());
			// InetAddress getAddress() 返回某台机器的 IP
			// 地址，当程序准备发送次数据报时，该方法返回此数据报的目标机器的IP地址；
			// 当程序刚刚接收到一个数据报时，该方法返回该数据报的发送主机的IP地址。

			// int getPort()：返回某台机器的端口，当程序准备发送此数据报时，
			// 该方法返回此数据报的目标机器的端口；当程序刚刚接收到一个数据报时，
			// 该方法返回该数据报的发送主机的端口。

			ds.close();

			// 共享数据，需要进行同步控制。
			// 该对象在任意时刻只能由一个线程访问
			synchronized (UDPRecvThread.class) {
				Recvstr = str;
			}

		}// run end
	} // UDPRecvThread end

	// 自定义 UDP 发送 线程
	class UDPSendThread extends Thread {

		String UDPSendstr;

		public UDPSendThread(String s) {
			super();
			this.UDPSendstr = s;
		}

		public void run() {
			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket();
			} catch (SocketException e1) {
				e1.printStackTrace();
			}

			DatagramPacket dp = null;

			try {
				dp = new DatagramPacket(UDPSendstr.getBytes(),
						UDPSendstr.length(), InetAddress.getByName(IpAddress),
						UDP_PORT);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			try {
				ds.send(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Log.i("UDPSendThread", "L:" + ds.getLocalSocketAddress() + " R:"
					+ ds.getRemoteSocketAddress());

			ds.close(); // 关闭连接
		}

	}// UDPSendThread

	// 自定义 stop thread
	public void stopThread() {
		if (SendThd.isAlive())
			SendThd.stop();
		if (trt.isAlive())
			trt.stop();
		if (urt.isAlive())
			urt.stop();
		if (ust.isAlive())
			ust.stop();

	}

	// ////////////------生命周期-------

	protected void onDestroy() {

		super.onDestroy();
		stopThread();
	}

	protected void onPause() {

		super.onPause();
		try {
			socket.close();
			serversocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		stopThread();

	}

	protected void onRestart() {

		super.onRestart();

	}

	protected void onResume() {

		super.onResume();
	}

	protected void onStart() {

		super.onStart();
	}

	protected void onStop() {

		super.onStop();
		stopThread();
	}

	// 自定义 填写IP
	public String writeIP_SendTU() {
		// 引用视图组件
		LayoutInflater inflater = (LayoutInflater) ChatClient.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		// View&xml关联
		final View et_dialog_v = inflater.inflate(R.layout.et_dialog, null);

		// ip address 对话框
		et_dialog = new AlertDialog.Builder(ChatClient.this); // 实例化
		et_dialog.setTitle("服务器地址").setView(et_dialog_v);

		et_dialog.setPositiveButton("ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

						// xml中的控件
						EditText et = (EditText) et_dialog_v
								.findViewById(R.id.pac_db_ed_et);

						TextView tv = (TextView) et_dialog_v
								.findViewById(R.id.pac_db_ed_tv);

						tv.setText("输入服务器地址");

						CharSequence edit_value = et.getText();
						arg0.dismiss();
						IpAddress = edit_value.toString();
						Log.i("onOptionsItemSelected", IpAddress);
						// it.start();

						// TCPsendMsg("Comming T " + imei);// TCP 发送
						UDPSendMsg("Comming U " + imei);// UDP 发送

					}// onClick
				});

		et_dialog.setNegativeButton("no",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						ChatClient.this.finish(); // 退出
					}
				});

		et_dialog.create().show();

		return IpAddress;

	}

	/* 菜单制作 */
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "ip info").setIcon(

		android.R.drawable.ic_menu_myplaces);

		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case Menu.FIRST + 1:

			XA_util_ADialog alog = new XA_util_ADialog(ChatClient.this);
			alog.show1ADialog("ip info", IpAddress + ":" + localIP + ":"
					+ TSend_Port + ":" + UDP_PORT, "好的");

			break;

		}

		return true;
	}

}// all end 