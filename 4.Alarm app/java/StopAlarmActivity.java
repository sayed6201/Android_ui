package com.sayed.alarm_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sayed.alarm_app.Services.MyAlarmService;

import java.util.Random;

public class StopAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);

        TextView questionTv= findViewById(R.id.question_tv);
        final EditText resultEt = findViewById(R.id.result);
        Button button = findViewById(R.id.submit);

        long id = getIntent().getLongExtra("id",0);


        Random ran = new Random();
        int x = ran.nextInt(15)+1;
        int y = ran.nextInt(10)+15;
        int operation = ran.nextInt(2);
        final int result;

        if(operation == 0){
            result = x + y ;
            questionTv.setText("What is the result of "+ x +" + "+y);
        }else if(operation == 1){
            result = x - y ;
            questionTv.setText("What is the result of "+ x +" - "+y);
        }else {
            result = x - y ;
            questionTv.setText("What is the result of "+ x +" - "+y);
        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(resultEt.getText().toString().trim().isEmpty()){
                    resultEt.setError("please solve the math to stop alarm");
                    Toast.makeText(StopAlarmActivity.this, "please solve the math to stop alarm", Toast.LENGTH_SHORT).show();
                    return;
                }
                int res = Integer.parseInt(resultEt.getText().toString());
                if(res != result){
                    resultEt.setError("wrong answer !");
                    Toast.makeText(StopAlarmActivity.this, "wrong answer !", Toast.LENGTH_SHORT).show();
                }else if(res == result) {
                    Toast.makeText(StopAlarmActivity.this, "Stopping service", Toast.LENGTH_SHORT).show();
                    Intent serviceIntent = new Intent(StopAlarmActivity.this, MyAlarmService.class);
                    stopService(serviceIntent);
                    finish();
                }

            }
        });


    }
}
