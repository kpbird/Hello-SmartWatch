package com.kpbird.hellosmartwatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}
	
	public void buttonClicked(View v){
		if(v.getId() == R.id.btnSend){
			Intent serviceIntent = new Intent(this, HelloExtensionService.class);
	        serviceIntent.setAction(HelloExtensionService.INTENT_ACTION_ADD);
	        EditText etName = (EditText) findViewById(R.id.etTitle);
	        EditText etMsg = (EditText) findViewById(R.id.etMessage);
	        serviceIntent.putExtra("name", etName.getText().toString());
	        serviceIntent.putExtra("message", etMsg.getText().toString());
	        startService(serviceIntent);
		}
		else if(v.getId() == R.id.btnClearn){
			Intent serviceIntent = new Intent(this, HelloExtensionService.class);
	        serviceIntent.setAction(HelloExtensionService.INTENT_ACTION_CLEAR);
	        startService(serviceIntent);
		}
		
	}
}
