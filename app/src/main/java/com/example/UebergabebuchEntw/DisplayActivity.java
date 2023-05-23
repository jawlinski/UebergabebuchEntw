/*package de.bfwi.sqlite;*/
package com.example.UebergabebuchEntw;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity
{
   TextView lblID;
   TextView lblName;
   TextView lblGebDat;
   ImageButton btnEntfernen;
   ImageButton btnAnfang;
   ImageButton btnWeiter;
   ImageButton btnZurueck;
   ImageButton btnEnde;
   Button btnMain;
   DBTools dbTools = new DBTools();
   Cursor cursor = null;

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_display);
      lblID = (TextView) findViewById(R.id.lblID);
      lblName = (TextView) findViewById(R.id.lblName);
      lblGebDat = (TextView) findViewById(R.id.lblGebDat);
      btnEntfernen = (ImageButton) findViewById(R.id.btnEntfernen);
      btnAnfang = (ImageButton) findViewById(R.id.btnAnfang);
      btnWeiter = (ImageButton) findViewById(R.id.btnWeiter);
      btnZurueck = (ImageButton) findViewById(R.id.btnZurueck);
      btnEnde = (ImageButton) findViewById(R.id.btnEnde);
      btnMain = (Button) findViewById(R.id.btnMain);

      btnAnfang.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            Boolean rc = false;
            modifyRecord(cursor);
            rc = cursor.moveToFirst();
            if (rc) displayRecord(cursor);
         }
      });

      btnZurueck.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            Boolean rc = false;
            modifyRecord(cursor);
            if(cursor.getPosition() < (cursor.getCount()-1))
            {
               rc = cursor.moveToNext();
               if (rc) displayRecord(cursor);
            }
         }
      });

      btnWeiter.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
             Boolean rc = false;
             modifyRecord(cursor);
             if (cursor.getPosition() > 0)
             {
                 rc = cursor.moveToPrevious();
                 if (rc) displayRecord(cursor);
             }
         }
      });

      btnEnde.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            Boolean rc = false;
            modifyRecord(cursor);
            rc = cursor.moveToLast();
            if (rc) displayRecord(cursor);
         }
      });

      btnEntfernen.setOnClickListener(new View.OnClickListener()
      {
           @Override
           public void onClick(View v)
           {
              dbTools.deleteRecord(DisplayActivity.this, cursor);
              cursor.moveToFirst();
              displayRecord(cursor);
           }
      });

      btnMain.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            Intent intent = new Intent(DisplayActivity.this, MainActivity.class);
            startActivity(intent);
         }
      });

      dbTools.openDB(this);
      cursor = dbTools.getCursor(this);
      cursor.moveToFirst();
      displayRecord(cursor);
   }


   public void displayRecord(Cursor cu)
   {
      if (cu.getCount() > 0)
      {
         lblID.setText(cu.getString(0));
         lblName.setText(cu.getString(1));
         lblGebDat.setText(cu.getString(2));
      }
   }

   public void modifyRecord(Cursor cu)
   {
      try
      {
         int pos = cu.getPosition();
         ContentValues daten = new ContentValues();
         daten.put("fullname", lblName.getText().toString());
         daten.put("birthday", lblGebDat.getText().toString());
         dbTools.modifyRecord(DisplayActivity.this, cu, daten);
         cursor = dbTools.getCursor(DisplayActivity.this);
         cursor.moveToPosition(pos);
      }
      catch (Exception ex)
      {
         Log.e("de.bfwi.sqlite",getLocalClassName()+" - Update Record Error");
      }
   }
}

