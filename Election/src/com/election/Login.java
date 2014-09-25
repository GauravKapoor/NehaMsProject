package com.election;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	
	Button sub;
	EditText VName, VUid, VDOB, VOtp;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sub = (Button)findViewById(R.id.lsubmit) ;
        VName=(EditText)findViewById(R.id.lvname);
        VUid=(EditText)findViewById(R.id.lvuid);
        VDOB=(EditText)findViewById(R.id.lvdob);
        VOtp=(EditText)findViewById(R.id.lvotp);
        sub.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				
				String votername = VName.getText().toString();
				
				String voteruid = VUid.getText().toString();
				
				String voterdob = VDOB.getText().toString();
				
				String voterotp = VOtp.getText().toString();
				
				String smsmessage = "VERIFICATION#uname:"+votername+"#uid:"+voteruid+"#dob:"+voterdob+"#otp:"+voterotp;
				
							           
				sendSMS(smsmessage);
				
			}
		});
		
        
    }
	
public void sendSMS(String message) {
		
		String encryptmsg="Test sms";
		
		try {
			byte[] req_data = TripleDES.reencrypt(message);
			encryptmsg=TripleDES.bytetostr(req_data);
			System.out.println("encryptmsg:"+encryptmsg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    String phoneNumber = "+31621504023";
	   
	    String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
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

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
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
	    
	    smsManager.sendTextMessage(phoneNumber, null, encryptmsg, sentPI, deliveredPI);
	   
	}

}
