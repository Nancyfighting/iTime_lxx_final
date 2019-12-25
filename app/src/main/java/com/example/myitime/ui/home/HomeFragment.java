package com.example.myitime.ui.home;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
    ListView listViewThings;
    TextView textView_sheng;

//    @SuppressLint("HandlerLeak")
//    final Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 0x11) {
//
//                Bundle bundle = msg.getData();
//                int position = bundle.getInt("msg");
//                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Calendar calendar= Calendar.getInstance();
//                int year=calendar.get(Calendar.YEAR);
//                int month=calendar.get(Calendar.MONTH)+1;
//                int day=calendar.get(Calendar.DAY_OF_MONTH);
//                int hour=calendar.get(Calendar.HOUR_OF_DAY);
//                int minute = calendar.get(Calendar.MINUTE);
//                try {
//                    d2 = format.parse(year+"-"+month+"-"+day+" "+hour +":"+minute+":00");
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                int[]date_clock=listThings.get(position).getTime();
//                Date d1 = null;
//                try {
//                    d1 = format.parse(date_clock[0]+"-"+date_clock[1]+"-"+date_clock[2]+" "+date_clock[3]+":"+date_clock[4]+":00");
//                } catch (ParseException e) {
//                    e.printStackTrace();
//
//                }
//
//                leftTime = (d1.getTime() - d2.getTime())/1000;//这样得到的差值是微秒级别
//                leftTime--;
//
//
//                if (leftTime > 0) {
//                    //倒计时效果展示
//                    String formatLongToTimeStr = formatLongToTimeStr(leftTime);
////                    View view = LayoutInflater.from(getContext()).inflate(position, listViewThings, false);
//                    textView_sheng.setText(formatLongToTimeStr);
//                    handler.sendEmptyMessageDelayed(0x11,1000);
//                    Log.v("w","ff");
//                }
//            }
//        }
//    };
    private long leftTime ;//一分钟
    ThingSaver thingSaver;
    Date d2 = null;
    private ResourceBundle extras;
    private  Calendar calendar= Calendar.getInstance();;
    private int year_lst=calendar.get(Calendar.YEAR);
    private int month_lst=calendar.get(Calendar.MONTH);
    private int day_lst=calendar.get(Calendar.DAY_OF_MONTH);
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
        listThings=thingSaver.load();
        Bitmap cameraPhoto_lim=BitmapFactory.decodeResource(getResources(),R.drawable.mole2);
        ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
        cameraPhoto_lim.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
        image_moren = output.toByteArray();//转换成功了  result就是一个bit的资源数组
        if(listThings.size()==0)
        {
            int [] date_add=new int[]{2020,2,11,18,30};
            listThings.add(new Thing("做作业","安卓",date_add, image_moren,"每天"));
        }
        listViewThings=root.findViewById(R.id.list_view_things);
        adapter = new ThingAdapter(HomeFragment.this.getActivity(),R.layout.list_view_item_thing, listThings);
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
                Intent intentNew = new Intent(root.getContext(),EditActivity.class);
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

        @SuppressLint("HandlerLeak")
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Thing thing = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);



            ContentResolver  a = HomeFragment.this.getContext().getContentResolver();
            byte[] image_byte= thing.getImage();
            Bitmap bmp = getPicFromBytes(image_byte,null);
            view.findViewById(R.id.conslayout_daojishi).setBackground((new BitmapDrawable(getResources(),bmp)));
            textView_sheng = view.findViewById(R.id.textView_sheng);
            ((TextView) view.findViewById(R.id.title_view_thing)).setText(thing.getTitle());
            ((TextView) view.findViewById(R.id.date_view_thing)).setText(thing.getTime()[0]+"年"+thing.getTime()[1]+"月"+thing.getTime()[2]+"日");
            ((TextView) view.findViewById(R.id.tip_view_thing)).setText(thing.getTip());
            int day_l = (thing.getTime()[0]-year_lst)*365+(thing.getTime()[1]-month_lst)*30+(thing.getTime()[2]-day_lst);
            textView_sheng.setText(day_l+"天");


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

//    private void startRun( ) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                Bundle bundle = new Bundle();
////                bundle.putInt("msg",position);
//                Message message = Message.obtain();
//                message.what = 0x11;
//                handler.sendMessage(message);
////每一秒执行一次
////        handler.postDelayed(this, 1000);
////                sendMessageDelayed(Message msg)
//            }
//        }).start();
//    }





    public String formatLongToTimeStr(Long date) {
        long day = date / (60 * 60 * 24);
        String strtime = day+"天";
        return strtime;
    }

}