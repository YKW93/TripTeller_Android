package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.common.TouchInterceptViewPager;
import com.example.rhkdd.yunal.data.detailImage.DetailImageItem;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

import static com.example.rhkdd.yunal.DetailActivity.COMMENT_IMAGE;
import static com.example.rhkdd.yunal.DetailActivity.DETAIL_IMAGES;

/**
 * Created by rhkdd on 2018-03-27.
 */

public class DetailImageSliderActivity extends AppCompatActivity {

    public static final String ClICK_POSITION = "CLICK_POSITION";
    public static final String CALLER_ACTIVITY = "CALLER_ACTIVITY";
    private int clickPosition;
    private ViewPager viewPager;

    public static Intent newIntent(Context context, ArrayList<?> list, int clickPosition, int caller) {
        Intent intent = new Intent(context, DetailImageSliderActivity.class);
        if (caller == 1) { // DetailImageActivity -> DetailImageSliderActivity 일 경우
            intent.putExtra(DETAIL_IMAGES, list);
        } else if (caller == 2) { // DetailActivity(댓글 뷰페이저 이미지 부분) -> DetailImageSliderActivity 일 경우
            intent.putExtra(COMMENT_IMAGE, list);
        }
        intent.putExtra(ClICK_POSITION, clickPosition);
        intent.putExtra(CALLER_ACTIVITY, caller);
        return intent;
    }

//    public static Intent newIntent1(Context context, ArrayList<LocationBasedItem> locationBasedLists, int clickPosition, int caller) {
//        Intent intent = new Intent(context, DetailImageSliderActivity.class);
//        intent.putExtra(LOCATIONBASED_LIST_DATA, locationBasedLists);
//        intent.putExtra(ClICK_POSITION, clickPosition);
//        intent.putExtra(CALLER_ACTIVITY, caller);
//        return intent;
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailimageslider);


        // 인텐트값 가져오기
        Intent intent = getIntent();
        int caller = intent.getIntExtra(CALLER_ACTIVITY, 0);
        if (caller == 0) { // 예외 처리
            finish();
        } else if (caller == 1) { // DetailImageActivity -> DetailImageSliderActivity 일 경우
            final ArrayList<DetailImageItem> detailImageItems = (ArrayList<DetailImageItem>) intent.getSerializableExtra(DETAIL_IMAGES);
            clickPosition = intent.getIntExtra(ClICK_POSITION, 0);


            // 사진 전체 현재 갯수 초기화
            final TextView currentTV = findViewById(R.id.imagecurrentCount);
            TextView TotalTV = findViewById(R.id.imagetotalCount);
            currentTV.setText(String.valueOf(clickPosition+1));
            TotalTV.setText(String.valueOf(detailImageItems.size()));

            // viewpage 셋팅
            viewPager = (TouchInterceptViewPager)findViewById(R.id.viewPager);
            DetailImageVPAdapter viewpageAdapter = new DetailImageVPAdapter(DetailImageSliderActivity.this);
            viewpageAdapter.setData(detailImageItems);
            viewPager.setAdapter(viewpageAdapter);
            viewPager.setCurrentItem(clickPosition);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentTV.setText(String.valueOf(viewPager.getCurrentItem() + 1));

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else if (caller == 2) {
            final ArrayList<Uri> uriLists = (ArrayList<Uri>) intent.getSerializableExtra(COMMENT_IMAGE);
            clickPosition = intent.getIntExtra(ClICK_POSITION, 0);


            // 사진 전체 현재 갯수 초기화
            final TextView currentTV = findViewById(R.id.imagecurrentCount);
            TextView TotalTV = findViewById(R.id.imagetotalCount);
            currentTV.setText(String.valueOf(clickPosition+1));
            TotalTV.setText(String.valueOf(uriLists.size()));

            // viewpage 셋팅
            viewPager = (TouchInterceptViewPager)findViewById(R.id.viewPager);
            CommentImageVPAdapter viewpageAdapter = new CommentImageVPAdapter(DetailImageSliderActivity.this);
            viewpageAdapter.setData(uriLists);
            viewPager.setAdapter(viewpageAdapter);
            viewPager.setCurrentItem(clickPosition);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentTV.setText(String.valueOf(viewPager.getCurrentItem() + 1));

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    }


    private class DetailImageVPAdapter extends PagerAdapter {

        private ArrayList<DetailImageItem> detailImageItems;
        private Context mContext;

        DetailImageVPAdapter(Context context) {
            mContext = context;
            detailImageItems = new ArrayList<>();
        }

        public void setData(ArrayList<DetailImageItem> lists) {
            detailImageItems.addAll(lists);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return detailImageItems.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_viewpager_detailimageslider, container, false);
//            ImageView imageView = v.findViewById(R.id.detailImage);
//            photoViewAttacher = new PhotoViewAttacher(imageView);
//            photoViewAttacher.update();
            PhotoView photoView = v.findViewById(R.id.detailImage);

            GlideApp.with(mContext).load(detailImageItems.get(position).originimgurl).into(photoView);
            container.addView(v);

            return v;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    }

    private class CommentImageVPAdapter extends PagerAdapter {

        private ArrayList<Uri> uriLists;
        private Context mContext;

        CommentImageVPAdapter(Context context) {
            mContext = context;
            uriLists = new ArrayList<>();
        }

        public void setData(ArrayList<Uri> lists) {
            uriLists.addAll(lists);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return uriLists.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_viewpager_detailimageslider, container, false);
//            ImageView imageView = v.findViewById(R.id.detailImage);
//            photoViewAttacher = new PhotoViewAttacher(imageView);
//            photoViewAttacher.update();
            PhotoView photoView = v.findViewById(R.id.detailImage);

            GlideApp.with(mContext).load(uriLists.get(position)).into(photoView);
            container.addView(v);

            return v;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    }

}
