package com.example.myitime.ui.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myitime.MainActivity;
import com.example.myitime.R;
import com.example.myitime.data.Thing;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import static com.example.myitime.MainActivity.adapter;

public class AddActivity extends AppCompatActivity {
    Calendar ca = Calendar.getInstance();
    private int date[];
    TextView textViewDate;
    TextView textView_autoNew_end;
    private ImageView ImageView_OK,ImageView_Cancel;
    private EditText editTextAddTitle,editTextAddTip;
    private AlertDialog alertDialog1;
    private Bitmap cameraPhoto;
    private String auto_pre;
    private int auto_num;
    private int position;
    private Thing thing;
    private int yemian=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add);

        //初始化装日期的数组
        date= new int[]{ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH), ca.get(Calendar.HOUR_OF_DAY), ca.get(Calendar.MINUTE)};
        LinearLayout linearLayout_date = this.findViewById(R.id.linearLayout_date);
        LinearLayout linearLayout_image = this.findViewById(R.id.linearLayout_image);
        final LinearLayout linearLayout_autoNew = this.findViewById(R.id.linearLayout_autoNew);

        textView_autoNew_end = findViewById(R.id.textView_autoNew_end);
        textViewDate = findViewById(R.id.textView_date_end);
        ImageView_OK = findViewById(R.id.imageView_ok);
        ImageView_Cancel = findViewById(R.id.imageView_back);
        editTextAddTitle = findViewById(R.id.editText_add_title);
        editTextAddTip = findViewById(R.id.editText_add_tip);

        position=getIntent().getIntExtra("position",-1);
        if(position != -1)
        {
            String s = editTextAddTitle.getText().toString();
            yemian=1;

            thing = MainActivity.listThings.get(position);
            editTextAddTitle.setText(thing.getTitle());
            editTextAddTip.setText(thing.getTip());
            date =thing.getTime();
            textViewDate.setText(date[0]+"年"+date[1]+"月"+ date[2]+"日 " + date[3] + ":" + date[4]);
            textView_autoNew_end.setText(thing.getAuto_pre());
        }
        //点击选择日期后
        linearLayout_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddActivity.this,new DatePickerDialog.OnDateSetListener(){
                    //重写onDateSet方法
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date[0]=year;//dothings
                        date[1]=monthOfYear+1;//year，monthOfYear,dayOfMonth分别为当前选择的年，月，日，这样便获取到了你想要的日期
                        date[2]=dayOfMonth;

                        new TimePickerDialog( AddActivity.this,new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        date[3]=hourOfDay;
                                        date[4]=minute;
                                        textViewDate.setText(date[0]+"年"+date[1]+"月"+ date[2]+"日 " + hourOfDay + ":" + minute);
                                    }
                                }
                                // 设置初始时间
                                , ca.get(Calendar.HOUR_OF_DAY)
                                , ca.get(Calendar.MINUTE)
                                // true表示采用24小时制
                                ,true).show();
                    }
                },ca.get(Calendar.YEAR),ca.get(Calendar.MONTH),ca.get(Calendar.DAY_OF_MONTH)).show();
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


        //点击确认后事件
        ImageView_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextAddTitle.getText().toString().equals("")||cameraPhoto==null){
                    if(editTextAddTitle.getText().toString().equals(""))
                         Toast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "图片不能为空", Toast.LENGTH_SHORT).show();
                }
                else{
                    String title = editTextAddTitle.getText().toString();
                    String tip = editTextAddTip.getText().toString();
                    String auto_pre = textView_autoNew_end.getText().toString();
                    ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
                    cameraPhoto.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
                    byte[] image = output.toByteArray();//转换成功了  result就是一个bit的资源数组
                    if(yemian==1)
                    {
                        Thing thing_update=MainActivity.listThings.get(position);
                        thing_update.setTitle(title);
                        thing_update.setTip(tip);
                        thing_update.setAuto_pre(auto_pre);
                        thing_update.setImage(image);
                        thing_update.setTime(date);
                    }
                    else
                        MainActivity.listThings.add( new Thing(title, tip,date,image,auto_pre));
                    adapter.notifyDataSetChanged();
                    AddActivity.this.finish();
                }
            }
        });

        ImageView_Cancel.setOnClickListener(new View.OnClickListener() {
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
                    ContentResolver resolver = getContentResolver();
                    Uri originalUri = data.getData();        //获得图片的uri
                    cameraPhoto = MediaStore.Images.Media.getBitmap(resolver, originalUri);        //得到bitmap图片
                    this.findViewById(R.id.constraintLayout2).setBackground(new BitmapDrawable(getResources(),cameraPhoto));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void showList(View view){

        final String[] items = {"每周", "每月", "每年","自定义","无"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("周期");
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i!=3) {
                    textView_autoNew_end.setText(items[i]);
                }
                else
                {
                    final EditText inputServer = new EditText(AddActivity.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                    builder.setTitle("周期").setView(inputServer)
                            .setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            textView_autoNew_end.setText(inputServer.getText().toString()+"天");
                        }
                    });
                    builder.show();
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = alertBuilder.create();
        alertDialog1.show();
    }

}
