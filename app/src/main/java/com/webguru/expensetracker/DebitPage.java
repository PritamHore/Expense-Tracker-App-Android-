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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DateFormat;
import java.util.Calendar;

public class DebitPage extends AppCompatActivity {
    TextView cal;
    int d, m, y;
    ImageView img1;
    TextView dt;
    EditText cn, cst;
    boolean mflg;
    int rec_id=0;
    Toolbar tb;
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit);
//        et = (EditText) findViewById(R.id.edtOther);

        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        tb.setNavigationIcon(R.drawable.leftarrow);

        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final Calendar c = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(c.getTime());
        cal = (TextView) findViewById(R.id.tvcal1);
        cal.setText(currentDate);
        y = c.get(Calendar.YEAR);
        m = c.get(Calendar.MONTH);
        d = c.get(Calendar.DAY_OF_MONTH);

        img1 = (ImageView) findViewById(R.id.imgCal1);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        mflg = false;
        cn = (EditText) findViewById(R.id.edtOther);
        cst = (EditText) findViewById(R.id.debitAmmount);
        dt = (TextView) findViewById(R.id.tvcal1);

        rec_id = getIntent().getIntExtra("rec_id", -1);
//        Toast.makeText(this, "User Id: " + rec_id, Toast.LENGTH_SHORT).show();

        if(rec_id!=-1) {
            mflg = true;
            DBAdapter db = new DBAdapter(getBaseContext());
            db.open();
            Cursor c1 = db.getContact(rec_id);
            if (c1.moveToFirst()) {
                cn.setText(c1.getString(1));
                cst.setText(c1.getString(3));
                dt.setText(c1.getString(4));
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
    public void btnDebit(View v) {
        if(mflg) modify_rec();
        else add_rec();
    }

    private void add_rec() {
        DBAdapter db = new DBAdapter(getBaseContext());
        //---add a contact---
        db.open();
//        cn.setText("-"+cn);
        String uname = cn.getText().toString();
        String uincome = cst.getText().toString();
        String uexpense = "";
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
        String mincome = cst.getText().toString();
        String mexpense = "";
        String mdate = dt.getText().toString();
        if (db.updateContact(rec_id, nm, mincome, mexpense, mdate))
            Toast.makeText(this, "Update successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();
        db.close();
    }

}
