package com.example.rhkdd.yunal;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.model.userResponseResult.SigunupResponseError;
import com.example.rhkdd.yunal.model.userResponseResult.SignupResponseResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String Passwrod_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,16}$";

    private TextInputEditText userName_edit;
    private TextInputEditText id_edit;
    private TextInputEditText pw_edit;

    private TextInputLayout id_inputLayout;
    private TextInputLayout pw_inputLayout;

    private Boolean isEmailSuccess;
    private Boolean isPwSuccess;

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

        id_inputLayout = findViewById(R.id.id_inputlayout);
        pw_inputLayout = findViewById(R.id.pw_inputlayout);

        id_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String id_text = id_edit.getText().toString().trim();

                if (!Pattern.matches(EMAIL_REGEX, id_text)) {
                    id_inputLayout.setError("이메일 오류");
                    isEmailSuccess = false;
                } else {
                    id_inputLayout.setError(null);
                    isEmailSuccess = true;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pw_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pw_text = pw_edit.getText().toString().trim();
                if (!Pattern.matches(Passwrod_PATTERN, pw_text)) {
                    pw_inputLayout.setError("영문과 숫자를 혼합해 주세요.(최소 8자 이상)");
                    isPwSuccess = false;
                } else {
                    pw_inputLayout.setError(null);
                    isPwSuccess = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        findViewById(R.id.signup_btn).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.signup_btn : // 회원가입
                    if (isEmailSuccess && isPwSuccess) {
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
                        break;
                    } else {
                        // 이메일, pw 형식이 안맞음.
                        Toasty.error(SignupActivity.this, "회원가입 형식을 다시 한번 확인해주세요.", Toast.LENGTH_LONG).show();
                    }
            }
        }
    };

}
