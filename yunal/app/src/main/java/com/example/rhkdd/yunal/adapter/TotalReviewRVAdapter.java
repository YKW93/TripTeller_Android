package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhkdd.yunal.DetailActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.model.tourDetail.TourReviewItem;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TotalReviewRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TourReviewItem> mLists;

    public TotalReviewRVAdapter(Context context) {
        mContext = context;
        mLists = new ArrayList<>();
    }

    public void setData(ArrayList<TourReviewItem> lists) {
        mLists.clear();
        mLists.addAll(lists);
        notifyDataSetChanged();
    }

    public class TotalReviewVH extends RecyclerView.ViewHolder {

        private ImageView userImage;
        private TextView userNickname;
        private MaterialRatingBar ratingBar;
        private TextView ratingbarTV;
        private TextView reviewDate;
        private ViewPager viewPager;
        private TextView contentTV;
        private ShineButton likeBtn;

        public TotalReviewVH(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_IMAGE);
            userNickname = itemView.findViewById(R.id.user_Nickname);
            ratingBar = itemView.findViewById(R.id.ratingbar);
            ratingbarTV = itemView.findViewById(R.id.ratingbarTV);
            reviewDate = itemView.findViewById(R.id.commentDate);
            viewPager = itemView.findViewById(R.id.commentVP);
            contentTV = itemView.findViewById(R.id.commentTV);
            likeBtn = itemView.findViewById(R.id.like_btn);

            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setReviewLike(likeBtn, getAdapterPosition());
                }
            });
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TotalReviewVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TotalReviewVH totalReviewVH = (TotalReviewVH) holder;

        totalReviewVH.userNickname.setText(mLists.get(position).author);
        totalReviewVH.ratingBar.setRating(mLists.get(position).star_score);
        totalReviewVH.ratingbarTV.setText(String.valueOf(mLists.get(position).star_score));
        totalReviewVH.reviewDate.setText(mLists.get(position).created_at);
        totalReviewVH.contentTV.setText(mLists.get(position).content);

        if (mLists.get(position).photo.isEmpty()) { // 사용자가 댓글에 이미지를 추가 안했을 경우
            totalReviewVH.viewPager.setVisibility(View.GONE);
        } else { // 댓글에 이미지를 추가했을 경우 -> Viewpager 셋팅
            totalReviewVH.viewPager.setClipToPadding(false);
            totalReviewVH.viewPager.setPadding(35,0,35,0);
            totalReviewVH.viewPager.setPageMargin(17);

            ReviewImageVPAdapter commentImageVP = new ReviewImageVPAdapter(mContext);
            commentImageVP.setData(mLists.get(position).photo);
            totalReviewVH.viewPager.setAdapter(commentImageVP);
        }

        if (mLists.get(position).like != 1) { // 해당 리뷰에 사용자가 좋아요 버튼을 클릭한적이 없을 경우
            totalReviewVH.likeBtn.setChecked(false);
        } else {
            totalReviewVH.likeBtn.setChecked(true);
        }


    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    private void setReviewLike(final ShineButton likeBtn, final int position) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("TripTeller", mContext.MODE_PRIVATE);
        String email_id = sharedPreferences.getString("userId", "이메일 정보 없음");
        Call<ResponseBody> call = RetrofitServerClient.getInstance().getService().LikeResponseBody(mLists.get(position).pk, email_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.message().equals("Created")) {
                    if (likeBtn.isChecked()) {
                        Toasty.success(mContext, "좋아요", Toast.LENGTH_SHORT).show();
                    } else {
                        Toasty.success(mContext, "좋아요 취소", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
