package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhkdd.yunal.CommentActivity;
import com.example.rhkdd.yunal.DetailActivity;
import com.example.rhkdd.yunal.DetailImageSliderActivity;
import com.example.rhkdd.yunal.MainActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.fragment.MainTabFragment;
import com.example.rhkdd.yunal.model.mainReview.MainReviewItem;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rhkdd.yunal.fragment.MainTabFragment.FRAGMENT_COMMENT;

public class MainReviewRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<MainReviewItem> mainReviewItems;
    private int click_position;

    public MainReviewRVAdapter(Context context) {
        mContext = context;
        mainReviewItems = new ArrayList<>();
    }

    public void setData(ArrayList<MainReviewItem> data) {
        mainReviewItems.clear();
        mainReviewItems.addAll(data);
        notifyDataSetChanged();
    }

    public void changeData(MainReviewItem data) {
        mainReviewItems.set(click_position, data);
        notifyItemChanged(click_position);
    }

    private class MainReviewVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView user_profile;
        private TextView user_name;
        private TextView review_created;
        private Button tourDetailBtn;
        private RelativeLayout review_layout;
        private ImageView review_image;
        private TextView moreimageCount;
        private TextView review_content;

        private TextView like_cnt;
        private TextView comment_cnt;

        private LinearLayout like_layout;
        private ShineButton like_btn;
        private LinearLayout review_btn;

        public MainReviewVH(View itemView) {
            super(itemView);

            user_profile = itemView.findViewById(R.id.user_profile);
            user_name = itemView.findViewById(R.id.user_name);
            review_created = itemView.findViewById(R.id.review_created);
            tourDetailBtn = itemView.findViewById(R.id.tourDetailBtn);
            review_layout = itemView.findViewById(R.id.review_layout);
            review_image = itemView.findViewById(R.id.review_image);
            moreimageCount = itemView.findViewById(R.id.moreimageCount);
            review_content = itemView.findViewById(R.id.review_content);
            like_cnt = itemView.findViewById(R.id.like_cnt);
            comment_cnt = itemView.findViewById(R.id.comment_cnt);
            like_layout = itemView.findViewById(R.id.like_layout);
            like_btn = itemView.findViewById(R.id.like_btn);
            review_btn = itemView.findViewById(R.id.review_btn);

            tourDetailBtn.setOnClickListener(this);
            like_layout.setOnClickListener(this);
            review_btn.setOnClickListener(this);
            review_image.setOnClickListener(this);
            moreimageCount.setOnClickListener(this);
            comment_cnt.setOnClickListener(this);
            like_btn.setClickable(false);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tourDetailBtn :
                    Intent intent = DetailActivity.newIntent(mContext, mainReviewItems.get(getAdapterPosition()).content_id);
                    click_position = getAdapterPosition();
                    mContext.startActivity(intent);
                    break;
                case R.id.review_image :
                    Intent intent1 = DetailImageSliderActivity.newIntent(mContext, mainReviewItems.get(getAdapterPosition()).photo, 0, 2);
                    mContext.startActivity(intent1);
                    break;
                case R.id.moreimageCount :
                    Intent intent2 = DetailImageSliderActivity.newIntent(mContext, mainReviewItems.get(getAdapterPosition()).photo, 0, 2);
                    mContext.startActivity(intent2);
                    break;
                case R.id.like_layout :
                    setReviewLike(like_btn, getAdapterPosition());
                    click_position = getAdapterPosition();
                    break;
                case R.id.review_btn :
                    Intent intent3 = CommentActivity.newIntent(mContext, mainReviewItems.get(getAdapterPosition()).pk, mainReviewItems.get(getAdapterPosition()).comment);
                    click_position = getAdapterPosition();
                    mContext.startActivity(intent3);
                    break;
                case R.id.comment_cnt :
                    Intent intent4 = CommentActivity.newIntent(mContext, mainReviewItems.get(getAdapterPosition()).pk, mainReviewItems.get(getAdapterPosition()).comment);
                    click_position = getAdapterPosition();
                    mContext.startActivity(intent4);
                    break;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainReviewVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_main_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MainReviewVH mainReviewVH = (MainReviewVH) holder;

        GlideApp.with(mContext).load(mainReviewItems.get(position).image).into(mainReviewVH.user_profile);

        mainReviewVH.user_name.setText(mainReviewItems.get(position).author);

        mainReviewVH.review_created.setText(mainReviewItems.get(position).created_at);

        mainReviewVH.review_content.setText(mainReviewItems.get(position).content);


        if (mainReviewItems.get(position).photo.isEmpty()) { // 사용자가 댓글에 이미지를 추가 안했을 경우
            mainReviewVH.review_layout.setVisibility(View.GONE);
        } else { // 댓글에 이미지를 추가했을 경우 -> Viewpager 셋팅
            mainReviewVH.review_layout.setVisibility(View.VISIBLE);
            GlideApp.with(mContext).load(mainReviewItems.get(position).photo.get(0).photo).into(mainReviewVH.review_image);
            mainReviewVH.moreimageCount.setText(String.valueOf("+ " + (mainReviewItems.get(position).photo.size() -1)));
        }

        mainReviewVH.like_cnt.setText(String.valueOf("좋아요 " + mainReviewItems.get(position).like + "개"));
        if (mainReviewItems.get(position).comment.isEmpty()) { // 댓글이 없을 경우
            mainReviewVH.comment_cnt.setText(String.valueOf("댓글 0개"));
        } else { // 댓글이 있을 경우
            mainReviewVH.comment_cnt.setText(String.valueOf("댓글 " + mainReviewItems.get(position).comment.size() + "개"));
        }


        if (mainReviewItems.get(position).is_like) {
            mainReviewVH.like_btn.setChecked(true);
        } else {
            mainReviewVH.like_btn.setChecked(false);
        }


    }


    @Override
    public int getItemCount() {
        return mainReviewItems.size();
    }

    private  void setReviewLike(final ShineButton likeBtn, final int position) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("TripTeller", mContext.MODE_PRIVATE);
        final String email_id = sharedPreferences.getString("userId", "이메일 정보 없음");
        Call<ResponseBody> call = RetrofitServerClient.getInstance().getService().LikeResponseBody(mainReviewItems.get(position).pk, email_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.message().equals("Created")) {
                    if (!likeBtn.isChecked()) {
                        likeBtn.setChecked(true, true);
                        MainTabFragment.loadSingleReviewData(email_id, mainReviewItems.get(position).pk);
//                        Toasty.success(mContext, "좋아요를 눌르셨습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        likeBtn.setChecked(false);
                        MainTabFragment.loadSingleReviewData(email_id, mainReviewItems.get(position).pk);
//                        Toasty.success(mContext, "좋아요를 취소 하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }



}
