package com.example.myitime.ui.home;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myitime.MainActivity;
import com.example.myitime.R;
import com.example.myitime.data.Thing;
import com.example.myitime.data.ThingSaver;

import java.util.List;
import java.util.ResourceBundle;

import static com.example.myitime.MainActivity.adapter;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    ThingSaver thingSaver;
    private ResourceBundle extras;

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
                Intent intentNew = new Intent(root.getContext(),AddActivity.class);
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Thing thing = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

            ContentResolver  a = HomeFragment.this.getContext().getContentResolver();
            byte[] image_byte= thing.getImage();
            Bitmap bmp = getPicFromBytes(image_byte,null);
            ((ImageView) view.findViewById(R.id.image_view_thing)).setImageDrawable(new BitmapDrawable(getResources(),bmp));
            ((TextView) view.findViewById(R.id.title_view_thing)).setText(thing.getTitle());
            ((TextView) view.findViewById(R.id.date_view_thing)).setText(thing.getTime()[0]+"年"+thing.getTime()[1]+"月"+thing.getTime()[2]+"日");
            ((TextView) view.findViewById(R.id.tip_view_thing)).setText(thing.getTip());

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


}