package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhkdd.yunal.common.GlideApp;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;


/**
 * Created by rhkdd on 2018-04-09.
 */

public class ReviewActivity extends AppCompatActivity {

    public static final String DETAIL_TITLE = "DETAIL_TITLE";
    public static final String REVIEW_EDIT = "REVIEW_EDIT";
    public static final String RATING_NUM = "RATING_NUM";
    public static final String REVIEW_IMAGES = "REVIEW_IMAGES";
    public static final String REVIEW_DATE = "REVIEW_DATE";

    private RecyclerView picturesRV;
    private PicturesRVAdapter picturesRVAdapter;
    private ArrayList<Uri> urilist;
    private int imageCount = 10;

    private TextView saveBtn;
    private RelativeLayout picture_Layout;
    private EditText reviewEdit;
    private TextView ratingBarTV;


    public static Intent newIntent(Context context, String placeName) {
        Intent intent = new Intent(context, ReviewActivity.class);
        intent.putExtra(DETAIL_TITLE, placeName);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        saveBtn = findViewById(R.id.saveBtn);
        picture_Layout = findViewById(R.id.picture_layout);
        reviewEdit = findViewById(R.id.reviewEdit);

        urilist = new ArrayList<>();

        //intent 값 가져오기
        Intent intent = getIntent();
        String placeNmae = intent.getStringExtra(DETAIL_TITLE);

        //툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);

        //장소명 셋팅
        TextView placeNameTV = findViewById(R.id.placeName);
        placeNameTV.setText("장소명 : " + placeNmae);

        //ratingbar 숫자 셋팅
        MaterialRatingBar ratingBar = findViewById(R.id.ratingbar);
        ratingBar.setRating(5);
        ratingBarTV = findViewById(R.id.ratingbarTV);
        ratingBar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                String ratingNum = Float.toString(rating);
                ratingBarTV.setText(ratingNum);
            }
        });

        //앨범에서 사진 가져오기
        Button albumBtn = findViewById(R.id.albumBtn);
        albumBtn.setOnClickListener(onClickListener);

        // 저장 버튼 리스너 셋팅
        saveBtn.setOnClickListener(onClickListener);

        // 리사이클러뷰 셋팅
        picturesRV = findViewById(R.id.image_Recyclerview);
        picturesRV.setLayoutManager(new LinearLayoutManager(ReviewActivity.this, LinearLayoutManager.HORIZONTAL, false));
        picturesRVAdapter = new PicturesRVAdapter(ReviewActivity.this);
        picturesRV.setAdapter(picturesRVAdapter);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            switch (id) {
                case R.id.albumBtn :
                    if (imageCount != 0) {
                        FishBun.with(ReviewActivity.this).setImageAdapter(new GlideAdapter())
                                .setCamera(true)
                                .setMaxCount(imageCount)
                                .setActionBarColor(Color.parseColor("#14b9d6"), Color.parseColor("#000000"), false)
                                .startAlbum();
                    } else {
                        Toasty.error(ReviewActivity.this, "최대 10장 까지 추가 할 수 있습니다.", Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.saveBtn :
                    if ((reviewEdit.getText().toString().length() > 20)) {
                        // 작성완료 날짜 구하기
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String getTime = simpleDateFormat.format(date);

                        // 작성한 댓글 데이터 DetailActivity로 넘겨주기
                        Intent intent = new Intent();
                        intent.putExtra(REVIEW_EDIT, reviewEdit.getText().toString());
                        intent.putExtra(RATING_NUM, ratingBarTV.getText().toString());
                        intent.putExtra(REVIEW_IMAGES, picturesRVAdapter.getDatas());
                        intent.putExtra(REVIEW_DATE, getTime);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);
                        builder.setTitle("리뷰 하기");
                        builder.setMessage("20자 이상 작성해주세요.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();
                    }
                    break;

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE :
                if (resultCode == RESULT_OK) {
                    urilist = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    imageCount = imageCount - urilist.size();
                    picturesRVAdapter.setDatas(urilist);
                    findViewById(R.id.saveBtn).setClickable(true);
                    break;
                }
        }
    }

    //휴대폰 뒤로가기 버튼클릭시
    @Override
    public void onBackPressed() {
        dialogShow();
//        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home :
                dialogShow();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void dialogShow() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("작성 취소");
        builder.setMessage("변경사항을 잃어버립니다.\n계속하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private class PicturesRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mcontext;
        ArrayList<Uri> imagesList;

        private PicturesRVAdapter(Context context) {
            mcontext = context;
            imagesList = new ArrayList<>();
        }

        private void setDatas(ArrayList<Uri> list) {
            imagesList.addAll(list);
            if (imagesList == null) {
                picture_Layout.setVisibility(View.GONE);
                picturesRV.setVisibility(View.GONE);
            } else {
                picturesRV.setVisibility(View.VISIBLE);
                picture_Layout.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
            }

        }

        private ArrayList<Uri> getDatas() {
            return imagesList;
        }

        private class PicturesVH extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ImageView reviewImage;
            private ImageView reviewImageRemove;

            public PicturesVH(View itemView) {
                super(itemView);
                reviewImage = itemView.findViewById(R.id.reviewImage);
                reviewImageRemove = itemView.findViewById(R.id.reviewImageRemove);

                reviewImageRemove.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.reviewImageRemove :
                        imagesList.remove(getAdapterPosition());
                        imageCount++;
                        if (imagesList.isEmpty()) {
                            RelativeLayout picturelayout = findViewById(R.id.picture_layout);
                            picturesRV.setVisibility(View.GONE);
                            picturelayout.setVisibility(View.GONE);
                        }
                        notifyDataSetChanged();
                }
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PicturesVH(LayoutInflater.from(mcontext).inflate(R.layout.item_recyclerview_reviewimage, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            PicturesVH pictruesVH = (PicturesVH) holder;
            GlideApp.with(mcontext).load(imagesList.get(position)).into(pictruesVH.reviewImage);

        }

        @Override
        public int getItemCount() {
            return imagesList.size();
        }
    }

}
