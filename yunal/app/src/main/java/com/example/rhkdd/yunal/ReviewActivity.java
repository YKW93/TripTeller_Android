package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.common.StatusBarColorChange;
import com.example.rhkdd.yunal.model.detailCommon.DetailCommonItem;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by rhkdd on 2018-04-09.
 */

public class ReviewActivity extends AppCompatActivity {

    private static final String TAG = ReviewActivity.class.getSimpleName();

    public static final String DETAIL_COMMON = "DETAIL_COMMON";

    private RecyclerView picturesRV;
    private ReviewImageRVAdapter picturesRVAdapter;
    private ArrayList<Uri> urilist;
    private int imageCount = 10;

    private DetailCommonItem detailCommonItem;

    private TextView saveBtn;
    private RelativeLayout picture_Layout;
    private EditText reviewEdit;
    private TextView ratingBarTV;

    private Boolean checkRun = true;

    public static Intent newIntent(Context context, DetailCommonItem detailCommonItem) {
        Intent intent = new Intent(context, ReviewActivity.class);
        intent.putExtra(DETAIL_COMMON, detailCommonItem);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //상태바 색상 변경
        StatusBarColorChange.setStatusBarColor(ReviewActivity.this, getResources().getColor(R.color.status_color));


        Initialize();
    }

    private void Initialize() {
        //툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);

        saveBtn = findViewById(R.id.saveBtn);
        picture_Layout = findViewById(R.id.picture_layout);
        reviewEdit = findViewById(R.id.reviewEdit);

        urilist = new ArrayList<>();

        //intent 값 가져오기
        Intent intent = getIntent();
        detailCommonItem = (DetailCommonItem) intent.getSerializableExtra(DETAIL_COMMON);

        //장소명 셋팅
        TextView placeNameTV = findViewById(R.id.placeName);
        placeNameTV.setText("장소명 : " + detailCommonItem.title);

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
        picturesRVAdapter = new ReviewImageRVAdapter(ReviewActivity.this);
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
                        if (checkRun) {
                            checkRun = false;
                            sendReviewToServer();
                        }

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

    // 서버로 댓글 데이터 전송
    private void sendReviewToServer() {

        SharedPreferences sharedPreferences = getSharedPreferences("TripTeller", MODE_PRIVATE);
        String email_id = sharedPreferences.getString("userId", "이메일 정보 없음");
        String token = sharedPreferences.getString("userToken", "토큰 정보 없음");

        ArrayList<MultipartBody.Part> images = new ArrayList<>();

        for (int i = 0; i < urilist.size(); i++) {
            File file = new File(getRealPathFromURI(urilist.get(i)));
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            images.add(MultipartBody.Part.createFormData("photo", file.getName(), requestBody));
        }
        RequestBody emailIdRequest = RequestBody.create(MediaType.parse("text/pain"), email_id);
        RequestBody contentRequest = RequestBody.create(MediaType.parse("text/plain"), reviewEdit.getText().toString());
        RequestBody ratingRequest = RequestBody.create(MediaType.parse("text/plain"), ratingBarTV.getText().toString());
        RequestBody contentIdRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(detailCommonItem.contentid));
        RequestBody areaCodeRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(detailCommonItem.areacode));
        RequestBody sigunguCodeRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(detailCommonItem.sigungucode));


//                        MultipartBody.Part image = images.get(0);
        Call<ResponseBody> call = RetrofitServerClient.getInstance().getService().reviewResponseBody(token, emailIdRequest, contentIdRequest, contentRequest,
                images, ratingRequest, areaCodeRequest, sigunguCodeRequest);
        Log.d(TAG,"서버 호출 주소: " +  String.valueOf(call.request().url()));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful() && response.body() != null && response.message().equals("Created")) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

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

    private class ReviewImageRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mcontext;
        ArrayList<Uri> imagesList;

        private ReviewImageRVAdapter(Context context) {
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
