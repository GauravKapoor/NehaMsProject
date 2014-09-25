package com.election;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Smsdisplay extends Activity {

	
	TextView tview;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smsdisplay);
        Intent myIntent = getIntent(); 
        String smsmsg = myIntent.getStringExtra("recievedmessage");
        tview=(TextView)findViewById(R.id.messageview);
        
        if(smsmsg.startsWith("sskey")){
        	String parts[] = smsmsg.split("#");
        	TripleDES.secretkey=parts[1];
        }
       
		//serverdet = myIntent.getStringExtra("serverinfo");
		Log.d("election", smsmsg);
		tview.setText(smsmsg);
		

        
    }
	
	
}
