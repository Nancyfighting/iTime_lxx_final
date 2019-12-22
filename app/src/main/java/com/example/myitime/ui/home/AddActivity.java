package com.example.myitime.ui.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myitime.R;
import com.example.myitime.data.Thing;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    Calendar ca = Calendar.getInstance();
    private int  year = ca.get(Calendar.YEAR);
    private int  month = ca.get(Calendar.MONTH);
    private int  day = ca.get(Calendar.DAY_OF_MONTH);
    TextView textViewDate;
    private ImageButton buttonOK,buttonCancel;
    private EditText editTextAddTitle,editTextAddTip;
    private AlertDialog alertDialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add);

        LinearLayout linearLayout_date = this.findViewById(R.id.linearLayout_date);
        LinearLayout linearLayout_image = this.findViewById(R.id.linearLayout_image);
        final LinearLayout linearLayout_autoNew = this.findViewById(R.id.linearLayout_autoNew);

        textViewDate = findViewById(R.id.textView_date_end);
        buttonOK = findViewById(R.id.imageButton_ok);
        buttonCancel = findViewById(R.id.imageButton_back);
        editTextAddTitle = findViewById(R.id.editText_add_title);
        editTextAddTip = findViewById(R.id.editText_add_tip);

        linearLayout_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddActivity.this,new DatePickerDialog.OnDateSetListener(){
                    //重写onDateSet方法
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        AddActivity.this.year=year;//dothings
                        AddActivity.this.month=monthOfYear+1;//year，monthOfYear,dayOfMonth分别为当前选择的年，月，日，这样便获取到了你想要的日期
                        AddActivity.this.day=dayOfMonth;
                        textViewDate.setText(AddActivity.this.year+"年"+AddActivity.this.month+"月"+ AddActivity.this.day+"日");
                    }
                },year,month,day).show();
            }
        });

        linearLayout_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlbum();
            }
        });

        linearLayout_autoNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showList(linearLayout_autoNew);
            }
        });

        //点击事件
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextAddTitle.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent();
                    intent.putExtra("title", editTextAddTitle.getText().toString());
                    intent.putExtra("tip", editTextAddTip.getText().toString());

                    setResult(RESULT_OK, intent);
                    AddActivity.this.finish();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddActivity.this.finish();
            }
        });

    }

    /**
     * 打开系统相册
     */
    public void openAlbum(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //设置请求码，以便我们区分返回的数据
        startActivityForResult(intent, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //判断请求码是否是请求打开相机的那个请求码
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null) {
                //获取数据
                //获取内容解析者对象
                try {
                    Bitmap cameraPhoto = BitmapFactory.decodeStream(
                            getContentResolver().openInputStream(data.getData()));
                    this.findViewById(R.id.constraintLayout2).setBackground(new BitmapDrawable(getResources(),cameraPhoto));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.add(0, CONTEXT_MENU_DELETE, 0, "删除");
//        menu.add(0, CONTEXT_MENU_NEW, 0, "添加");
//        menu.add(0, CONTEXT_MENU_UPDATE, 0, "修改");
//    }
//
//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case 1:
//                Toast.makeText(this, "点击了删除", Toast.LENGTH_SHORT).show();
//                break;
//            case 2:
//                Toast.makeText(this, "添加", Toast.LENGTH_SHORT).show();
//                break;
//            case 3:
//                Toast.makeText(this, "修改", Toast.LENGTH_SHORT).show();
//                break;
//
//            default:
//                break;
//        }
//        return super.onContextItemSelected(item);
//    }

    public void showList(View view){

        final String[] items = {"每周", "每月", "每日", "每年"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("周期");
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AddActivity.this, items[i], Toast.LENGTH_SHORT).show();
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = alertBuilder.create();
        alertDialog1.show();
    }

}
