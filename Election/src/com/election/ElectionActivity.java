package com.election;

import android.app.Activity;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.os.Bundle;

public class ElectionActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViewById(R.id.start).getBackground().setAlpha(150);
        findViewById(R.id.stop).getBackground().setAlpha(150);
        findViewById(R.id.vregister).getBackground().setAlpha(150);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.vregister).setOnClickListener(this);
    }

    private Intent inetnt;
    //@Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.start:
            //new ServiceExample(this.getApplicationContext());
            inetnt=new Intent(this.getApplicationContext(),ServiceExample.class);
            
            startService(inetnt);
            
            Intent homeIntent= new Intent(Intent.ACTION_MAIN);
			homeIntent.addCategory(Intent.CATEGORY_HOME);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(homeIntent);
            
            break;
        case R.id.stop:
            
            inetnt=new Intent(this.getApplicationContext(),ServiceExample.class);
            stopService(inetnt);
            
            Intent homIntent= new Intent(Intent.ACTION_MAIN);
			homIntent.addCategory(Intent.CATEGORY_HOME);
			homIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(homIntent);
            break;
            
        case R.id.vregister:    
        	inetnt=new Intent(this.getApplicationContext(),ElectionCommissionActivity.class);
        	startActivity(inetnt);
        	break;
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        
    }
}