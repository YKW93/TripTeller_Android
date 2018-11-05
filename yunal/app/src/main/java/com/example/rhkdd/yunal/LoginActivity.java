package com.example.rhkdd.yunal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.model.userResponseResult.LoginResponseResult;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email_edit;
    private EditText pw_edit;
    private boolean isChecked = true;
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

        email_edit = findViewById(R.id.id_edit);
        pw_edit = findViewById(R.id.pw_edit);

        Button login_btn = findViewById(R.id.login_btn);
        Button signup_btn = findViewById(R.id.signup_btn);

        login_btn.setOnClickListener(onClickListener);
        signup_btn.setOnClickListener(onClickListener);

        // 저장된 토큰값 가져오기
//        SharedPreferences sharedPreferences = getSharedPreferences("TripTeller", MODE_PRIVATE);
//        String token = sharedPreferences.getString("userToken", "값이 없네..");
//        String id = sharedPreferences.getString("userId", "값이 없네..");
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.login_btn :
                    if (isChecked) {
                        isChecked = false;
                        String email = email_edit.getText().toString();
                        String pw = pw_edit.getText().toString();

                        Call<LoginResponseResult> call = RetrofitServerClient.getInstance().getService().LoginResponseResult(email, pw);
                        call.enqueue(new Callback<LoginResponseResult>() {
                            @Override
                            public void onResponse(Call<LoginResponseResult> call, Response<LoginResponseResult> response) {
                                if (response.code() == 200) { // 로그인 성공
                                    // 휴대폰에 로그인 토큰값 저장
                                    SharedPreferences sharedPreferences = getSharedPreferences("TripTeller", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("userToken", response.body().token);
                                    editor.putString("userId", email_edit.getText().toString());
                                    editor.apply();

                                    Toasty.normal(LoginActivity.this, "Trip Teller에 입장 하셨습니다.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    isChecked = true;
                                } else if (response.code() == 400) {
                                    try {
                                        String re = response.errorBody().string();
                                        Log.d("ttt1515", re);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Toasty.error(LoginActivity.this, "회원 정보가 없습니다.", Toast.LENGTH_LONG).show();
                                    isChecked = true;
                                } else { // 로그인 실패
                                    Toasty.error(LoginActivity.this, "서버 점검중입니다.", Toast.LENGTH_LONG).show();
                                    isChecked = true;
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginResponseResult> call, Throwable t) {
                                Toasty.error(LoginActivity.this, "서버 에러", Toast.LENGTH_LONG).show();
                                isChecked = true;
                            }
                        });
                    }


                    break;
                case R.id.signup_btn :
                    Intent intent1 = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    };
}
