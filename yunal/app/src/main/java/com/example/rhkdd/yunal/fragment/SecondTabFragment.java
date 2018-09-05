package com.example.rhkdd.yunal.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rhkdd.yunal.DetailActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.SelectAreaActivity;
import com.example.rhkdd.yunal.common.RetrofitClient;
import com.example.rhkdd.yunal.common.TourApiService;
import com.example.rhkdd.yunal.data.areaBase.AreaBase;
import com.example.rhkdd.yunal.data.areaBase.AreaBaseItem;
import com.example.rhkdd.yunal.data.searchKeyword.SearchKeyword;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.rhkdd.yunal.SearchActivity.API_BASE_URL;
import static com.example.rhkdd.yunal.SearchActivity.API_key;

/**
 * Created by rhkdd on 2018-01-10.
 */

public class SecondTabFragment extends Fragment {

    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private AreaDataRankRVAdapter areaDataRankRVAdapter;

    private static final String TAG = "SecondFragement";
    private Boolean checkRun = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.e(TAG, "onCreateView 호출");

        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Log.e(TAG, "onViewCreated 호출");

        // recyclerview 셋팅
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false); // NestScrollView 안에 RecyclerView 설정 시, Scroll이 부드럽게 되지 않는 경우가 발생해서 해당 구문 필요
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        areaDataRankRVAdapter = new AreaDataRankRVAdapter(getActivity());
        recyclerView.setAdapter(areaDataRankRVAdapter);

        loadRankAreaData();

        Button areaBtn = view.findViewById(R.id.areaBtn);
        areaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SelectAreaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadRankAreaData() {

        final ArrayList<AreaBaseItem> lists = new ArrayList<>();

        Call<AreaBase> call = RetrofitClient.getInstance().getService(null).AreaBase(API_key, "yunal", "AND", "json",
                1, 100, null, null, "P", null);
        call.enqueue(new Callback<AreaBase>() {
            @Override
            public void onResponse(Call<AreaBase> call, Response<AreaBase> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AreaBase areaBaseData = response.body();
                    if (areaBaseData != null && areaBaseData.response.body.items.item != null) {
                        ArrayList<AreaBaseItem> areaBaseItemLists = areaBaseData.response.body.items.item;
                        for (int i = 0; i < areaBaseItemLists.size(); i++) {
                            if (areaBaseItemLists.get(i).contenttypeid != 15) {
                                lists.add(areaBaseItemLists.get(i));
                            }
                            if (lists.size() == 10) {
                                areaDataRankRVAdapter.setData(lists);
                                break;
                            }
                        }
                    }
                }            }

            @Override
            public void onFailure(Call<AreaBase> call, Throwable t) {

            }
        });
    }

    private class AreaDataRankRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;
        private ArrayList<AreaBaseItem> lists;

        AreaDataRankRVAdapter(Context context) {
            mContext = context;
            lists = new ArrayList<>();
        }

        public void setData(ArrayList<AreaBaseItem> datas) {
            lists.clear();
            lists.addAll(datas);
            notifyDataSetChanged();
        }

        private class AreaDataRankVH extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ImageView thumbIV;
            private TextView nameTV;

            AreaDataRankVH(View itemView) {
                super(itemView);
                thumbIV = itemView.findViewById(R.id.thumb);
                nameTV = itemView.findViewById(R.id.name);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (checkRun) {

                    checkRun = false;

                    int contentid = lists.get(getAdapterPosition()).contentid;
                    Intent intent = DetailActivity.newIntent(getActivity(), contentid);
                    startActivity(intent);

                    checkRun = true;
                }
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AreaDataRankVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_datatype2, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            AreaDataRankVH areaDataRankVH = (AreaDataRankVH) holder;

            if (lists.get(position).firstimage != null) {
                Glide.with(mContext).load(lists.get(position).firstimage).into(areaDataRankVH.thumbIV);
            } else {
                Glide.with(mContext).load(R.drawable.no_image).into(areaDataRankVH.thumbIV);
            }

            areaDataRankVH.nameTV.setText(lists.get(position).title);
        }

        @Override
        public int getItemCount() {
            return lists.size();
        }
    }

}
