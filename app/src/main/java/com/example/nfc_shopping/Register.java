package com.example.nfc_shopping;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.connectivity.connectionManager;
import com.example.data.UsersData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends Activity
{
    EditText txtName,txtAddress,txtContact,txtEmail,txtPassword;
    Button btnRegister;
    ProgressDialog dg;
    int resp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtName = (EditText) findViewById(R.id.txtName);
        txtContact = (EditText) findViewById(R.id.txtContact);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPass);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                validateSubmit();
            }
        });
    }

    public void validateSubmit()
    {
        final String name = txtName.getText().toString().trim(),
                address = txtAddress.getText().toString().trim(),
                contact = txtContact.getText().toString().trim(),
                email = txtEmail.getText().toString().trim(),
                password = txtPassword.getText().toString().trim();

        final EditText[] Alledit = {txtName, txtAddress, txtContact, txtEmail, txtPassword};
        for (EditText edit : Alledit)
        {
            if (edit.getText().toString().trim().length() == 0)
            {
                edit.setError("Empty Field");
                edit.requestFocus();
            }
        }

        if (!isValidEmail(email))
        {
            txtEmail.setError("Invalid Mail-Id");
        }
        else if (!isValidName(name))
        {
            txtName.setError("Invalid Name");
        }
        else if (password.length() < 4)
        {
            txtPassword.setError("Password Length must be atleast 4");
        }
        else if (contact.length() < 10 && contact.length() > 10)
        {
            txtPassword.setError("Invalid Number");
        }
        else
        {
            UsersData.setName(name);
            UsersData.setContact(contact);
            UsersData.setAddress(address);
            UsersData.setEmail(email);
            UsersData.setPassword(password);
            register();
        }
    }

    public void register()
    {
        final connectionManager conn = new connectionManager();
        if (connectionManager.checkNetworkAvailable(Register.this))
        {
            dg = new ProgressDialog(Register.this);
            dg.setMessage("Processing ...");
            dg.show();

            Thread thread = new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        resp = conn.register();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    hd.sendEmptyMessage(0);
                }
            };
            thread.start();
        }
        else
        {
            Toast.makeText(Register.this,"Sorry no network access.", Toast.LENGTH_LONG).show();
        }
    }

    public Handler hd = new Handler() {
        public void handleMessage(Message msg)
        {
            if (dg.isShowing())
                dg.dismiss();

            switch (resp)
            {
                case 0:
                    Toast.makeText(getApplicationContext(), "Register Successfully", Toast.LENGTH_LONG).show();
                    finish();
                    break;

                case 1:
                    Toast.makeText(getApplicationContext(), "Email Id already exists", Toast.LENGTH_LONG).show();
                    break;

                case 2:
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidName(String name)
    {
        String N_Pattern = "^([A-Za-z\\+]+[A-Za-z0-9])$";
        Pattern pattern = Pattern.compile(N_Pattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
}
