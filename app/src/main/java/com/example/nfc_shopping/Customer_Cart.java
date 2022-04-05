package com.example.nfc_shopping;

import java.util.ArrayList;

import com.example.connectivity.connectionManager;
import com.example.data.CartDetails_Request;
import com.example.data.fillProduct_Request;
import com.example.nfc_shopping.R;
import com.example.util.progressdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class Customer_Cart extends Activity {

	Dialog dg;
	int resp;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer__cart);
		
		
		final ArrayList<String> Product_Id;
		Product_Id=CartDetails_Request.GetProduct_Ids();
		
		final ArrayList<String> ProductName;
		ProductName=CartDetails_Request.GetProductNames();
		
		final ArrayList<String> ImagePath;
		ImagePath=CartDetails_Request.GetImagePaths();
		
		final ArrayList<String> Quantity;
		Quantity=CartDetails_Request.GetQuantitys();
		
		final ArrayList<String> Total_Cost;
		Total_Cost=CartDetails_Request.GetTotal_Costs();
		
		final ArrayList<String> OverallCost;
		OverallCost=CartDetails_Request.GetOverallCost();
		
		
		TextView txtAmount=(TextView)findViewById(R.id.txtAmount);
		
		String cartcost=OverallCost.get(0);
		txtAmount.setText("Rs."+cartcost);
		
		/*if(Product_Id.isEmpty())
		{
			AlertDialog alert=new AlertDialog.Builder(this).create();
			alert.setTitle("No Record Found");
			alert.setMessage("No Prescriptions found");
			alert.show();
			
		}*/
		
		ProductDetails_Adapter adapter=new ProductDetails_Adapter(Customer_Cart.this, Product_Id, ProductName, ImagePath, Quantity, Total_Cost);
		ListView lst=(ListView)findViewById(R.id.listView1);
		lst.setAdapter(adapter);
		
		lst.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				String selectedProduct_Id=Product_Id.get(position);
				fillProduct_Request.SetProduct_Id(selectedProduct_Id);
				
				GetProductDetails();
			}
		});
	}
	
	public void GetProductDetails()
	{
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
	
	public Handler hd=new Handler()
	{
		public void handleMessage(Message msg)
		{
			dg.cancel();
			Intent intent=new Intent(Customer_Cart.this,Product_Details_Page.class);
			intent.putExtra("Form", "List");
			startActivity(intent);
			finish();
		}
	};
	
	public void onBackPressed() 
	{
		Intent intent=new Intent(Customer_Cart.this,Selection_.class);
		startActivity(intent);
		finish();
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.customer__cart, menu);
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
