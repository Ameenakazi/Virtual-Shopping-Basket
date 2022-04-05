package com.example.nfc_shopping;

import com.example.connectivity.connectionManager;
import com.example.data.fillProduct_Request;
import com.example.util.ImageLoadTask;
import com.example.util.progressdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

public class Product_Details_Page extends Activity {

	Dialog dg;
	int resp;
	Context context;
	public static int a;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product__details__page);
		
		Intent intent=getIntent();
		String form=intent.getStringExtra("Form");
		if(form.equals("List"))
		{
			a=1;
		}
		else
		{
			a=2;
			Button btnDelete=(Button)findViewById(R.id.btnDelete);
			btnDelete.setVisibility(View.INVISIBLE);
		}
		
		TextView txtProductName=(TextView)findViewById(R.id.txtProductName);
		TextView txtCost=(TextView)findViewById(R.id.txtCost);
		final TextView txtQuantity=(TextView)findViewById(R.id.txtQuantity);
		
		txtProductName.setText(fillProduct_Request.GetProductName());
		txtCost.setText(fillProduct_Request.GetCost());
		txtQuantity.setText(fillProduct_Request.GetQuantity());
		
		String ImagePath=fillProduct_Request.GetImagePath();
		ImageView image=(ImageView)findViewById(R.id.imageView1);
		
		new ImageLoadTask(ImagePath, image);
		
		new ImageLoadTask(ImagePath, image).execute();
		
		
		NumberPicker NP=(NumberPicker)findViewById(R.id.numberPicker1);
	
		NP.setMaxValue(10);
		NP.setMinValue(1);
		NP.setWrapSelectorWheel(false);
		
		NP.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				
				
				txtQuantity.setText(String.valueOf(newVal));
			}
		});
		
		Button btnSave=(Button)findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				AddToCart();
			}
		});
		
		Button btnDelete=(Button)findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				DeleteProduct();
				
			}
		});
		
	}
	
	public void DeleteProduct() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                     switch (which) {
                     case DialogInterface.BUTTON_POSITIVE:
                            // Yes button clicked
                            //Toast.makeText(Product_Details_Page.this, "Yes Clicked",Toast.LENGTH_LONG).show();
                            
                            final connectionManager conn=new connectionManager();
                            if(connectionManager.checkNetworkAvailable(Product_Details_Page.this))
                            {
                            	progressdialog dialog1=new progressdialog();
                    			dg=dialog1.createDialog(Product_Details_Page.this);
                    			dg.show();	
                    			
                    			Thread tthread=new Thread()
                    			{
                    				public void run()
                    				{
                    					conn.DeleteProduct();
                    					hd2.sendEmptyMessage(0);
                    				}
                    			};
                    			tthread.start();
                            }
                            else
                            {
                            	Toast.makeText(Product_Details_Page.this, "Sorry no network access.", Toast.LENGTH_LONG).show();
                            }
                            
                            
                            break;

                     case DialogInterface.BUTTON_NEGATIVE:
                            // No button clicked
                            // do nothing
                            Toast.makeText(Product_Details_Page.this, "Canceled",
                                          Toast.LENGTH_LONG).show();
                            break;
                     }
               }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure for delete product from cart?")
                     .setPositiveButton("Yes", dialogClickListener)
                     .setNegativeButton("No", dialogClickListener).show();
	}
	
	
	public void AddToCart()
	{
		TextView txtQuantity=(TextView)findViewById(R.id.txtQuantity);
		String SelectedQuantity=txtQuantity.getText().toString();
		fillProduct_Request.SetSelectedQuantity(SelectedQuantity);
		
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
					conn.AddToCart();
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
	
	public Handler hd=new Handler()
	{
		public void handleMessage(Message msg)
		{
			dg.cancel();
			Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_LONG).show();
			Intent intent=new Intent(Product_Details_Page.this,Selection_.class);
			startActivity(intent);
			finish();
		}
	};
	
	public Handler hd2=new Handler()
	{
		public void handleMessage(Message msg)
		{
			dg.cancel();
			Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_LONG).show();
			Intent intent=new Intent(Product_Details_Page.this,Selection_.class);
			startActivity(intent);
			finish();
		}
	};
	
	public void onBackPressed() 
	{
		if(a==1)
		{
			Intent intent=new Intent(Product_Details_Page.this,Customer_Cart.class);
			startActivity(intent);
			finish();
		}
		else
		{
			Intent intent=new Intent(Product_Details_Page.this,Selection_.class);
			startActivity(intent);
			finish();
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product__details__page, menu);
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
