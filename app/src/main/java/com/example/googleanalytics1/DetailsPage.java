package com.example.googleanalytics1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsPage extends AppCompatActivity {
    Intent intent;
    ImageView imageView ;
    TextView itemNameTextView ;
    TextView itemSpecificationTextView;
    private FirebaseAnalytics mFirebaseAnalytics ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    double start = Calendar.getInstance().getTimeInMillis() ;
    double end = 0;
    double time  = 0;
    String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);
        imageView = findViewById(R.id.item_image);
        itemNameTextView = findViewById(R.id.item_name);
        itemSpecificationTextView = findViewById(R.id.item_specification);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        trackScreen("DetailsPage");
        //getTime();
        intent = getIntent();
        int image = intent.getIntExtra("image",-1);
        String name = intent.getStringExtra("name");
        String specification = intent.getStringExtra("speci");
        imageView.setImageResource(image);
        itemNameTextView.setText(name);
        itemSpecificationTextView.setText(specification);

    }
    void trackScreen(String screenName){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "DetailsPage");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }




    void saveTime(String time , String userId , String screenName ){
        Map<String, Object> user = new HashMap<>();
        user.put("user_id", userId);
        user.put("time", time);
        user.put("screen_name",screenName);
        Log.d("finalTime3",time);

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

        saveTime(fTime , id , "DetailsPage");
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