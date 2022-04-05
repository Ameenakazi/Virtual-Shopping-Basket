package com.example.nfc_shopping;

import com.example.connectivity.connectionManager;
import com.example.data.fillcust_Request;
import com.example.util.progressdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity
{
	Dialog dg;
	int resp;
	Context context;
	
	public static SharedPreferences myPreferences,sharedPreferences;
	SharedPreferences.Editor myEditor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		myPreferences=getSharedPreferences("NFCShopping", Context.MODE_PRIVATE);
		myEditor=myPreferences.edit();
		
		String PhoneNo=myPreferences.getString("PhoneNo", "");
		String Password=myPreferences.getString("Password", "");

		if(!PhoneNo.isEmpty())
		{
			fillcust_Request.SetPhoneNo(PhoneNo);
			fillcust_Request.SetPassword(Password);
			
			StoredLogin();
		}
		
		Button btnLogin=(Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				
				LoginAct();
			}
		});

		Button btnRegister = (Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Login.this, Register.class);
				startActivity(intent);
			}
		});
		Button btnForget=(Button)findViewById(R.id.btnForgetPass);
		btnForget.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ForgetAct();
			}
		});
	}

	private void register()
	{

	}


	public void ForgetAct()
	{
		EditText txtPhoneNo=(EditText)findViewById(R.id.txtPhoneNo);
		String PhoneNo=txtPhoneNo.getText().toString().trim();
		if(PhoneNo.equals(""))
		{
			AlertDialog alert=new AlertDialog.Builder(this).create();
			alert.setTitle("Enter Details");
			alert.setMessage("Phone number is Mandatory");
			alert.show();
		}
		else
		{
			final connectionManager conn=new connectionManager();
			if(connectionManager.checkNetworkAvailable(this))
			{

				fillcust_Request.SetPhoneNo(PhoneNo);

				progressdialog dialog=new progressdialog();
				dg=dialog.createDialog(this);
				dg.show();

				Thread tthread=new Thread()
				{
					public void run()
					{
						if(conn.forgetPass())
						{
							resp=0;
						}
						else
						{
							resp=1;
						}
						hd2.sendEmptyMessage(0);
					}
				};
				tthread.start();

			}
			else
			{
				Toast.makeText(this, "Sorry no network access.", Toast.LENGTH_LONG).show();
			}

		}
	}

	Handler hd2=new Handler()
	{
		public void handleMessage(Message msg)
		{


			dg.cancel();
			switch (resp) {
				case 0:

					Toast.makeText(getApplicationContext(), "Password is sent to your registered phone number", Toast.LENGTH_LONG).show();
					break;

				case 1:
					Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
					break;
			}
		}
	};



	public void LoginAct()
	{
		EditText txtPhoneNo=(EditText)findViewById(R.id.txtPhoneNo);
		EditText txtPassword=(EditText)findViewById(R.id.txtPassword);
		
		String PhoneNo=txtPhoneNo.getText().toString().trim();
		String Password=txtPassword.getText().toString().trim();
		
		if(PhoneNo.equals("")||Password.equals(""))
		{
			AlertDialog alert=new AlertDialog.Builder(this).create();
			alert.setTitle("Enter All Details");
			alert.setMessage("All Fields Are Mandatory");
			alert.show();
		}
		else
		{
			
			final connectionManager conn=new connectionManager();
			if(connectionManager.checkNetworkAvailable(this))
			{
				
				fillcust_Request.SetPhoneNo(PhoneNo);
				fillcust_Request.SetPassword(Password);
				
				progressdialog dialog=new progressdialog();
				dg=dialog.createDialog(this);
				dg.show();
				
				Thread tthread=new Thread()
				{
					public void run()
					{
						if(conn.Login(Login.this))
						{
							resp=0;
						}
						else
						{
							resp=1;
						}
						hd.sendEmptyMessage(0);
					}
				};
				tthread.start();
				
			}
			else
			{
				Toast.makeText(this, "Sorry no network access.", Toast.LENGTH_LONG).show();
			}
			
		}
		
	}
	
	public void StoredLogin()
	{
		final connectionManager conn=new connectionManager();
		if(connectionManager.checkNetworkAvailable(this))
		{
			
			progressdialog dialog=new progressdialog();
			dg=dialog.createDialog(this);
			dg.show();
			
			Thread tthread=new Thread()
			{
				public void run()
				{
					if(conn.Login(Login.this))
					{
						resp=0;
					}
					else
					{
						resp=1;
					}
					hd.sendEmptyMessage(0);
				}
			};
			tthread.start();
			
		}
		else
		{
			Toast.makeText(this, "Sorry no network access.", Toast.LENGTH_LONG).show();
		}
	}
	
	Handler hd=new Handler()
	{
		public void handleMessage(Message msg)
		{
			EditText txtPhoneNo=(EditText)findViewById(R.id.txtPhoneNo);
			EditText txtPassword=(EditText)findViewById(R.id.txtPassword);
			
			dg.cancel();
			switch (resp) {
			case 0:
				
				myEditor.putString("PhoneNo", fillcust_Request.GetPhoneNo());
				myEditor.putString("Password", fillcust_Request.GetPassword());
				myEditor.commit();
				txtPhoneNo.setText("");
				txtPassword.setText("");
				
				Intent intent=new Intent(Login.this,Selection_.class);
				startActivity(intent);
				finish();
				break;

			case 1:
				Toast.makeText(getApplicationContext(), "Invalid Phone Number Or Password", Toast.LENGTH_LONG).show();
				txtPhoneNo.setText("");
				txtPassword.setText("");
				
				break;
			}
		}
	};

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

						Intent intent=new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						finish();

	}

}
