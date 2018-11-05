package com.example.rhkdd.yunal.common;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatDialog;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rhkdd.yunal.R;

public class LoadingScreenHelper {

    private static LoadingScreenHelper instance = null;
    private AppCompatDialog progressDialog;

    public static LoadingScreenHelper getInstance() {
        if (instance == null) {
            instance = new LoadingScreenHelper();
        }
        return instance;
    }

    private LoadingScreenHelper() {

    }

    public void progressON(Activity activity) {

        if (activity == null || activity.isFinishing()) {
            return;
        }


        if (progressDialog != null && progressDialog.isShowing()) {

        } else {

            progressDialog = new AppCompatDialog(activity);

            progressDialog.setCancelable(false);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


            progressDialog.setContentView(R.layout.dialog_loading);

            progressDialog.show();

        }


        final ImageView img_loading_frame = progressDialog.findViewById(R.id.loading_image);
        Glide.with(activity).load(R.drawable.loading_gif).into(img_loading_frame);



    }


    public void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
