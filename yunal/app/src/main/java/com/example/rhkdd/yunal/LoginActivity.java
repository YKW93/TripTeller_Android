package com.example.rhkdd.yunal;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout Rlayout1, Rlayout2;

    EditText email_edit;
    EditText pw_edit;

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

        // 상태바 삭제
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_login);

        Initialize();
    }

    private void Initialize() {

        Rlayout1 = findViewById(R.id.Rlayout1);
        Rlayout2 = findViewById(R.id.Rlayout2);

        handler.postDelayed(runnable, 2000);

        email_edit = findViewById(R.id.id_edit);
        pw_edit = findViewById(R.id.pw_edit);

        Button login_btn = findViewById(R.id.login_btn);
        Button signup_btn = findViewById(R.id.signup_btn);

        login_btn.setOnClickListener(onClickListener);
        signup_btn.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.login_btn :
//                    if (Util.getToken() != null) {
//                    activityIntent = new Intent(this, MainActivity.class);
//                } else {
//                    activityIntent = new Intent(this, LoginActivity.class);
//                }

                    String email = email_edit.getText().toString();
                    String pw = pw_edit.getText().toString();

//                    Call<LoginResponseResult> call = RetrofitApiClient.getInstance().getService().LoginResponseResult(email, pw);
//                    call.enqueue(new Callback<LoginResponseResult>() {
//                        @Override
//                        public void onResponse(Call<LoginResponseResult> call, Response<LoginResponseResult> response) {
//                            if (response.code() == 200) {
//                                Log.d("rr159", response.body().token);
//                            } else {
//                                Log.d("rr159", String.valueOf(response.code()));
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<LoginResponseResult> call, Throwable t) {
//
//                        }
//                    });

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.signup_btn :
                    Intent intent1 = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    };
}
