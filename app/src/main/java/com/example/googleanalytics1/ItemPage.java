package com.example.googleanalytics1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ItemPage extends AppCompatActivity {
    Intent intent;
    RecyclerView rView;
    ItemAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAnalytics mFirebaseAnalytics ;
    double start = Calendar.getInstance().getTimeInMillis() ;
    double end = 0;
    double time  = 0;
    String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        trackScreen("ItemPage");

        rView = findViewById(R.id.rView2);
         intent = getIntent();
         int id = intent.getIntExtra("tag",-1);
         if(id == 0 ){
             Log.d("TAG" , "Food");
             ArrayList<Item> mArrayList = new ArrayList<>();
             mArrayList.add(new Item("Shawerma" , R.drawable.shawerma , "spicy \nhot "));
             mArrayList.add(new Item("Chicken" , R.drawable.chicken , "for 3 person \nhot "));
             mArrayList.add(new Item("Kabsa" , R.drawable.kabsa , "Spicy \nfor 2 person "));
             SetUpRecyclerView(mArrayList);
         }else if(id == 1){
             Log.d("TAG" , "Clothes");
             ArrayList<Item> mArrayList = new ArrayList<>();
             mArrayList.add(new Item("Blouse" , R.drawable.clothes1 , "age : 3-5 y \ncolor : white , blue "));
             mArrayList.add(new Item("Baby suit" , R.drawable.clothes2 , "age : 0-2 y \ncolor : red , blue"));
             mArrayList.add(new Item("Jumpsuit" , R.drawable.js4 , "age : 4-10 y \ncolor : blue , pink"));
             SetUpRecyclerView(mArrayList);
         }else if(id == 2 ){
            Log.d("TAG" , "Electronic");
             ArrayList<Item> mArrayList = new ArrayList<>();
             mArrayList.add(new Item("Laptop" , R.drawable.laptop , "Ram : 15  \ncpu : 20  "));
             mArrayList.add(new Item("Iphone" , R.drawable.iphonee , "color : white \nmemory : 64 "));
             mArrayList.add(new Item("TV" , R.drawable.tv , "size : 50  \ncolor : black "));
             SetUpRecyclerView(mArrayList);
         }else {
             Log.d("TAG" , "Error");
         }


    }

    private void SetUpRecyclerView(ArrayList<Item> mArrayList ) {
        adapter = new ItemAdapter(mArrayList, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onclick(View v, int postion, String tag) {
                System.out.println(postion);
                Log.d("TAG" , String.valueOf(postion));
                getTime();
                Intent intent = new Intent(ItemPage.this, DetailsPage.class);
                intent.putExtra("image" , mArrayList.get(postion).getItemImage());
                intent.putExtra("name" , mArrayList.get(postion).getItemName());
                intent.putExtra("speci" , mArrayList.get(postion).getSpecifications());
                startActivity(intent);
            }
        });

        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setAdapter(adapter);
    }
    void trackScreen(String screenName){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "ItemPage");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }




    void saveTime(String time , String userId , String screenName ){
        Map<String, Object> user = new HashMap<>();
        user.put("user_id", userId);
        user.put("time", time);
        user.put("screen_name",screenName);
        Log.d("finalTime2",time);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "Success to add to DB");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Faild to add to DB" + e);
                    }
                });
    }
    void getTime(){
        start= end ;
        end  = 0 ;
        end = Calendar.getInstance().getTimeInMillis();
        Log.i("time" , end+"end");
        time =  end-start;
        int tmin = (int)(Math.floor(time/1000/60));
        double tsec = (time/1000)%60;
        String  fTime = tmin +"m " +tsec + "s" ;

        Log.i("time" , time+"time");
        SharedPreferences sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        id = sharedPref.getString("userid", "");
        Log.i("sharedPref" , id);

        saveTime(fTime , id , "ItemPage");
    }
    @Override
    protected void onResume() {
        super.onResume();
        start= end ;
        end  = 0 ;
        end = Calendar.getInstance().getTimeInMillis();
        Log.i("time" , end+"end");
        time =  end-start;
        double tmin =time/1000/60;
        double tsec = (time/1000)%60;
        String  fTime = tmin +" m " +tsec + " s" ;



    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        getTime();
    }

}