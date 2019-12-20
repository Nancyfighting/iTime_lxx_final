package com.example.myitime.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.example.myitime.R;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    Calendar ca = Calendar.getInstance();
    int  year = ca.get(Calendar.YEAR);
    int  month = ca.get(Calendar.MONTH);
    int  day = ca.get(Calendar.DAY_OF_MONTH);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        LinearLayout a = (LinearLayout)this.findViewById(R.id.linearLayout_date);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddActivity.this,new DatePickerDialog.OnDateSetListener(){
                    //重写onDateSet方法
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        AddActivity.this.year=year;//dothings
                        AddActivity.this.month=monthOfYear+1;//year，monthOfYear,dayOfMonth分别为当前选择的年，月，日，这样便获取到了你想要的日期
                        AddActivity.this.day=dayOfMonth;
                    }
                },year,month,day).show();
            }
        });
    }
}
