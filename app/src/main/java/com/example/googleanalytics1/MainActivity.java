package com.example.googleanalytics1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAnalytics mFirebaseAnalytics ;
    Random rand = new Random();
    double start = Calendar.getInstance().getTimeInMillis() ;
    double end = 0;
    double time  = 0;
    String id = "";


    private ArrayList<Category> mArrayList = new ArrayList<>();
    RecyclerView rView;
    CategoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userid", rand.toString());
        editor.commit();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        trackScreen("main screen");

        rView = findViewById(R.id.rView);
        mArrayList.add(new Category("Food"));
        mArrayList.add(new Category("Clothes"));
        mArrayList.add(new Category("Electronics"));
        SetUpRecyclerView();

    }

    private void SetUpRecyclerView() {
        adapter = new CategoryAdapter(mArrayList, new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onclick(View v, int postion, String tag) {
                System.out.println(postion);
                getTime();

                Intent intent = new Intent(MainActivity.this, ItemPage.class);
                intent.putExtra("tag" , postion);
                startActivity(intent);
            }
        });

        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setAdapter(adapter);
    }

    void trackScreen(String screenName){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }




    void saveTime(String time , String userId , String screenName ){
        Map<String, Object> user = new HashMap<>();
        user.put("user_id", userId);
        user.put("time", time);
        user.put("screen_name",screenName);
        Log.d("finalTime1",time);

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

        saveTime(fTime , id , "MainActivity");
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