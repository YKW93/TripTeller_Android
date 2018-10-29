package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rhkdd.yunal.adapter.CommentRVAdapter;
import com.example.rhkdd.yunal.model.mainReview.CommentItem;

import org.w3c.dom.Comment;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class CommentActivity extends AppCompatActivity {

    public static final String COMMENT_ITEMS = "COMMENT_ITEMS";
    public static Intent newIntent(Context context, ArrayList<CommentItem> commentItems) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(COMMENT_ITEMS, commentItems);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Initialize();
    }

    private void Initialize() {
        //툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);

        //인텐트값 가져오기
        Intent intent = getIntent();
        ArrayList<CommentItem> commentItems = (ArrayList<CommentItem>) intent.getSerializableExtra(COMMENT_ITEMS);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommentRVAdapter commentRVAdapter = new CommentRVAdapter(CommentActivity.this);
        recyclerView.setAdapter(commentRVAdapter);

        if (!commentItems.isEmpty()) {
            commentRVAdapter.setData(commentItems);
       } else {
            Toasty.warning(CommentActivity.this, "댓글이 없네용 ^^ " , Toast.LENGTH_SHORT).show();
        }

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
