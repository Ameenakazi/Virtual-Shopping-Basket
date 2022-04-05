package com.example.nfc_shopping;

import java.util.ArrayList;

import com.example.connectivity.connectionManager;
import com.example.data.CartDetails_Request;
import com.example.data.fillProduct_Request;
import com.example.data.fillcust_Request;
import com.example.util.progressdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NFC_Page extends Activity {

	Dialog dg;
	int resp;
	Context context;
	
	private NfcAdapter mNfCAdapter;
	boolean isNFCReadFlag=false; 
	boolean isHandleIntent=false;
	 
	public static SharedPreferences myPreferences;
	SharedPreferences.Editor myEditor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc__page);
		
		myPreferences=getSharedPreferences("NFCShopping", Context.MODE_PRIVATE);
		myEditor=myPreferences.edit();
		
		TextView txtCustname=(TextView)findViewById(R.id.lblCustName);
		TextView lblBalance=(TextView)findViewById(R.id.lblBalance);
		txtCustname.setText(fillcust_Request.GetCustName());
		lblBalance.setText("Rs."+fillcust_Request.GetBalance());
		
		handleIntent(getIntent());
		
		Button btn=(Button)findViewById(R.id.button1);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				GetProductDetails();
				
			}
		});
		
		Button btnSeeCart=(Button)findViewById(R.id.btnSeeCart);
		btnSeeCart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				SeeCart();
				
			}
		});
		
		Button btnLogout=(Button)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				myEditor.putString("PhoneNo","");
				myEditor.putString("Password","");
				myEditor.commit();
				
				Intent intent=new Intent(NFC_Page.this,Login.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	public void SeeCart()
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
					conn.SeeCart();
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
	
	public void GetProductDetails()
	{
		EditText txtProductId=(EditText)findViewById(R.id.editText1);
		String Product_Id=txtProductId.getText().toString();
		fillProduct_Request.SetProduct_Id(Product_Id);
		
		progressdialog dialog=new progressdialog();
		dg=dialog.createDialog(this);
		dg.show();
		
		final connectionManager conn=new connectionManager();
		
		Thread tthread =new Thread()
		{
			public void run()
			{
				conn.GetProductDetails();
				hd.sendEmptyMessage(0);
			}
		};
		tthread.start();
	}
	
	
	private void handleIntent(Intent intent)
	{
		String action=intent.getAction();
		if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
    	{
			String type=intent.getType();
    		Object MIME_TEXT_PLAIN = "text/plain";
    		if (MIME_TEXT_PLAIN.equals(type)) 
    		{
    			 Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
 	             String[] test= tag.getTechList();
	             Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	             if (rawMsgs != null) 
	             {
	            	 //TextView txt=(TextView)findViewById(R.id.textView1);
	            	 //txt.setText("NFC ticket detected.");
	            	 isNFCReadFlag=true;
	                 NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
	                 for (int i = 0; i < rawMsgs.length; i++)
	                 {
	                        msgs[i] = (NdefMessage) rawMsgs[i];
	                 }
	                 NdefMessage ndefmsg=msgs[0];
	                 NdefRecord[] records= ndefmsg.getRecords();
	                 NdefRecord currRecord=records[0];
	                 byte[] payload=currRecord.getPayload();
	                 byte[] id=currRecord.getId();
	                 Log.d("ID", id.toString());
	                 int encoding=payload[0] & 128;
	                 
	                 // Get the Text Encoding
	                 String textEncoding = (encoding == 0) ? "UTF-8" : "UTF-16";
	                 
	                 // Get the Language Code
	                    int languageCodeLength = payload[0] & 0063;
	                    
	                    try
	                    {
	                    	
	                    	final String ProductId=new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
	                    	
	                    	fillProduct_Request.SetProduct_Id(ProductId);
	                    	
	                    	String PhoneNo=myPreferences.getString("PhoneNo", "");
	                    	
	                    	if(!PhoneNo.isEmpty())
	                    	{
	                    	
	                    	final connectionManager conn=new connectionManager();
	                    	if(connectionManager.checkNetworkAvailable(this))
	                    	{
	                    		progressdialog dialog=new progressdialog();
								dg=dialog.createDialog(this);
								dg.show();	
								isHandleIntent=true;
								
								Thread tthread=new Thread()
								{
									public void run()
									{
										conn.GetProductDetails();
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
	                    	else
	                    	{
	                    		Intent intent2=new Intent(NFC_Page.this,Login.class);
	                    		startActivity(intent2);
	                    		finish();
	                    	}
	                    }
	                    catch(Exception e)
	                    {
	                    	e.printStackTrace();
	                    }
	             }
	        
    		}
    	}
	}
	
	public Handler hd=new Handler()
	{
		public void handleMessage(Message msg)
		{
			dg.cancel();
			Intent intent=new Intent(NFC_Page.this,Product_Details_Page.class);
			intent.putExtra("Form", "NFC");
			startActivity(intent);
			finish();
		}
	};
	
	public Handler hd2=new Handler()
	{
		public void handleMessage(Message msg)
		{
			dg.cancel();
			final ArrayList<String> Product_Id;
			Product_Id=CartDetails_Request.GetProduct_Ids();
			if(Product_Id.isEmpty())
			{
				AlertDialog alert=new AlertDialog.Builder(NFC_Page.this).create();
				alert.setTitle("No Record Found");
				alert.setMessage("No Cart Details found");
				alert.show();

			}
			else
			{
			Intent intent=new Intent(NFC_Page.this,Customer_Cart.class);
			startActivity(intent);
			finish();
			}
		}
	};
	
	public void onBackPressed() 
	{
		Intent intent=new Intent(NFC_Page.this,Selection_.class);
		startActivity(intent);
		finish();
	}
	

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nfc__page, menu);
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
}
