package com.example.nfc_shopping;

import java.util.ArrayList;

import com.example.util.ImageLoadTask;
import com.example.nfc_shopping.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetails_Adapter extends ArrayAdapter<String> {

	private Activity context;
	private ArrayList<String> Product_Id;
	private ArrayList<String> ProductName;
	private ArrayList<String> ImagePath;
	private ArrayList<String> Quantity;
	private ArrayList<String> Total_Cost;
	
	public ProductDetails_Adapter(Activity context, 
			ArrayList<String> Product_Id,ArrayList<String> ProductName,ArrayList<String> ImagePath,ArrayList<String> Quantity,ArrayList<String> Total_Cost) {
		super(context, R.layout.activity_product_details__adapter,Product_Id);
		// TODO Auto-generated constructor stub
		
		this.context=context;
		this.Product_Id=Product_Id;
		this.ProductName=ProductName;
		this.ImagePath=ImagePath;
		this.Quantity=Quantity;
		this.Total_Cost=Total_Cost;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) 
	{
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.activity_product_details__adapter,null,true);
		//rowView.setTag(position);
		
		TextView txtProductName=(TextView)rowView.findViewById(R.id.txtProductName);
		TextView txtQuantity=(TextView)rowView.findViewById(R.id.txtQuantity);
		TextView txtTotalCost=(TextView)rowView.findViewById(R.id.txtTotal_Cost);
		ImageView image=(ImageView)rowView.findViewById(R.id.imageView1);
		
		//txtProductName.setTag(ProductName.get(position));
		txtProductName.setText(ProductName.get(position));
		txtQuantity.setText(Quantity.get(position));
		//txtQuantity.setTag(Quantity.get(position));
		txtTotalCost.setText(Total_Cost.get(position));
		//txtTotalCost.setTag(Total_Cost.get(position));
		image.setTag(ImagePath.get(position));
		
		new ImageLoadTask(ImagePath.get(position), image);
		
		new ImageLoadTask(ImagePath.get(position), image).execute();
		
		return rowView;
	}
}
