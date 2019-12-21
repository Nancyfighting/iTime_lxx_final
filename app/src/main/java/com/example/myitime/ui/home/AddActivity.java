package com.example.myitime.ui.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myitime.R;

import java.util.Calendar;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {
    Calendar ca = Calendar.getInstance();
    int  year = ca.get(Calendar.YEAR);
    int  month = ca.get(Calendar.MONTH);
    int  day = ca.get(Calendar.DAY_OF_MONTH);
    TextView textview_date;
    private ImageButton buttonOK,buttonCancel;
    private EditText editTextAddTitle,editTextAddTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add);

        LinearLayout a = this.findViewById(R.id.linearLayout_date);
        textview_date = findViewById(R.id.textView_date_end);
        buttonOK = findViewById(R.id.imageButton_ok);
        buttonCancel = findViewById(R.id.imageButton_back);
        editTextAddTitle = findViewById(R.id.editText_add_title);
        editTextAddTip = findViewById(R.id.editText_add_tip);
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
                        textview_date.setText(AddActivity.this.year+"年"+AddActivity.this.month+"月"+ AddActivity.this.day+"日");
                    }
                },year,month,day).show();
            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("title", editTextAddTitle.getText().toString());
                intent.putExtra("tip", editTextAddTip.getText().toString());
                setResult(RESULT_OK, intent);
                AddActivity.this.finish();

            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddActivity.this.finish();
            }
        });
    }
}
