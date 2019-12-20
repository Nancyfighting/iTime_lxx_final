package com.example.myitime.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myitime.MainActivity;
import com.example.myitime.R;
import com.example.myitime.data.Thing;
import com.example.myitime.data.ThingSaver;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private List<Thing> listThings=new ArrayList<>();

    ThingAdapter adapter;
    ThingSaver thingSaver;
    public void onDestroy() {
        super.onDestroy();
        thingSaver.save();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        FloatingActionButton fab = findViewById(R.id.fab);

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        thingSaver=new ThingSaver(root.getContext());
        listThings=thingSaver.load();
        if(listThings.size()==0)
            init();
        ListView listViewThings=root.findViewById(R.id.list_view_things);
        adapter = new ThingAdapter(HomeFragment.this.getActivity(),R.layout.list_view_item_thing,listThings);
        listViewThings.setAdapter(adapter);

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

    private void init() {
        listThings.add( new Thing("进来", "玩","12月12日",R.drawable.mole));
        listThings.add( new Thing("双人", "哈哈","12月12日",R.drawable.mole));
    }
    public class ThingAdapter extends ArrayAdapter<Thing> {

        private int resourceId;

        public ThingAdapter(Context context, int resource, List<Thing> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Thing thing = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ((ImageView) view.findViewById(R.id.image_view_thing)).setImageResource(thing.getResourceId());
            ((TextView) view.findViewById(R.id.title_view_thing)).setText(thing.getTitle());
            ((TextView) view.findViewById(R.id.date_view_thing)).setText(thing.getTime());
            ((TextView) view.findViewById(R.id.tip_view_thing)).setText(thing.getTip());

            return view;
        }
    }
}