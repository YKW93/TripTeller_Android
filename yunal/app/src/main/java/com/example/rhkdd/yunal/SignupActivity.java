package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.model.userResponseResult.SigunupResponseError;
import com.example.rhkdd.yunal.model.userResponseResult.SignupResponseResult;
import com.google.gson.Gson;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    EditText userName_edit;
    EditText id_edit;
    EditText pw_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Initialize();
    }

    private void Initialize() {
        userName_edit = findViewById(R.id.userName_edit);
        id_edit = findViewById(R.id.id_edit);
        pw_edit = findViewById(R.id.pw_edit);

        findViewById(R.id.signup_btn).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.signup_btn : // 회원가입
                    String userName = userName_edit.getText().toString();
                    String id = id_edit.getText().toString();
                    String pw = pw_edit.getText().toString();

                    Call<SignupResponseResult> call = RetrofitServerClient.getInstance().getService().SignupResponseResult(userName, id, pw);
                    Log.d("test1010", String.valueOf(call.request().url()));
                    call.enqueue(new Callback<SignupResponseResult>() {
                        @Override
                        public void onResponse(Call<SignupResponseResult> call, Response<SignupResponseResult> response) {
                            Log.d("test1010", String.valueOf(response.code()));
                            if (response.code() == 201) { // 로그인 성공
                                Toasty.success(SignupActivity.this, "로그인 성공", Toast.LENGTH_LONG).show();
                                finish();
                            } else if (response.code() == 404) {
                                Toasty.error(SignupActivity.this, "서버와 연결할 수 없습니다,.", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    String re = response.errorBody().string();
                                    Gson gson = new Gson();
                                    SigunupResponseError error = gson.fromJson(re,SigunupResponseError.class);

                                    if(error.nickname != null) {
                                        Log.d("test1010", error.nickname.get(0));
                                    } else if(error.email != null) {
                                        Log.d("test1010", error.email.get(0));
                                    } else if (error.password != null) {
                                        for (int i = 0; i < error.password.size(); i++) {
                                            Log.d("test1010", error.password.get(i));
                                            Toasty.error(SignupActivity.this, error.password.get(i), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SignupResponseResult> call, Throwable t) {
                            Log.d("test1010", "실패");
                        }
                    });

//                    Log.d("test1010" , "userName : " + userName + "ID : " + id + "pw : " + pw);

                break;
            }
        }
    };

}
