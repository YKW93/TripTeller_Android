package com.example.rhkdd.yunal;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout Rlayout1, Rlayout2;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Rlayout1.setVisibility(View.VISIBLE);
            Rlayout2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Rlayout1 = findViewById(R.id.Rlayout1);
        Rlayout2 = findViewById(R.id.Rlayout2);

        handler.postDelayed(runnable, 2000);

        Button login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (Util.getToken() != null) {
//                    activityIntent = new Intent(this, MainActivity.class);
//                } else {
//                    activityIntent = new Intent(this, LoginActivity.class);
//                }

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
