package com.example.viquan108.mortagecalculator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.viquan108.mortagecalculator.R.id.mortgageAmount;

//public class MainActivity extends AppCompatActivity {
public class MainActivity extends Activity {

    // ui
    private TextView mTextMessage;

    private EditText amount;
    private EditText termY;
    private EditText termM;

    private EditText extraMonth;
    private EditText extraYear;
    private EditText extraOnce;

    private EditText rate;
    private TextView payment;
    private TextView paidOffDate;

    private Spinner daySpinner;
    private Spinner monthSpinner;
    private Spinner yearSpinner;

    private Spinner extraMonthSpinner;
    private Spinner extraOnceYSpinner;
    private Spinner extraOnceMSpinner;

    private Button calculate;
    private Button extraButton;

    private LinearLayout extraWindow;

    //data
    private double mortgageAmount;
    private double monthlyTerms ;

    private double monthlyRate;
    private double monthlyPayment;


    private String table;

    private boolean hidden = true;

    //database
    public DatabaseHelper db;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    Intent i = new Intent(MainActivity.this, AmortizationTable.class);
                    i.putExtra("data", table);
                    startActivity(i);
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_data);
                    Intent u = new Intent(MainActivity.this, Data.class);
                    startActivity(u);
                    finish();
                    return true;
            }
            return false;
        }

    };

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set up notitle
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set up full screen
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);


        // connecting ui
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        monthSpinner  = (Spinner) findViewById(R.id.month);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        yearSpinner = (Spinner) findViewById(R.id.year);
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this,R.array.years, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        daySpinner = (Spinner) findViewById(R.id.day);
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this,R.array.days, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);


        extraMonthSpinner  = (Spinner) findViewById(R.id.extraMonthSpinner);
        ArrayAdapter<CharSequence> EMadapter = ArrayAdapter.createFromResource(this,R.array.months, android.R.layout.simple_spinner_item);
        EMadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        extraMonthSpinner.setAdapter(EMadapter);

        extraOnceYSpinner  = (Spinner) findViewById(R.id.extraOnceYSpinner);
        ArrayAdapter<CharSequence> OYadapter = ArrayAdapter.createFromResource(this,R.array.months, android.R.layout.simple_spinner_item);
        OYadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        extraOnceYSpinner.setAdapter(OYadapter);

        extraOnceMSpinner  = (Spinner) findViewById(R.id.extraOnceMSpinner);
        ArrayAdapter<CharSequence> OMadapter = ArrayAdapter.createFromResource(this,R.array.years, android.R.layout.simple_spinner_item);
        OMadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        extraOnceMSpinner.setAdapter(OMadapter);

        calculate = (Button)findViewById(R.id.calculateButton);
        extraButton = (Button)findViewById(R.id.extraButton);
        amount  =  (EditText)findViewById(R.id.amount);
        termY  =  (EditText)findViewById(R.id.term);
        termM  =  (EditText)findViewById(R.id.termM);
        rate  =  (EditText)findViewById(R.id.rate);
        extraWindow = (LinearLayout)findViewById(R.id.extraWindow);

        payment = (TextView) findViewById(R.id.payment);
        paidOffDate  = (TextView) findViewById(R.id.paidOffDate);

        extraMonth =  (EditText)findViewById(R.id.extraMonth);
        extraYear =  (EditText)findViewById(R.id.extraYear);
        extraOnce =  (EditText)findViewById(R.id.extraOnce);

        //Listeners

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("amount ","$"+amount.getText());
                Log.i("term ",termY.getText()+"years "+ termM.getText()+ " months");
                Log.i("rate ",rate.getText()+"%");
                calculate();
            }
        });

        extraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hidden) {
                   // Drawable d = getResources().getDrawable(android.R.drawable.arrow_up_float);
                   // hide.setBackgroundDrawable(d);

                    extraWindow.setVisibility(LinearLayout.GONE);

                    hidden = true;
                }
                else{
                   // Drawable d = getResources().getDrawable(android.R.drawable.arrow_down_float);
                   // hide.setBackgroundDrawable(d);

                    extraWindow.setVisibility(LinearLayout.VISIBLE);

                    hidden=false;
                }
            }
        });

    }
    public void calculate (){
        table ="";
        mortgageAmount = Double.parseDouble(String.valueOf(amount.getText()));
        monthlyTerms = Integer.parseInt(String.valueOf(termY.getText())) * 12 + Integer.parseInt(String.valueOf(termM.getText()));
        monthlyRate= Double.parseDouble(String.valueOf(rate.getText())) / 1200.0;
        monthlyPayment= mortgageAmount* ( monthlyRate*Math.pow((1 + monthlyRate),monthlyTerms)
                / ( Math.pow((1 + monthlyRate),monthlyTerms)- 1) );
        Log.i("payment", monthlyPayment+"");

        payment.setText("$ "+ String.format("%.2f", monthlyPayment));

        int fmonths = Amortization(monthlyPayment,monthlyRate,mortgageAmount,
                                Double.parseDouble(String.valueOf(extraMonth.getText())),
                                Double.parseDouble(String.valueOf(extraYear.getText())),
                                Double.parseDouble(String.valueOf(extraOnce.getText()))
                                );


    }

    public int Amortization(double P, double rate, double Bal,
                            double extraMonthly,double extraYearly,double extraOnce){
        int months = 0;
        double payment=0;
        double totalInt=0;
        double prin =0;
        double interest =0;

        Calendar call = new GregorianCalendar();

        while (((int)Bal)>0){
            months++;

            payment=P;
            payment+=extraMonthly;

            if((months+ monthSpinner.getSelectedItemPosition() +1)%12==(extraMonthSpinner.getSelectedItemPosition()+1)){
                payment+=extraYearly;
            }

            double i=((Double.valueOf(extraOnceYSpinner.getSelectedItemPosition()+1995)-Double.valueOf(yearSpinner.getSelectedItemPosition()+1995))*12)
                    +(extraOnceMSpinner.getSelectedItemPosition()-monthSpinner.getSelectedItemPosition());

            if (months==i){
                payment+=extraOnce;
            }

            interest=Bal*rate;
            totalInt+=interest;
            prin=payment-interest;

            if(Bal-prin<0){
                interest=Bal*rate;
                totalInt+=interest;
                prin=Bal;
                payment = prin+interest;
                Bal=0;
            }
            else{
                Bal-=prin;
            }

            call.set(Integer.valueOf(yearSpinner.getSelectedItem().toString()),
                    Integer.valueOf(monthSpinner.getSelectedItemPosition())+1,
                    Integer.valueOf(daySpinner.getSelectedItemPosition())+1);

            call.add(Calendar.MONTH, (int)months);
            // TODO recontruct into parts
            table+=dateformat_month(call.get(Calendar.MONTH)) +
                    " "+ call.get(Calendar.YEAR) +
                    " \t "+String.format("%.2f", payment)+
                    " \t "+ String.format("%.2f",prin) +
                    " \t "+String.format("%.2f", interest)+
                    " \t "+String.format("%.2f", totalInt)+
                    " \t "+String.format("%.2f", Bal)+
                    "\n";

            db.insertData("a","b","c","d","e","f");

        }

        Log.i("date",""+(daySpinner.getSelectedItemPosition()+1) + " "+ (monthSpinner.getSelectedItemPosition()) + " "+ yearSpinner.getSelectedItem().toString()) ;
        Log.i("date",""+ extraOnceYSpinner.getSelectedItemPosition()+1995) ;

        paidOffDate.setText(""+dateformat_month(call.get(Calendar.MONTH))+" "+call.get(Calendar.DATE)+ " "+call.get(GregorianCalendar.YEAR) );
        Log.i("month",""+ call.get(Calendar.MONTH) + " "+call.get(GregorianCalendar.YEAR) );
        return months;
    }

    public String dateformat_month(int i){
        String s="";
        switch(i){
            case 0 :{s="Jan";};
            break;
            case 1 :{s="Feb";};
            break;
            case 2 :{s="Mar";};
            break;
            case 3 :{s="Apr";};
            break;
            case 4 :{s="May";};
            break;
            case 5 :{s="Jun";};
            break;
            case 6 :{s="Jul";};
            break;
            case 7 :{s="Aug";};
            break;
            case 8 :{s="Sep";};
            break;
            case 9 :{s="Oct";};
            break;
            case 10 :{s="Nov";};
            break;
            case 11 :{s="Dec";};
            break;

        }
        return s;
    }

}
