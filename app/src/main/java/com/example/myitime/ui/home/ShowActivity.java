package com.example.myitime.ui.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myitime.MainActivity;
import com.example.myitime.R;
import com.example.myitime.data.Thing;

import java.util.ArrayList;

import static com.example.myitime.MainActivity.adapter;
import static com.example.myitime.ui.home.HomeFragment.getPicFromBytes;

public class ShowActivity extends AppCompatActivity {

    private int position;
    private Thing thing;
    private int[] date;
    private ImageView imageView_delete,imageView_edit;
    private TextView textView_daojishi,textView_date_show,textView_title_show;
    private ConstraintLayout constraintLayout_image;
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
        textView_daojishi.setText(thing.getTip());
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
                Intent intent_edit = new Intent(ShowActivity.this,AddActivity.class);
                intent_edit.putExtra("position",position);
                startActivityForResult(intent_edit, 2);
            }
        });

    }
}
