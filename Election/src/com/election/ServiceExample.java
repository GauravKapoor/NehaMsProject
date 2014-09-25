package com.election;

import android.app.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.os.IBinder;

public class ServiceExample extends Service {

	public NetworkListener nwlisten;
	private Handler mMainHandler;

	private String serverdata;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.addAction(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		filter.addAction("your_action_strings"); // further more
		filter.addAction("your_action_strings"); // further more

		registerReceiver(reciever1, filter);

		// Toast.makeText(pm,"Service created",300);
		// nwlisten = new NetworkListener();

		Log.d("Service message", "Service Created");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service Destroy", 300);
		Log.d("Service message", "Service Destroy");
		unregisterReceiver(reciever1);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Toast.makeText(this, "Service LowMemory", 300);
		Log.d("Service message", "Service LowMemory");

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		Toast.makeText(this, "Service start", 300);
		Log.d("Service message", "Service start");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// Toast.makeText(context,"task perform in service",300);
		Log.d("Service message", "task perform in service");
		// ThreadDemo td = new ThreadDemo();
		// nwlisten.start();
		// td.start();

		return super.onStartCommand(intent, flags, startId);
	}

	private final BroadcastReceiver reciever1 = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
				// action for sms recieved
				System.out.println(" Recieved SMS from client");

				Bundle bundle = intent.getExtras(); // ---get the SMS message
													// passed
													// in---
				SmsMessage[] msgs = null;
				String msg_from = "";
				String msgBody = "";
				if (bundle != null) {
					// ---retrieve the SMS message received---
					try {
						Object[] pdus = (Object[]) bundle.get("pdus");
						msgs = new SmsMessage[pdus.length];

						for (int i = 0; i < msgs.length; i++) {
							msgs[i] = SmsMessage
									.createFromPdu((byte[]) pdus[i]);
							msg_from = msgs[i].getOriginatingAddress();
							msgBody += msgs[i].getMessageBody();

						}

					} catch (Exception e) {
						// Log.d("Exception caught",e.getMessage());
					}
				}
				Log.d("Message From", msg_from.trim());
				System.out
						.println("**********" + msg_from.trim() + "*********");
				Log.d("Message body", msgBody.trim());
				System.out.println("**********" + msgBody + "*********");
				if (msg_from.trim().contains("621504")) {
					Log.d("Message body", msgBody.trim());
					System.out.println("**********" + msgBody + "*********");
					String decryptedmsg = "Test sms";
					String regdecrypt="Register decrypt";
					try {
						byte[] encrypt_ct = TripleDES.strtobyte(msgBody.trim());
						decryptedmsg = TripleDES.redecrypt(encrypt_ct);
						Log.d("decryptedmsg", decryptedmsg);
						regdecrypt=TripleDES.decrypt(encrypt_ct);
						System.out.println("**********" + decryptedmsg + "*********");
					} catch (Exception e) {
						// TODO: handle exception
					}

					MMessage msg = new MMessage();
					if (decryptedmsg.startsWith("verify")) {

						msg.type = 1;
						
						
					} else if (decryptedmsg.startsWith("contenders")) {

						msg.type = 2;
						
					} else if(regdecrypt.startsWith("secretkey")){
						
						String part[] = regdecrypt.split("#");
						TripleDES.secretkey=part[1];
						System.out.println("Secret key:"+TripleDES.secretkey);
						
					}

					// "a,l,h#B,k,t#c,u,e#d,i,t@12345#12";//
					
					// votersinfo.trim();
					msg.smsdata = decryptedmsg;
					android.os.Message toMain = handler.obtainMessage();

					toMain.obj = msg;

					handler.sendMessage(toMain);
					
				}
			} else if (action
					.equals(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
				// action for phone state changed
				System.out.println(" action for phone state changed");
			}
		}
	};

	private class ThreadDemo extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				int i = 1;
				while (i < 4) {
					sleep(10 * 1000);
					Message myMessage = new Message();
					Bundle resBundle = new Bundle();
					resBundle.putString("status", "SUCCESS");
					myMessage.obj = resBundle;
					handler.sendMessage(myMessage);
					Log.d("Service message", "Handler message");
					i++;
				}
			} catch (Exception e) {
				e.getMessage();
			}
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			Toast.makeText(getApplicationContext(),
					"Message from handler class", Toast.LENGTH_LONG).show();

			MMessage sms = (MMessage) msg.obj;
			String data = sms.smsdata.trim();
			if (sms.type == 1) {
				Intent loginIntent = new Intent(getBaseContext(), Login.class);
				loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplication().startActivity(loginIntent);
			} else if(sms.type == 2) {
				Intent dialogIntent = new Intent(getBaseContext(), Vote.class);
				dialogIntent.putExtra("contender", data);

				dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplication().startActivity(dialogIntent);

			} else {
				
				Log.d("Guarav-1", "Inside SmsType Else");
				Intent displayIntent = new Intent(getBaseContext(), Smsdisplay.class);
				displayIntent.putExtra("recievedmessage", data);

				displayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplication().startActivity(displayIntent);
				
			}
			// showAppNotification();
		}
	};

	public class NetworkListener extends Thread {

		String serverip;
		int serverport = 5000;
		DatagramSocket sock;

		public void castvote(String vote) {

		}

		public void run() {

			try {

				sock = new DatagramSocket(serverport);

				Log.d("Myapp", "Trying to connect to server " + serverip);

				Log.d("Myapp", "connected to server");

				String request[] = serverdata.split("#");

				String message = "START#" + request[0];

				String pad_res = TripleDES.padding(message);

				byte[] req_data = TripleDES.encrypt(pad_res);// message.getBytes();

				InetAddress ipaddr = InetAddress.getByName(request[1]);
				int port = Integer.parseInt(request[2]);
				DatagramPacket serverpacket = new DatagramPacket(req_data,
						req_data.length, ipaddr, port);
				sock.send(serverpacket);
				Log.d("Myapp", "request Sent");

				while (true) {
					byte[] data = new byte[208];
					DatagramPacket recievedpacket = new DatagramPacket(data,
							data.length);
					sock.receive(recievedpacket);

					byte[] rec_byte = recievedpacket.getData();

					String votersinfo = TripleDES.decrypt(rec_byte);// new
																	// String(recievedpacket.getData());

					Log.d("Myapp", "waiting for messages");

					Log.d("Myapp", votersinfo);

					Log.d("Myapp", "recieved message");

					MMessage msg = new MMessage();

					msg.smsdata = votersinfo.trim();

					android.os.Message toMain = handler.obtainMessage();

					toMain.obj = msg;

					handler.sendMessage(toMain);

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
