package com.example.superuser.superuser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private Button btn_start_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }

    private void initViews() {
        btn_start_service = findViewById(R.id.btn_start_service);
    }

    private void initListeners() {
        btn_start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("aaa", "onClick");
//                Intent intent = new Intent(MainActivity.this, MainServices.class);
//                startService(intent);
                do_exec("adb shell input swipe 400 600 400 200");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void do_exec(String cmdString) {
        Log.i("aaa", "在cmd里面输入" + cmdString);
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmdString);
            // p.waitFor();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(
                    p.getInputStream(), "gbk"));
            String line = null;
            while ((line = bReader.readLine()) != null)
                Log.i("aaa", "line" + line);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
