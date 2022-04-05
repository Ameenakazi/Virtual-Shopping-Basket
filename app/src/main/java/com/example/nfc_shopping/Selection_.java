package com.example.nfc_shopping;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectivity.connectionManager;
import com.example.data.CartDetails_Request;
import com.example.data.fillProduct_Request;
import com.example.util.progressdialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class Selection_ extends Activity {

    public static String re;
    Dialog dg;
    int resp;
    public static SharedPreferences myPreferences,sharedPreferences;
    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_);

        sharedPreferences = getSharedPreferences(SharedPreference.loginPref, 0);
        String name = sharedPreferences.getString("name",null);
        String balance = sharedPreferences.getString("balance",null);

        TextView txtName = (TextView) findViewById(R.id.txtName);
        TextView txtBalance = (TextView) findViewById(R.id.txtBalance);

        String Cname = "Name : " + name;
        txtName.setText(Cname);
        String CBal = "Balance : " + balance;
        txtBalance.setText(CBal);

        myPreferences=getSharedPreferences("NFCShopping", Context.MODE_PRIVATE);
        myEditor=myPreferences.edit();
        Button btnTapNfc = (Button) findViewById(R.id.btnTapNfc);
        Button btnScanQr = (Button) findViewById(R.id.btnScanQr);
        btnTapNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Selection_.this, NFC_Page.class);
                startActivity(intent);
            }
        });

        btnScanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(Selection_.this);
                integrator.initiateScan();
            }
        });

        Button btnLogout_=(Button)findViewById(R.id.btnLogoutMain);
        btnLogout_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditor.putString("PhoneNo","");
                myEditor.putString("Password","");
                myEditor.commit();

                Intent intent=new Intent(Selection_.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        Button btnSeeCart=(Button)findViewById(R.id.btnViewCart);
        btnSeeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewCart();
            }
        });

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && resultCode == RESULT_OK) {
            re = scanResult.getContents();
            fillProduct_Request.SetProduct_Id(re);
            final connectionManager conn = new connectionManager();
            if (connectionManager.checkNetworkAvailable(this)) {
                progressdialog dialog = new progressdialog();
                dg = dialog.createDialog(this);
                dg.show();
                Thread th1 = new Thread() {
                    @Override
                    public void run() {
                        try {
                            conn.GetProductDetails();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        hd.sendEmptyMessage(0);

                    }
                };
                th1.start();
            } else {
                Toast.makeText(this, "Sorry no network access.", Toast.LENGTH_LONG).show();
            }
            Log.d("code", re);

        }
        // else continue with any other code you need in the method
        else if(resultCode == RESULT_CANCELED){
            String a = "kfredfhyeru";
        }
    }


    public Handler hd = new Handler() {
        public void handleMessage(Message msg) {
            dg.cancel();
            switch (resp) {
                case 0:
                    Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();
                    dg.cancel();
                    Intent intent=new Intent(Selection_.this,Product_Details_Page.class);
                    intent.putExtra("Form", "NFC");
                    startActivity(intent);
                    finish();
                    break;

                case 1:
                    Toast.makeText(getApplicationContext(), "Not Submitted SCan Again", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    public void viewCart()
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

    public Handler hd2=new Handler()
    {
        public void handleMessage(Message msg)
        {
            dg.cancel();
            final ArrayList<String> Product_Id;
            Product_Id= CartDetails_Request.GetProduct_Ids();
            if(Product_Id.isEmpty())
            {
                AlertDialog alert=new AlertDialog.Builder(Selection_.this).create();
                alert.setTitle("No Record Found");
                alert.setMessage("No Cart Details found");
                alert.show();

            }
            else
            {
                Intent intent=new Intent(Selection_.this,Customer_Cart.class);
                startActivity(intent);
                finish();
            }
        }
    };

}
