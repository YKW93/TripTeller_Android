package com.example.rhkdd.yunal;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.common.StatusBarColorChange;
import com.example.rhkdd.yunal.model.userResponseResult.SigunupResponseError;
import com.example.rhkdd.yunal.model.userResponseResult.SignupResponseResult;
import com.google.gson.Gson;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String Passwrod_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,16}$";

    private CircleImageView user_Profile;

    private TextInputEditText userName_edit;
    private TextInputEditText id_edit;
    private TextInputEditText pw_edit;

    private TextInputLayout id_inputLayout;
    private TextInputLayout pw_inputLayout;

    private Boolean isEmailSuccess = false;
    private Boolean isPwSuccess = false;

    private File file = null;

    private boolean isChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //상태바 색상 변경
        StatusBarColorChange.setStatusBarColor(SignupActivity.this, getResources().getColor(R.color.status_color));

        Initialize();
    }

    private void Initialize() {

        user_Profile = findViewById(R.id.profile);

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
        findViewById(R.id.elbumBtn).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.signup_btn : // 회원가입
                    if (isChecked) {
                        isChecked = false;
                        if (isEmailSuccess && isPwSuccess) {
                            String userName = userName_edit.getText().toString();
                            String id = id_edit.getText().toString();
                            String pw = pw_edit.getText().toString();

                            RequestBody nickNameRequest = RequestBody.create(MediaType.parse("text/pain"), userName);
                            RequestBody IdRequest = RequestBody.create(MediaType.parse("text/pain"), id);
                            RequestBody pwRequest = RequestBody.create(MediaType.parse("text/pain"), pw);

                            if (file != null) { // 사용자 프로필 이미지를 지정했을 경우
                                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                                MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestBody);
                                sendUserInfoToServer(nickNameRequest,IdRequest, pwRequest, body);
                            } else { // 프로필 이미지를 지정 안했을 경우
                                sendUserInfoToServer(nickNameRequest,IdRequest, pwRequest, null);
                            }
                        } else {
                            // 이메일, pw 형식이 안맞음.
                            Toasty.error(SignupActivity.this, "회원가입 형식을 다시 한번 확인해주세요.", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case R.id.elbumBtn :
                    FishBun.with(SignupActivity.this).setImageAdapter(new GlideAdapter())
                            .setCamera(true)
                            .setMaxCount(1)
                            .setActionBarColor(Color.parseColor("#14b9d6"), Color.parseColor("#000000"), false)
                            .startAlbum();
                    break;
            }
        }
    };

    public void sendUserInfoToServer(RequestBody nickNameRequest, RequestBody IdRequest, RequestBody pwRequest, MultipartBody.Part body) {
        Call<SignupResponseResult> call = RetrofitServerClient.getInstance().getService().SignupResponseResult(nickNameRequest, IdRequest, pwRequest, body);
        call.enqueue(new Callback<SignupResponseResult>() {
            @Override
            public void onResponse(Call<SignupResponseResult> call, Response<SignupResponseResult> response) {
                if (response.code() == 201) { // 로그인 성공
                    Toasty.success(SignupActivity.this, "회원가입 성공", Toast.LENGTH_LONG).show();
                    finish();
                } else if (response.code() == 404) {
                    Toasty.error(SignupActivity.this, "서버와 연결할 수 없습니다,.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        String re = response.errorBody().string();
                        Gson gson = new Gson();
                        SigunupResponseError error = gson.fromJson(re,SigunupResponseError.class);
                        if(error.nickname != null) {
                            for (int i = 0; i < error.nickname.size(); i++) {
                                Toasty.error(SignupActivity.this, error.nickname.get(i), Toast.LENGTH_LONG).show();
                                Log.d("test1010", error.nickname.get(0));
                            }
                        } else if(error.email != null) {
                            for (int i = 0; i < error.email.size(); i++) {
                                Toasty.error(SignupActivity.this, error.email.get(i), Toast.LENGTH_LONG).show();
                                Log.d("test1010", error.email.get(0));
                            }
                        } else if (error.password != null) {
                            for (int i = 0; i < error.password.size(); i++) {
                                Toasty.error(SignupActivity.this, error.password.get(i), Toast.LENGTH_LONG).show();
                                Log.d("test1010", error.password.get(i));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignupResponseResult> call, Throwable t) {

            }
        });
        isChecked = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE :
                if (resultCode == RESULT_OK) {
                    Toasty.info(SignupActivity.this, "실행", Toast.LENGTH_SHORT).show();
                    ArrayList<Uri> uri = data.getParcelableArrayListExtra(Define.INTENT_PATH); // 앨범에서 선택된 이미지값 가져오기
                    GlideApp.with(SignupActivity.this).load(uri.get(0)).into(user_Profile); // 선택된 이미지 뷰로 출력
                    file = new File(getRealPathFromURI(uri.get(0))); // 이미지 절대 경로가져와 file로 변경
                    break;
                }
        }
    }

    // 이미지 절대 경로값
    private String getRealPathFromURI(Uri uri) {
        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        return cursor.getString(index);
    }

}
