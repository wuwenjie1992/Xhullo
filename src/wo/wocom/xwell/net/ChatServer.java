package wo.wocom.xwell.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
/**
 * 
 * @author wuwenjie
 * @date 20130405
 * @version 1.3.10.3.18:3
 * @more Socket 编程
 * 
 */
public class ChatServer {

	public static void main(String[] args) {
		ServerThread st = new ServerThread();
		st.start();
		System.out.println("开始服务器端。。。");

		UDPRecThread ut = new UDPRecThread();
		ut.start();

	}// main

}

// UDP rec
class UDPRecThread extends Thread {

	private static final int UPort = 56085;

	public void run() {

		System.out.println("UDPRecThread");

		// udp 接收
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(UPort);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);

		try {
			ds.receive(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = new String(dp.getData(), 0, dp.getLength());
		System.out.println(str);
		System.out.println("IP:" + dp.getAddress().getHostAddress() + ",PORT:"
				+ dp.getPort());
		ds.close();

	}
}// UDPRecThread

// udp send 发送
class UDPSendThread extends Thread {

	InetAddress ia;
	int p;

	public UDPSendThread(InetAddress s, int p) {
		super();
		this.ia = s;
		this.p = p;
	}

	public void run() {

		System.out.println("UDPSendThread:" + ia + ":" + p);

		DatagramSocket ds2 = null;
		try {
			ds2 = new DatagramSocket();
		} catch (SocketException e) {

			e.printStackTrace();
		}

		String str2 = "I'm from server!!";
		DatagramPacket dp2 = null;
		byte[] buf = str2.getBytes();// 将数据转换成Byte类型

		dp2 = new DatagramPacket(buf, buf.length, ia, p);

		try {
			ds2.send(dp2);
		} catch (IOException e) {

			e.printStackTrace();
		}
		ds2.close(); // 关闭连接

	}
}

// Tcp rec 创建一个线程在后台监听
class ServerThread extends Thread {

	private static final int Port = 7777;
	ServerSocket serversocket = null;
	public boolean Respond;
	public InetAddress ria;
	public int rport;

	public void run() {

		try {
			// 创建一个serversocket对象，并让他在Port端口监听
			serversocket = new ServerSocket(Port);

			Socket socket = serversocket.accept();
			// Exception in thread "Thread-0" java.lang.NullPointerEp
			System.out.println("本地ip&端口" + socket.getLocalSocketAddress());

			while (true) {
				// 调用serversocket的accept()方法，接收客户端发送的请求
				socket = serversocket.accept();
				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));

				synchronized (ChatServer.class) {
					if (buffer != null) {
						this.Respond = true;
						this.ria = socket.getInetAddress();
						this.rport = socket.getPort();
						System.out.println("远程ip&端口:" + this.ria + ":"
								+ this.rport + " " + this.Respond);

						UDPSendThread ust = new UDPSendThread(this.ria,
								this.rport);
						ust.start();

						ServerTSThread st = new ServerTSThread(this.ria,
								this.rport, "tcpsend");
						st.start();

					}
				}
				// 读取数据
				String msg = buffer.readLine();
				// System.out.println(socket.getInetAddress());

				System.out.println("msg:" + msg);

			} // while end

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serversocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}// run
}// ServerThread

// TCP send
class ServerTSThread extends Thread {
	public InetAddress ria;
	int p;
	private String text;

	public ServerTSThread(InetAddress ria, int p, String text) {
		this.ria = ria;
		this.p = p;
		this.text = text;
	}

	public void run() {
		System.out.println("ServerTSThread");

		try {
			// 创建socket对象，指定服务器端地址和端口号
			Socket socket = new Socket(ria, p);
			socket.setKeepAlive(true);
			// 获取 Client 端的输出流
			PrintWriter pw = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())), true);
			// 填充信息
			pw.println(text);
			// this.port = socket.getPort();

			// 关闭
			socket.close();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
