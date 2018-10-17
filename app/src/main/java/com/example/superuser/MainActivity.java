package com.example.superuser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.superuser.model.Contast;
import com.example.superuser.services.MainServices;

public class MainActivity extends AppCompatActivity {
    private String TAG = "aaa";

    private Button btn_start_service;
    private EditText et_keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }

    private String getText() {
        String text = et_keyword.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            return text;
        } else {
            Toast.makeText(this, "请输入自动点击关键字", Toast.LENGTH_SHORT).show();
            return "unknow";
        }
    }

    private void initViews() {
        btn_start_service = findViewById(R.id.btn_start_service);
        et_keyword = findViewById(R.id.et_keyword);
    }

    private void initListeners() {
        btn_start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick");
                String text = getText();
                Intent intentbroadcast = new Intent(Contast.KEYWORD_ACTION);
                intentbroadcast.putExtra(Contast.keyword,text);
                sendBroadcast(intentbroadcast);
                Intent intent = new Intent(MainActivity.this, MainServices.class);
                intent.putExtra(Contast.keyword, text);
                startService(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
