package com.example.rhkdd.yunal;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.input.InputManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhkdd.yunal.common.StatusBarColorChange;
import com.example.rhkdd.yunal.fragment.MainTabFragment;
import com.example.rhkdd.yunal.fragment.MypageTabFragment;
import com.example.rhkdd.yunal.fragment.InfoTabFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static com.example.rhkdd.yunal.CommentActivity.COMMENT_ITEMS;
import static com.example.rhkdd.yunal.fragment.MainTabFragment.COMMENT_SIZE;
import static com.example.rhkdd.yunal.fragment.MainTabFragment.FRAGMENT_COMMENT;

public class MainActivity extends AppCompatActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private static GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;

    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
                tabView.setMinimumWidth(0);
                // set padding to 0 for wrapping indicator as title
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                // setting custom margin between tabs
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        // left
                        settingMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        // right
                        settingMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        // internal
                        settingMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }

            tabLayout.requestLayout();
        }
    }

    private void settingMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //상태바 색상 변경
        StatusBarColorChange.setStatusBarColor(MainActivity.this, getResources().getColor(R.color.status_color));

        Initialize();

        checkPermission(); // 위치정보 권한 설정
        initGoogleAPIClient();

    }
    private void Initialize() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tab_sub1_highlight));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tab_sub2));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tab_sub3));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL); // ?
        wrapTabIndicatorToTitle(tabLayout, 70, 70);
        viewPager = findViewById(R.id.viewPager);
        // 뷰페이저 초기화면에서 미리 2개까지 로드시키는 함수
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0 :
                        tabLayout.getTabAt(0).setIcon(R.drawable.tab_sub1_highlight);
                        tabLayout.getTabAt(1).setIcon(R.drawable.tab_sub2);
                        tabLayout.getTabAt(2).setIcon(R.drawable.tab_sub3);

                        break;
                    case 1:
                        tabLayout.getTabAt(0).setIcon(R.drawable.tab_sub1);
                        tabLayout.getTabAt(1).setIcon(R.drawable.tab_sub2_highlight);
                        tabLayout.getTabAt(2).setIcon(R.drawable.tab_sub3);
                        break;
                    case  2:
                        tabLayout.getTabAt(0).setIcon(R.drawable.tab_sub1);
                        tabLayout.getTabAt(1).setIcon(R.drawable.tab_sub2);
                        tabLayout.getTabAt(2).setIcon(R.drawable.tab_sub3_highlight);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        findViewById(R.id.search).setOnClickListener(onClickListener);

        // 위치 관리자 객체 가져온다.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.search:
                    final DialogPlus dialogPlus = DialogPlus.newDialog(MainActivity.this)
                            .setContentHolder(new ViewHolder(R.layout.dialog_search))
                            .setGravity(Gravity.TOP)
                            .setOnDismissListener(new OnDismissListener() { // 다이얼로그 꺼졌을때 반응 리스너
                                @Override
                                public void onDismiss(DialogPlus dialog) {
                                    // 키보드 내리기
                                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                }
                            })
                            .create();
                    dialogPlus.show();

                    final EditText searchEdit = (EditText) dialogPlus.findViewById(R.id.testEdit);

                    // 해당 EditText에 포커스 주기
                    searchEdit.requestFocus();
                    // 키보드 자동으로 올리기
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                    searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                            switch (i) {
                                case EditorInfo.IME_ACTION_DONE : // 키보드에서 완료 버튼 클릭했을 경우
                                    String search_keyword = searchEdit.getText().toString();
                                    if (!search_keyword.isEmpty()) {
                                        dialogPlus.dismiss();
                                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                        Intent intent = SearchResultActivity.newIntent(MainActivity.this, search_keyword);
                                        startActivity(intent);

                                        return true;
                                    }
                                    break;
                            }
                            return false;
                        }
                    });

                    ImageView back_btn = (ImageView) dialogPlus.findViewById(R.id.back_btn);
                    back_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogPlus.dismiss();
                        }
                    });

                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toasty.warning(this, "한 번더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }


    }

    private void checkPermission() {
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){ // gps가 꺼져 있을 경우
                showSettingDialog(); // 구글 현재위치 다이얼로그
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//            Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_SHORT).show();
        }
    };

    private void initGoogleAPIClient() { // 구글 위치 권한 셋팅
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void showSettingDialog() { //구글 위치권한 다이얼로그
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
//                        updateGPSStatus("GPS is Enabled in your device");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");
                        break;
                    case RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel");
                        break;
                }
                break;
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private SparseArray<Fragment> fragmentSparseArray;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentSparseArray = new SparseArray<>();
            fragmentSparseArray.put(0, new MainTabFragment());
            fragmentSparseArray.put(1, new InfoTabFragment());
            fragmentSparseArray.put(2, new MypageTabFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentSparseArray.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

    }
}
