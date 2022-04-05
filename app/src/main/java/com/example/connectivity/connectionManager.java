package com.example.connectivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.data.CartDetails_Request;
import com.example.data.UsersData;
import com.example.data.fillProduct_Request;
import com.example.data.fillcust_Request;
import com.example.nfc_shopping.Customer_Cart;
import com.example.nfc_shopping.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static com.example.data.UsersData.name;

public class connectionManager 
{
	
	String ServiceUrl="http://my-demo.in/N_Shopping_Service/Service1.svc";
	SharedPreferences sharedPreferences;
	
	public static boolean checkNetworkAvailable(Context context)
	{
		try {
			ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo(); 
			return activeNetworkInfo != null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
	}
	
	public boolean Login(Context context)
	{
		String responseString;
		try
		{
			final String TAG_C_Id="C_Id";
			final String TAG_CustName="CustName";
			final String TAG_Balance="Balance";
			
			//String url=String.format("http://my-demo.in/NFC_Shopping_Service_New/Service1.svc/Login/"+fillcust_Request.GetPhoneNo()+"/"+fillcust_Request.GetPassword());
			String url=String.format(ServiceUrl+"/Login/"+fillcust_Request.GetPhoneNo()+"/"+fillcust_Request.GetPassword());
			HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(url));
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
		    {
		    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        responseString = out.toString();
		        
		        JSONObject jsonObj=new JSONObject(responseString);
		        String C_Id=jsonObj.getString(TAG_C_Id);
		        String CustName=jsonObj.getString(TAG_CustName);
		        String Balance=jsonObj.getString(TAG_Balance);
		        
		        if(CustName!="null")
		        {
		        	fillcust_Request.SetC_Id(C_Id);
		        	fillcust_Request.SetCustName(CustName);
		        	fillcust_Request.SetBalance(Balance);

					sharedPreferences =  context.getSharedPreferences(SharedPreference.loginPref, 0);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("name",CustName);
					editor.putString("balance",Balance);
					editor.commit();

		        	return true;
		        }
		        else
		        {
		        	return false;
		        }
		    }
		    else
		    {
		    	return false;
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public void GetProductDetails()
	{
		String responseString;
		try
		{
			final String TAG_Product_Id="Product_Id";
			final String TAG_ProductName="ProductName";
			final String TAG_Cost="Cost";
			final String TAG_ImagePath="ImagePath";
			final String TAG_Quantity="Quantity";
			
			String url=String.format(ServiceUrl+"/GetProductDetails/"+fillcust_Request.GetC_Id()+"/"+fillProduct_Request.GetProduct_Id());
			HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(url));
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
		    {
		    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        responseString = out.toString();
		        
		        JSONObject jsonObj=new JSONObject(responseString);
		        String Product_Id=jsonObj.getString(TAG_Product_Id);
		        String ProductName=jsonObj.getString(TAG_ProductName);
		        String Cost=jsonObj.getString(TAG_Cost);
		        String ImagePath=jsonObj.getString(TAG_ImagePath);
		        String Quantity=jsonObj.getString(TAG_Quantity);
		        
		        fillProduct_Request.SetProduct_Id(Product_Id);
		        fillProduct_Request.SetProductName(ProductName);
		        fillProduct_Request.SetCost(Cost);
		        //fillProduct_Request.SetImagePath("http://my-demo.in/NFC_Shopping_Web_New/"+ImagePath);
		        fillProduct_Request.SetImagePath("http://my-demo.in/N_Shopping_Web/"+ImagePath);
		        fillProduct_Request.SetQuantity(Quantity);
		    }
		    else
		    {
		    	response.getEntity().getContent().close();
		        throw new IOException(statusLine.getReasonPhrase());
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void AddToCart()
	{
		String responseString;
		try
		{
			final String TAG_RESULT="Result";
			
			String url=String.format(ServiceUrl+"/AddToCart/"+fillcust_Request.GetC_Id()+"/"+fillProduct_Request.GetProduct_Id()+"/"+fillProduct_Request.GetSelectedQuantity());
			HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(url));
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
		    {
		    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        responseString = out.toString();
		        
		        JSONObject jsonObj=new JSONObject(responseString);
		        String Result=jsonObj.getString(TAG_RESULT);
		        
		    }
		    else
		    {
		    	response.getEntity().getContent().close();
		        throw new IOException(statusLine.getReasonPhrase());
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void DeleteProduct()
	{
		String responseString;
		try
		{
			final String TAG_RESULT="Result";
			
			String url=String.format(ServiceUrl+"/DeleteProduct/"+fillcust_Request.GetC_Id()+"/"+fillProduct_Request.GetProduct_Id());
			HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(url));
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
		    {
		    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        responseString = out.toString();
		        
		        JSONObject jsonObj=new JSONObject(responseString);
		        String Result=jsonObj.getString(TAG_RESULT);
		    }
		    else
		    {
		    	response.getEntity().getContent().close();
		        throw new IOException(statusLine.getReasonPhrase());
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void SeeCart()
	{
		 String responseString;
		 JSONArray newJsonArray;
		 ArrayList<String> stringArray;
		 ArrayList<String> stringarray1;
		 ArrayList<String> stringarray2;
		 ArrayList<String> stringarray3;
		 ArrayList<String> stringarray4;
		 ArrayList<String> stringarray5;
		 
		 try
		 {
			 final String TAG_Product_Id="Product_Id";
			 final String TAG_ProductName="ProductName";
			 final String TAG_ImagePath="ImagePath";
			 final String TAG_Quantity="Quantity";
			 final String TAG_Total_Cost="Total_Cost";
			 final String TAG_OverallCost="OverallCost";
			 
			 String url=String.format(ServiceUrl+"/SeeCart/"+fillcust_Request.GetC_Id());
				HttpClient httpclient = new DefaultHttpClient();
			    HttpResponse response = httpclient.execute(new HttpGet(url));
			    StatusLine statusLine = response.getStatusLine();
			    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
			    {
			    	ByteArrayOutputStream out = new ByteArrayOutputStream();
			        response.getEntity().writeTo(out);
			        out.close();
			        responseString = out.toString();
			        
			        newJsonArray=new JSONArray(responseString);
				    stringArray=new ArrayList<String>(newJsonArray.length());
				    stringarray1=new ArrayList<String>(newJsonArray.length());
				    stringarray2=new ArrayList<String>(newJsonArray.length());
				    stringarray3=new ArrayList<String>(newJsonArray.length());
				    stringarray4=new ArrayList<String>(newJsonArray.length());
				    stringarray5=new ArrayList<String>(newJsonArray.length());
				    
				    for(int i=0;i<newJsonArray.length();i++)
				     {
				    	 JSONObject jsonObj = newJsonArray.getJSONObject(i);
				    	 String Product_Id=jsonObj.optString(TAG_Product_Id);
				    	 String Product_Name=jsonObj.optString(TAG_ProductName);
				    	 String ImagePath=jsonObj.optString(TAG_ImagePath);
				    	 String Quantity=jsonObj.optString(TAG_Quantity);
				    	 String Total_Cost=jsonObj.optString(TAG_Total_Cost);
				    	 String OverallCost=jsonObj.optString(TAG_OverallCost);
				    	 
				    	 stringArray.add(Product_Id);
				    	 stringarray1.add(Product_Name);
				    	 //stringarray2.add("http://my-demo.in/NFC_Shopping_Web_New/"+ImagePath);
				    	 stringarray2.add("http://my-demo.in/N_Shopping_Web/"+ImagePath);
				    	 stringarray3.add(Quantity);
				    	 stringarray4.add(Total_Cost);
				    	 stringarray5.add(OverallCost);
				     }
				    
				    CartDetails_Request.SetProduct_Ids(stringArray);
				    CartDetails_Request.SetProductNames(stringarray1);
				    CartDetails_Request.SetImagePaths(stringarray2);
				    CartDetails_Request.SetQuantitys(stringarray3);
				    CartDetails_Request.SetTotal_Costs(stringarray4);
				    CartDetails_Request.SetOverallCost(stringarray5);
			    }
			    else
			    {
			    	response.getEntity().getContent().close();
			        throw new IOException(statusLine.getReasonPhrase());
			    }
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
	}

	public boolean forgetPass()
	{
		String responseString;
		try
		{
			final String TAG_result="resp";


			//String url=String.format("http://my-demo.in/NFC_Shopping_Service_New/Service1.svc/Login/"+fillcust_Request.GetPhoneNo()+"/"+fillcust_Request.GetPassword());
			String url=String.format(ServiceUrl+"/forgetPassword/"+fillcust_Request.GetPhoneNo());
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(url));
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK)
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();

				JSONObject jsonObj=new JSONObject(responseString);
				String resp1=jsonObj.getString(TAG_result);

				if(resp1!="null")
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

    public int register()
	{
		try
		{
			final String TAG_id = "msg";
			StringBuilder result = new StringBuilder();

			HttpClient httpclient = new DefaultHttpClient();
			String url = String.format(ServiceUrl + "/register");
			HttpPost httpPost = new HttpPost(url);
			String json = "";
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("name", UsersData.getName());
			jsonObject.accumulate("contact", UsersData.getContact());
			jsonObject.accumulate("address", UsersData.getAddress());
			jsonObject.accumulate("email", UsersData.getEmail());
			jsonObject.accumulate("password", UsersData.getPassword());

			json = jsonObject.toString();
			StringEntity se = new StringEntity(json);
			httpPost.setEntity(se);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			HttpResponse response = httpclient.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK)
			{
				InputStream in = new BufferedInputStream(response.getEntity().getContent());
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = br.readLine()) != null) {
					result.append(line);
				}
				JSONObject jobj = new JSONObject(result.toString());
				String msg = jobj.getString(TAG_id);

				if (msg.equals("Inserted"))
				{
					return 0;
				}
				else if (msg.equals("Exist")) {
					return 1;
				}
				else {
					return 2;
				}
			}
			else {
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		}
		catch (Exception e)
		{
			return 2;
		}
    }
}
