package com.example.viquan108.mortagecalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class AmortizationTable extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView table;
    private Button save;
    private String data;
    private File file ;
    FileOutputStream outputStream;
    private GridView header, grid1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_data);
                    Intent u = new Intent(AmortizationTable.this, Data.class);
                    startActivity(u);
                    finish();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amortization_table);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        table = (TextView)findViewById(R.id.data);
        save = (Button) findViewById(R.id.saveButton);

        Menu menu = navigation.getMenu();
        MenuItem item = menu.getItem(1);
        item.setChecked(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                data="No data!";
            } else {
                data= extras.getString("data");
            }
        }
        else {
            data+= (String) savedInstanceState.getSerializable("data");
        }

        table.setText(data);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(AmortizationTable.this);

                // Get the layout inflater
                 LayoutInflater inflater = AmortizationTable.this.getLayoutInflater();
                final View inflator = inflater.inflate(R.layout.dialog_save, null);
                final EditText fileName = (EditText) inflator.findViewById(R.id.filename);

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflator);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button

                       // file = new File(getApplicationContext().getFilesDir(), name+"");

                        try {
                            String name = fileName.getText().toString();
                            Log.i("filename",name);
                            outputStream = openFileOutput(name, Context.MODE_PRIVATE);
                            outputStream.write(data.getBytes());
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();

                dialog.show();

                

            }
        });

    }





}
