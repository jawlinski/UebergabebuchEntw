//package de.bfwi.sqlite;
package com.example.UebergabebuchEntw;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{
    EditText txtName;
    EditText txtGebDat;
    Button btnSpeichern;
    Button btnAnzeige;
    DBTools dbTools = new DBTools();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtName = (EditText) findViewById(R.id.txtName);
        txtGebDat = (EditText) findViewById(R.id.txtGebDat);
        btnSpeichern = (Button) findViewById(R.id.btnSpeichern);
        btnAnzeige = (Button) findViewById(R.id.btnAnzeige);

        dbTools.openDB(this);
        dbTools.createTable(this);

        btnSpeichern.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dbTools.insertRecord(MainActivity.this,
                        txtName.getText().toString(), txtGebDat.getText().toString());
            }
        });


        btnAnzeige.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        dbTools.closeDB(this);
    }

}
