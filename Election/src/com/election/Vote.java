package com.election;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Vote extends Activity {

	private RadioGroup rg;
	private RadioButton rb[];
	private String mylist[];
	public String serverdet;
	public String sec_key;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Intent myIntent = getIntent(); // gets the previously created intent
		String partyl = myIntent.getStringExtra("contender");
		// serverdet = myIntent.getStringExtra("serverinfo");
		Log.d("election", partyl);

		// ScrollView sv = new ScrollView(this);
		final LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.ballot));

		// sv.addView(ll);

		int index = partyl.indexOf("#");

		String partylist = partyl.substring(index + 1);

		String tempdata[] = partylist.split("@");

		mylist = tempdata[0].split("#");
		System.out.println(mylist);

		String temp[] = tempdata[1].split("#");
		sec_key = temp[0];
		Log.d("Secret Key", sec_key);

		rg = new RadioGroup(this);

		rb = new RadioButton[mylist.length];

		for (int i = 0; i < mylist.length; i++) {
			rb[i] = new RadioButton(getApplicationContext());
			rb[i].setTextColor(Color.BLACK);
			rb[i].setTextSize(16);
			rb[i].setText(mylist[i]);
			rg.addView(rb[i]);
		}

		ll.addView(rg);
		TextView txtv = new TextView(this);
		txtv.setText("PRN KEY:\n" + sec_key);
		txtv.setTextSize(30);
		txtv.setTextColor(Color.BLACK);
		txtv.setTypeface(Typeface.DEFAULT_BOLD);
		ll.addView(txtv);

		Button b = new Button(this);

		b.setText("Click Here to vote");
		b.setTextSize(20);
		b.setTypeface(Typeface.DEFAULT_BOLD);

		b.setTextColor(Color.GREEN);
		b.getBackground().setAlpha(200);
		ll.addView(b);

		b.setOnClickListener(new OnClickListener() {

			// @Override
			public void onClick(View v) {
				String str = "";
				for (int i = 0; i < mylist.length; i++) {
					if (rb[i].isChecked()) {
						String temp = (String) rb[i].getText();

						String symbol[] = temp.split(",");

						str = "VOTE#" + symbol[2] + "#" + sec_key;

					}

				}
				sendSMS(str);
				System.out.println("**************" + str + "**************");
				/*
				 * try { DatagramSocket sock = new DatagramSocket(9999); String
				 * request[] = serverdet.split("#");
				 * 
				 * String pad_res=TripleDES.padding(str);
				 * 
				 * byte[] req_data =
				 * TripleDES.encrypt(pad_res);//str.getBytes();
				 * 
				 * InetAddress ipaddr = InetAddress.getByName(request[1]); int
				 * port = Integer.parseInt(request[2]); DatagramPacket
				 * serverpacket = new DatagramPacket(req_data, req_data.length,
				 * ipaddr, port); sock.send(serverpacket); Log.d("Vote casted",
				 * str);
				 * 
				 * Toast.makeText(getApplicationContext(), str,
				 * Toast.LENGTH_LONG).show(); sock.close(); } catch (Exception
				 * e) { e.printStackTrace(); }
				 */
				Intent homeIntent = new Intent(Intent.ACTION_MAIN);
				homeIntent.addCategory(Intent.CATEGORY_HOME);
				homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(homeIntent);
			}
		});

		this.setContentView(ll);

		/*
		 * setContentView(R.layout.vote);
		 * 
		 * Intent myIntent= getIntent(); // gets the previously created intent
		 * String partylist = myIntent.getStringExtra("partylist"); // will
		 * return "FirstKeyValue"
		 * 
		 * 
		 * 
		 * rg = (RadioGroup) findViewById(R.id.radiogroup);//not this RadioGroup
		 * rg = new RadioGroup(this);
		 * rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
		 * for(int i=0; i<5; i++) { rb[i] = new RadioButton(this);
		 * rb[i].setTextColor(Color.BLACK); rb[i].setTextSize(16);
		 * rb[i].setText("Test"); rg.addView(rb[i]);
		 * 
		 * } Button b = new Button(this);
		 * 
		 * b.setText("Submit"); b.setTextSize(15); ll.addView(b)
		 */

		// Toast.makeText(getApplicationContext(), "message",
		// Toast.LENGTH_LONG).show();

	}

	public void sendSMS(String message) {

		String encryptmsg = "Test sms";

		try {
			byte[] req_data = TripleDES.reencrypt(message);
			encryptmsg = TripleDES.bytetostr(req_data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String phoneNumber = "+31621504023";

		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		@SuppressWarnings("deprecation")
		SmsManager smsManager = SmsManager.getDefault();

		smsManager.sendTextMessage(phoneNumber, null, encryptmsg, sentPI,
				deliveredPI);

	}

}
