package com.example.myitime.ui.home;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myitime.MainActivity;
import com.example.myitime.R;
import com.example.myitime.data.Thing;
import com.example.myitime.data.ThingSaver;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.myitime.MainActivity.adapter;
import static com.example.myitime.MainActivity.image_moren;
import static com.example.myitime.MainActivity.listThings;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private SimpleDateFormat format;
    Handler handler = new Handler();
    private long leftTime ;//一分钟
    ThingSaver thingSaver;
    Date d2 = null;
    private ResourceBundle extras;
    private TextView textView_zhi,textView_sheng;


    public void onDestroy() {
        super.onDestroy();
        thingSaver.save();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        thingSaver=new ThingSaver(root.getContext());
        MainActivity.listThings=thingSaver.load();
//        Bitmap cameraPhoto_lim=BitmapFactory.decodeResource(getResources(),R.drawable.mole2);
//        ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
//        cameraPhoto_lim.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
//        image_moren = output.toByteArray();//转换成功了  result就是一个bit的资源数组
//        if(listThings.size()==0)
//        {
//            int [] date_add=new int[]{2020,2,11,18,30};
//            listThings.add(new Thing("做作业","安卓",date_add, image_moren,"每天"));
//        }
        ListView listViewThings=root.findViewById(R.id.list_view_things);
        adapter = new ThingAdapter(HomeFragment.this.getActivity(),R.layout.list_view_item_thing,MainActivity.listThings);
        listViewThings.setAdapter(adapter);

        //listView中的点击事件
        listViewThings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentNew = new Intent(root.getContext(), ShowActivity.class);
                intentNew.putExtra("position",i);
                startActivityForResult(intentNew, 1);
            }
        });

        this.registerForContextMenu(listViewThings);

        final Button button_add = root.findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentNew = new Intent(root.getContext(), EditActivity.class);
                startActivityForResult(intentNew, 0);
            }
        });


        return root;
    }


    public class ThingAdapter extends ArrayAdapter<Thing> {

        private int resourceId;
        private String path;
        public ThingAdapter(Context context, int resource, List<Thing> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Thing thing = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

//            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Calendar calendar=Calendar.getInstance();
//            int year=calendar.get(Calendar.YEAR);
//            int month=calendar.get(Calendar.MONTH)+1;
//            int day=calendar.get(Calendar.DAY_OF_MONTH);
//            int hour=calendar.get(Calendar.HOUR_OF_DAY);
//            int minute = calendar.get(Calendar.MINUTE);
//            try {
//                d2 = format.parse(year+"-"+month+"-"+day+" "+hour +":"+minute+":00");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }



            ContentResolver  a = HomeFragment.this.getContext().getContentResolver();
            byte[] image_byte= thing.getImage();
            Bitmap bmp = getPicFromBytes(image_byte,null);
//            ((ImageView) view.findViewById(R.id.image_view_thing)).setImageDrawable(new BitmapDrawable(getResources(),bmp));
            view.findViewById(R.id.conslayout_daojishi).setBackground((new BitmapDrawable(getResources(),bmp)));
            textView_sheng = view.findViewById(R.id.textView_sheng);
            ((TextView) view.findViewById(R.id.title_view_thing)).setText(thing.getTitle());
            ((TextView) view.findViewById(R.id.title_view_thing)).setText(thing.getTitle());
            ((TextView) view.findViewById(R.id.date_view_thing)).setText(thing.getTime()[0]+"年"+thing.getTime()[1]+"月"+thing.getTime()[2]+"日");
            ((TextView) view.findViewById(R.id.tip_view_thing)).setText(thing.getTip());
//            startRun(textView_sheng);

            return view;
        }
    }

    //下面的这个方法是将byte数组转化为Bitmap对象的一个方法
    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {

        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,  opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;

    }

    private void startRun(final TextView textView) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                for(int i=0;i<listThings.size();i++)
                {
                    int[]date_clock=listThings.get(i).getTime();
                    Date d1 = null;
                    try {
                        d1 = format.parse(date_clock[0]+"-"+date_clock[1]+"-"+date_clock[2]+" "+date_clock[3]+":"+date_clock[4]+":00");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    leftTime = (d1.getTime() - d2.getTime())/1000;//这样得到的差值是微秒级别
                    leftTime--;
                }

                if (leftTime > 0) {
                    //倒计时效果展示
                    String formatLongToTimeStr = formatLongToTimeStr(leftTime);
                    textView.setText(formatLongToTimeStr);
                    //每一秒执行一次
                    handler.postDelayed(this, 1000);
                }
            }
        }).start();
    }


    public String formatLongToTimeStr(Long date) {
        long day = date / (60 * 60 * 24);
        String strtime = day+"天";
        return strtime;
    }

}