package com.webguru.expensetracker;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DateFormat;
import java.util.Calendar;

public class CreditPage extends AppCompatActivity {
    TextView cal,dt, cn;
    int d, m, y;
    ImageView img1;
    EditText cst;
    boolean mflg;
    int rec_id=0;
    Toolbar tb2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        tb2 = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb2);

        tb2.setNavigationIcon(R.drawable.leftarrow);

        tb2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final Calendar c = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(c.getTime());
        cal = (TextView) findViewById(R.id.tvcal);
        cal.setText(currentDate);
        y = c.get(Calendar.YEAR);
        m = c.get(Calendar.MONTH);
        d = c.get(Calendar.DAY_OF_MONTH);



        img1 = (ImageView) findViewById(R.id.imgCal);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        mflg = false;
        cn = (TextView) findViewById(R.id.edtincome);
        cst = (EditText) findViewById(R.id.edtammount);
        dt = (TextView) findViewById(R.id.tvcal);

        rec_id = getIntent().getIntExtra("rec_id", -1);
//        Toast.makeText(this, "User Id: " + rec_id, Toast.LENGTH_SHORT).show();

        if(rec_id!=-1) {
            mflg = true;
            DBAdapter db = new DBAdapter(getBaseContext());
            db.open();
            Cursor c2 = db.getContact(rec_id);
            if (c2.moveToFirst()) {
                cn.setText(c2.getString(1));
                cst.setText(c2.getString(2));
                dt.setText(c2.getString(4));
            }
            db.close();
        }
    }

    public void showDatePicker()
    {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                cal.setText("" + dd + "/" + (mm+1) + "/" + yy);
            }
        }, y, m, d).show();

    }
    public void btnCredit(View v) {
        if(mflg) modify_rec();
        else add_rec();
    }

    private void add_rec() {
        DBAdapter db = new DBAdapter(getBaseContext());
        //---add a contact---
        db.open();
//        cn.setText("-"+cn);
        String uname = ("Ammount Added!" + cn.getText()).toString();
        String uincome = "";
        String uexpense = cst.getText().toString();
        String udate = dt.getText().toString();
        long id = db.insertContact(uname, uincome, uexpense, udate);

        if(id != 0) Toast.makeText(getBaseContext(), "Record Saved!", Toast.LENGTH_LONG).show();

        db.close();
    }

    private void modify_rec() {
        DBAdapter db = new DBAdapter(getBaseContext());
        //---update contact---
        db.open();
        String nm = cn.getText().toString();
        String mincome = "";
        String mexpense = cst.getText().toString();
        String mdate = dt.getText().toString();
        if (db.updateContact(rec_id, nm, mincome, mexpense, mdate))
            Toast.makeText(this, "Update successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();
        db.close();
    }

    public void goBack(View v) {
        super.onBackPressed();
    }

}
