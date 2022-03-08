package com.jsd.red_med;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;

import android.text.TextUtils;

import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class AddMed extends AppCompatActivity implements TimePickerDialog .OnTimeSetListener  {

    ImageView img_back2main;
    EditText ed_medname, ed_medtype,Txt_time;
    Button btn_save;
    String str_medname;
    String str_medtype;

    Button btn_time,btn_cancel;
    TextView txt_time;

    String email;
    String id;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    ProgressDialog pd;

    String pName,pType,pId,pTime;

    String timeText;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(this);

        img_back2main = findViewById(R.id.img_back2main);
        ed_medname = findViewById(R.id.ed_medname);
        ed_medtype = findViewById(R.id.ed_medtype);
        btn_save = findViewById(R.id.btn_save);
        txt_time = findViewById(R.id.txt_time);



        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        email = user.getEmail();

        bundle = getIntent().getExtras();
        if(bundle != null){
            btn_save.setText("Update");

            pId = bundle.getString("pId");
            pName = bundle.getString("pName");
            pType = bundle.getString("pType");
            pTime = bundle.getString("pTime");

            ed_medname.setText(pName);
            ed_medtype.setText(pType);
            txt_time.setText(pTime);
        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = getIntent().getExtras();
                if(bundle !=  null){
                    str_medname = ed_medname.getText().toString().trim();
                    str_medtype = ed_medtype.getText().toString().trim();
                    timeText = txt_time.getText().toString().trim();
                    updateData(str_medname,str_medtype,timeText);
                }
                else {
                    str_medname = ed_medname.getText().toString().trim();
                    str_medtype = ed_medtype.getText().toString().trim();
                    timeText = txt_time.getText().toString().trim();
                    if (str_medname.isEmpty() || str_medtype.isEmpty()) {
                        Toast.makeText(AddMed.this, "Empty Fields are not allowed.", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadData(str_medname, str_medtype, timeText);
                    }
                }
            }
        });




        img_back2main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddMed.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        btn_time = (Button) findViewById(R.id.btn_time);
        txt_time = (TextView) findViewById(R.id.txt_time);

        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
    }

    private void updateData(String str_medname, String str_medtype, String time) {
        pd.setTitle("Updating Medicine");
        pd.show();

        //String id =  db.collection("User/"+email+"/UserAlarms/").getId();
        //////////////////////////////////////////////////////////////////////
        db.collection("User").document(email).collection("UserAlarms").document(pId)
                .update("med_name",str_medname,"med_type",str_medtype,"time",time)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        startActivity(new Intent(AddMed.this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(AddMed.this, "Failed..", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void uploadData(String str_medname, String str_medtype, String time) {
        pd.setTitle("Adding Medicine");
        pd.show();

        id = UUID.randomUUID().toString();

       UploadDataFireStore up = new UploadDataFireStore(str_medtype, str_medname, id, time);
        db.collection("User").document(email).collection("UserAlarms").document(id).set(up)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        startActivity(new Intent(AddMed.this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(AddMed.this, "Not Saved", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        AlarmManager alarmManager =(AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Date date = new Date();

        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();

        cal_now.setTime(date);
        cal_alarm.setTime(date);


        cal_alarm.set(Calendar.HOUR_OF_DAY,hourOfDay);
        cal_alarm.set(Calendar.MINUTE,minute);
        cal_alarm.set(Calendar.SECOND,0);

        updateTimeText(cal_alarm);
        startAlarm(cal_alarm);



        if (cal_alarm.before(cal_now))
        {
            cal_alarm.add(Calendar.DATE,1);
        }

        Intent i = new Intent(AddMed.this,MyBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddMed.this,2444,i,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(),pendingIntent);
    }

    private void updateTimeText(Calendar c) {
        String timeText = " ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        txt_time.setText(timeText);
    }
    @SuppressLint("NewApi")
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }



      private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.cancel(pendingIntent);
        txt_time.setText(" ");
    }

}