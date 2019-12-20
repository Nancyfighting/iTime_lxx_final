package com.example.myitime.data;

import android.content.Context;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ThingSaver {
    public ThingSaver(Context context) { this.context = context; }
    Context context;
    ArrayList<Thing> things=new  ArrayList<Thing>();
    public void save() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(context.openFileOutput("Serializable.txt",Context.MODE_PRIVATE));
            outputStream.writeObject(things);
            outputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<Thing> load(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(context.openFileInput("Serializable.txt"));
            things = (ArrayList<Thing>) inputStream.readObject();
            inputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return things;
    }
}
