package com.example.viquan108.mortagecalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;

public class Data extends AppCompatActivity {

    private TextView mTextMessage;
    private ListView listView;

    private String [] list;
    private String table=null;
    private FileInputStream inputStream;
    private File[] files;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    Intent u = new Intent(Data.this, MainActivity.class);
                    startActivity(u);
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    Intent i = new Intent(Data.this, AmortizationTable.class);
                    startActivity(i);
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_data);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        MenuItem item = menu.getItem(2);
        item.setChecked(true);

        files = listFiles();

        //open file
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                // TODO form load
                Log.i("item",i+" :" +list[i]);

                try {
                    String name = files[i+1].getName();
                    Log.i("filename",name);
                    inputStream = openFileInput(name);

                    BufferedReader br = null;
                    StringBuilder sb = new StringBuilder();

                    try {

                        br = new BufferedReader(new InputStreamReader(inputStream));
                        while ((table = br.readLine()) != null) {
                            sb.append(table);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    table = sb.toString();
                    inputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent s = new Intent(Data.this, AmortizationTable.class);
                s.putExtra("data", table);
                startActivity(s);

            }
        });
        // delete file
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO form deletion
                Log.v("long clicked","pos: " + pos);
                String fn =files[pos+1].getName()+"";
                Log.i("Delete", fn);

                final int index = pos;

                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(Data.this);

                // Get the layout inflater
                LayoutInflater inflater = Data.this.getLayoutInflater();
                final View inflator = inflater.inflate(R.layout.data_dialog_delete, null);
                final TextView fileName = (TextView) inflator.findViewById(R.id.deleteFileName);
                fileName.setText("Delete \"" +fn+ "\"?");

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflator);

                // 2. Chain together various setter methods to set the dialog characteristics

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        // file = new File(getApplicationContext().getFilesDir(), name+"");
                            files[index+1].delete();
                        listFiles();

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

                return true;
            }
        });


    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public File[] listFiles(){
        String path = getApplicationContext().getFilesDir()+"";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        final File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        list = new String[files.length-1];
        for (int i = 1; i < files.length; i++)
        {
            //add files to array
            Log.d("Files", "FileName:" + files[i].getName());
            list[i-1] = files[i].getName();
        }

        listView = (ListView)findViewById(R.id.list);
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        return files;
    }

    public String formatData(String d){
        String data="";
        for (int i =0; i<d.length();i++){

        }
        return data;
    }
}
