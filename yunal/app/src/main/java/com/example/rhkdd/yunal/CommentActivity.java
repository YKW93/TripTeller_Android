package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.rhkdd.yunal.adapter.CommentRVAdapter;
import com.example.rhkdd.yunal.adapter.MainReviewRVAdapter;
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.common.StatusBarColorChange;
import com.example.rhkdd.yunal.fragment.MainTabFragment;
import com.example.rhkdd.yunal.model.mainReview.CommentItem;


import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    private String email_id;

    private  ArrayList<CommentItem> commentItems;
    private int review_pk;
    private int review_position;

    private EditText comment_content_Edit;
    private ImageButton comment_sendBtn;

    private CommentRVAdapter commentRVAdapter;

    public static final String COMMENT_ITEMS = "COMMENT_ITEMS";
    public static final String REVIEW_PK = "REVIEW_PK";

    public static Intent newIntent(Context context, int pk, ArrayList<CommentItem> commentItems) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(REVIEW_PK, pk);
        intent.putExtra(COMMENT_ITEMS, commentItems);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        //상태바 색상 변경
        StatusBarColorChange.setStatusBarColor(CommentActivity.this, getResources().getColor(R.color.status_color));

        Initialize();
    }

    private void Initialize() {

        // 휴대폰 내에 저장된 사용자 email 값 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("TripTeller", MODE_PRIVATE);
        email_id = sharedPreferences.getString("userId", "이메일 정보 없음");

        //툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);

        //인텐트값 가져오기
        Intent intent = getIntent();
        commentItems = (ArrayList<CommentItem>) intent.getSerializableExtra(COMMENT_ITEMS);
        review_pk = intent.getIntExtra(REVIEW_PK, 0);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRVAdapter = new CommentRVAdapter(CommentActivity.this);
        recyclerView.setAdapter(commentRVAdapter);

        if (!commentItems.isEmpty()) { // 댓글이 있을 경우에만 어댑터에 데이터를 넘겨줌
            commentRVAdapter.setData(commentItems);
       }

        comment_content_Edit = findViewById(R.id.comment_content_Edit);
        comment_sendBtn = findViewById(R.id.comment_send_Btn);

        comment_sendBtn.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.comment_send_Btn :
                    if (comment_content_Edit.getText().toString().isEmpty()) {
                        Toasty.warning(CommentActivity.this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        sendCommentToServer();
                    }
                    break;
            }
        }
    };

    private void sendCommentToServer() { // 댓글 서버로 전송

        String content = comment_content_Edit.getText().toString();

        Call<CommentItem> call = RetrofitServerClient.getInstance().getService().CommentItemResponseResult(review_pk, email_id, content);

        call.enqueue(new Callback<CommentItem>() {
            @Override
            public void onResponse(Call<CommentItem> call, Response<CommentItem> response) {
                if (response.isSuccessful()) {
                    CommentItem commentItem = response.body();
                    if (commentItem != null) {
                        commentRVAdapter.addData(commentItems.size() + 1, commentItem); // 리사이클러뷰 마지막 아이템에 해당 댓글 데이터 추가
                        comment_content_Edit.setText(null); // EditText 비우기
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentItem> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainTabFragment.loadSingleReviewData(email_id, review_pk);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home :
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
