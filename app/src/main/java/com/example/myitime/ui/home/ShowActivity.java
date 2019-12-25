package com.example.myitime.ui.home;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myitime.MainActivity;
import com.example.myitime.R;
import com.example.myitime.data.Thing;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.myitime.MainActivity.adapter;
import static com.example.myitime.ui.home.HomeFragment.getPicFromBytes;

public class ShowActivity extends AppCompatActivity {

    private SimpleDateFormat format;
    private int position;
    Handler handler = new Handler();
    private long leftTime ;//一分钟
    private Thing thing;
    private int[] date;
    private ImageView imageView_delete,imageView_edit;
    private TextView textView_daojishi,textView_date_show,textView_title_show;
    private ConstraintLayout constraintLayout_image;

    private TextView mDays_Tv, mHours_Tv, mMinutes_Tv, mSeconds_Tv;
    private boolean isRun = true;
//    private long day
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        textView_daojishi=findViewById(R.id.textView_daojishi);
        textView_date_show=findViewById(R.id.textView_date_show);
        textView_title_show=findViewById(R.id.textView_title_show);
        constraintLayout_image=findViewById(R.id.constraintLayout_image);
        imageView_delete=findViewById(R.id.imageView_delete);
        imageView_edit=findViewById(R.id.imageView_edit);

        position =getIntent().getIntExtra("position",0);
        thing = MainActivity.listThings.get(position);
        textView_title_show.setText(thing.getTitle());
        Bitmap bmp = getPicFromBytes(thing.getImage(),null);
        constraintLayout_image.setBackground(new BitmapDrawable(getResources(),bmp));
        date = thing.getTime();
        textView_date_show.setText(date[0]+"年"+date[1]+"月"+date[2]+"日");


        imageView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowActivity.this);
                builder.setTitle("是否删除该计时？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.listThings.remove(MainActivity.listThings.get(position));
                                adapter.notifyDataSetChanged();
                                ShowActivity.this.finish();
                            }
                        });
                builder.show();
            }
        });

        imageView_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_edit = new Intent(ShowActivity.this, EditActivity.class);
                intent_edit.putExtra("position",position);
                startActivityForResult(intent_edit, 2);
                Log.v("eeeeee","efefe");

            }
        });

        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        Date d1 = null;
        try {
            d1 = format.parse(date[0]+"-"+date[1]+"-"+date[2]+" "+date[3]+":"+date[4]+":00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date d2 = null;
        try {
            d2 = format.parse(year+"-"+month+"-"+day+" "+hour +":"+minute+":00");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        leftTime = (d1.getTime() - d2.getTime())/1000;//这样得到的差值是微秒级别

        if(leftTime>0)
        startRun();

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            Intent intent = new Intent(ShowActivity.this,ShowActivity.class);
            startActivity(intent);
            finish();
        }
    }





    private void startRun() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                leftTime--;
                if (leftTime > 0) {
                    //倒计时效果展示
                    String formatLongToTimeStr = formatLongToTimeStr(leftTime);
                    textView_daojishi.setText(formatLongToTimeStr);
                    //每一秒执行一次
                    handler.postDelayed(this, 1000);
                }
            }
        }).start();
    }


    public String formatLongToTimeStr(Long date) {
        long day = date / (60 * 60 * 24);
        long hour = (date / (60 * 60) - day * 24);
        long min = ((date / 60) - day * 24 * 60 - hour * 60);
        long s = (date - day*24*60*60 - hour*60*60 - min*60);
        String strtime = "剩余："+day+"天"+hour+"小时"+min+"分"+s+"秒";
        return strtime;
    }



}
