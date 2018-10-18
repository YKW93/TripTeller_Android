package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.rhkdd.yunal.model.tourType.CultureItem;
import com.example.rhkdd.yunal.model.tourType.FestivalItem;
import com.example.rhkdd.yunal.model.tourType.FoodItem;
import com.example.rhkdd.yunal.model.tourType.LeportsItem;
import com.example.rhkdd.yunal.model.tourType.LodgingItem;
import com.example.rhkdd.yunal.model.tourType.ShoppingItem;
import com.example.rhkdd.yunal.model.tourType.TouristItem;

import java.io.Serializable;

/**
 * Created by rhkdd on 2018-03-18.
 */

public class IntroductoryInfoActivity extends AppCompatActivity {
    public static final String INTRO_TYPE_CLASS = "INTRO_TYPE_CLASS";
    public static final String OVER_VIEW = "OVER_VIEW";
    public static final String TOURISM_TYPE_ID = "TOURISM_TYPE_ID";
    public static final String TITLE = "TITLE";

    TextView infoName1,infoName2, infoName3, infoName4;
    TextView infoName5,infoName6, infoName7, infoName8;
    TextView infoName9,infoName10, infoName11, infoName12;

    TextView infoData1,infoData2, infoData3, infoData4;
    TextView infoData5,infoData6, infoData7, infoData8;
    TextView infoData9,infoData10, infoData11, infoData12;

    public static Intent newIntent(Context context, Object tc, String overView, String title, int TypeId) {
        Intent intent = new Intent(context, IntroductoryInfoActivity.class);
        intent.putExtra(INTRO_TYPE_CLASS, (Serializable) tc);
        intent.putExtra(OVER_VIEW, overView);
        intent.putExtra(TITLE, title);
        intent.putExtra(TOURISM_TYPE_ID, TypeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductoryinfo);

        InfoTVSetting(); // Textview 셋팅
        Initialize();


    }

    private void Initialize() {

        //Toolbar 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);

        // 인텐트 데이터 받아오기
        Intent intent = getIntent();

        String overview = intent.getStringExtra(OVER_VIEW);
        String title = intent.getStringExtra(TITLE);
        int contentTypeId = intent.getIntExtra(TOURISM_TYPE_ID, 0);

        if (contentTypeId == 12) { // 관광지
            TouristItem touristItem = (TouristItem) intent.getSerializableExtra(INTRO_TYPE_CLASS);
            TouristDisplay(touristItem);

        } else if (contentTypeId == 14) { // 문화시설
            CultureItem cultureItem = (CultureItem) intent.getSerializableExtra(INTRO_TYPE_CLASS);
            CultureDisplay(cultureItem);

        } else if (contentTypeId == 15) { // 행사/공연/축제
            FestivalItem festivalItem = (FestivalItem) intent.getSerializableExtra(INTRO_TYPE_CLASS);
            FesTivalDisplay(festivalItem);

        } else if (contentTypeId == 28) { // 레포츠
            LeportsItem leportsItem = (LeportsItem) intent.getSerializableExtra(INTRO_TYPE_CLASS);
            LeportsDisplay(leportsItem);

        } else if (contentTypeId == 32) { // 숙박
            LodgingItem lodgingItem = (LodgingItem) intent.getSerializableExtra(INTRO_TYPE_CLASS);
            LodgingDisplay(lodgingItem);

        } else if (contentTypeId == 38) { // 쇼핑
            ShoppingItem shoppingItem = (ShoppingItem) intent.getSerializableExtra(INTRO_TYPE_CLASS);
            ShoppingDisplay(shoppingItem);

        } else if (contentTypeId == 39) { // 음식점
            FoodItem foodItem = (FoodItem) intent.getSerializableExtra(INTRO_TYPE_CLASS);
            FoodDisplay(foodItem);
        }

        //toolbar 제목 셋팅
        TextView titleTV = findViewById(R.id.toolbar_title);
        titleTV.setText(title);

        // 개요 셋팅
        TextView overviewTV = findViewById(R.id.introductoryinfo_overview);
        overviewTV.setText(Html.fromHtml(overview));
    }

    public void InfoTVSetting() {

        infoName1 = findViewById(R.id.infoName1);
        infoName2 = findViewById(R.id.infoName2);
        infoName3 = findViewById(R.id.infoName3);
        infoName4 = findViewById(R.id.infoName4);
        infoName5 = findViewById(R.id.infoName5);
        infoName6 = findViewById(R.id.infoName6);
        infoName7 = findViewById(R.id.infoName7);
        infoName8 = findViewById(R.id.infoName8);
        infoName9 = findViewById(R.id.infoName9);
        infoName10 = findViewById(R.id.infoName10);
        infoName11 = findViewById(R.id.infoName11);
        infoName12 = findViewById(R.id.infoName12);

        infoData1 = findViewById(R.id.infoData1);
        infoData2 = findViewById(R.id.infoData2);
        infoData3 = findViewById(R.id.infoData3);
        infoData4 = findViewById(R.id.infoData4);
        infoData5 = findViewById(R.id.infoData5);
        infoData6 = findViewById(R.id.infoData6);
        infoData7 = findViewById(R.id.infoData7);
        infoData8 = findViewById(R.id.infoData8);
        infoData9 = findViewById(R.id.infoData9);
        infoData10 = findViewById(R.id.infoData10);
        infoData11 = findViewById(R.id.infoData11);
        infoData12 = findViewById(R.id.infoData12);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    public void TouristDisplay(TouristItem Data) { // 관광 소개정보 뿌려주기

        if (Data.chkbabycarriage != null) {
            infoName1.setText("유모차");
            infoData1.setText(Data.chkbabycarriage);
        } else {
            infoName1.setVisibility(View.GONE);
            infoData1.setVisibility(View.GONE);
        }

        if (Data.chkcreditcard != null) {
            infoName2.setText("신용카드");
            infoData2.setText(Data.chkcreditcard);
        } else {
            infoName2.setVisibility(View.GONE);
            infoData2.setVisibility(View.GONE);
        }

        if (Data.chkpet != null) {
            infoName3.setText("애완동물");
            infoData3.setText(Data.chkpet);
        } else {
            infoName3.setVisibility(View.GONE);
            infoData3.setVisibility(View.GONE);
        }

        if (!Data.expguide.equals("")) {
            infoName4.setText("체험안내");
            infoData4.setText(Html.fromHtml(Data.expguide));
        } else {
            infoName4.setVisibility(View.GONE);
            infoData4.setVisibility(View.GONE);
        }

        if (Data.infocenter != null) {
            infoName5.setText("문의");
            infoData5.setText(Data.infocenter);
        } else {
            infoName5.setVisibility(View.GONE);
            infoData5.setVisibility(View.GONE);
        }

        if (Data.restdate != null) {
            infoName6.setText("쉬는날");
            infoData6.setText(Data.restdate);
        } else {
            infoName6.setVisibility(View.GONE);
            infoData6.setVisibility(View.GONE);
        }

        if (!Data.parking.equals("")) {
            infoName7.setText("주차시설");
            infoData7.setText(Data.parking);
        } else {
            infoName7.setVisibility(View.GONE);
            infoData7.setVisibility(View.GONE);
        }

        infoName8.setVisibility(View.GONE);
        infoName9.setVisibility(View.GONE);
        infoName10.setVisibility(View.GONE);
        infoName11.setVisibility(View.GONE);
        infoName12.setVisibility(View.GONE);

        infoData8.setVisibility(View.GONE);
        infoData9.setVisibility(View.GONE);
        infoData10.setVisibility(View.GONE);
        infoData11.setVisibility(View.GONE);
        infoData12.setVisibility(View.GONE);
    }

    public void CultureDisplay(CultureItem Data) { // 문화시설 소개정보 뿌려주기

        if (Data.chkbabycarriageculture != null) {
            infoName1.setText("유모차");
            infoData1.setText(Html.fromHtml(Data.chkbabycarriageculture));
        } else {
            infoName1.setVisibility(View.GONE);
            infoData1.setVisibility(View.GONE);
        }

        if (Data.chkcreditcardculture != null) {
            infoName2.setText("신용카드");
            infoData2.setText(Data.chkcreditcardculture);
        } else {
            infoName2.setVisibility(View.GONE);
            infoData2.setVisibility(View.GONE);
        }

        if (Data.chkpetculture != null) {
            infoName3.setText("애완동물");
            infoData3.setText(Data.chkpetculture);
        } else {
            infoName3.setVisibility(View.GONE);
            infoData3.setVisibility(View.GONE);
        }

        if (Data.infocenterculture != null) {
            infoName4.setText("문의");
            infoData4.setText(Data.infocenterculture);
        } else {
            infoName4.setVisibility(View.GONE);
            infoData4.setVisibility(View.GONE);
        }

        if (Data.parkingculture != null) {
            infoName5.setText("주차시설");
            infoData5.setText(Data.parkingculture);
        } else {
            infoName5.setVisibility(View.GONE);
            infoData5.setVisibility(View.GONE);
        }

        if (!Data.parkingfee.equals("")) {
            infoName6.setText("주차요금");
            infoData6.setText(Data.parkingfee);
        } else {
            infoName6.setVisibility(View.GONE);
            infoData6.setVisibility(View.GONE);
        }

        if (Data.restdateculture != null) {
            infoName7.setText("쉬는날");
            infoData7.setText(Data.restdateculture);
        } else {
            infoName7.setVisibility(View.GONE);
            infoData7.setVisibility(View.GONE);
        }

        if (Data.scale != null) {
            infoName8.setText("규모");
            infoData8.setText(Html.fromHtml(Data.scale));
        } else {
            infoName8.setVisibility(View.GONE);
            infoData8.setVisibility(View.GONE);
        }

        if (!Data.usefee.equals("")) {
            infoName9.setText("이용요금");
            infoData9.setText(Html.fromHtml(Data.usefee));
        } else {
            infoName9.setVisibility(View.GONE);
            infoData9.setVisibility(View.GONE);
        }

        if (Data.usetimeculture != null) {
            infoName10.setText("이용시간");
            infoData10.setText(Html.fromHtml(Data.usetimeculture));
        } else {
            infoName10.setVisibility(View.GONE);
            infoData10.setVisibility(View.GONE);
        }

        infoName11.setVisibility(View.GONE);
        infoName12.setVisibility(View.GONE);

        infoData11.setVisibility(View.GONE);
        infoData12.setVisibility(View.GONE);

    }

    public void FesTivalDisplay(FestivalItem Data) { // 행사/축제등 소개정보 뿌려주기
        if (Data.agelimit != null) {
            infoName1.setText("관람연령");
            infoData1.setText(Html.fromHtml(Data.agelimit));
        } else {
            infoName1.setVisibility(View.GONE);
            infoData1.setVisibility(View.GONE);
        }

        if (!Data.discountinfofestival.equals("")) {
            infoName2.setText("할인정보");
            infoData2.setText(Html.fromHtml(Data.discountinfofestival));
        } else {
            infoName2.setVisibility(View.GONE);
            infoData2.setVisibility(View.GONE);
        }

        if (Data.eventplace != null) {
            infoName3.setText("행사장소");
            infoData3.setText(Html.fromHtml(Data.eventplace));
        } else {
            infoName3.setVisibility(View.GONE);
            infoData3.setVisibility(View.GONE);
        }

        if (!Data.placeinfo.equals("")) {
            infoName4.setText("행사위치");
            infoData4.setText(Html.fromHtml(Data.placeinfo));
        } else {
            infoName4.setVisibility(View.GONE);
            infoData4.setVisibility(View.GONE);
        }

        if (Data.sponsor1 != null) {
            infoName5.setText("주최자정보");
            infoData5.setText(Html.fromHtml(Data.sponsor1));
        } else {
            infoName5.setVisibility(View.GONE);
            infoData5.setVisibility(View.GONE);
        }

        if (Data.spendtimefestival != null) {
            infoName6.setText("관람소요시간");
            infoData6.setText(Html.fromHtml(Data.spendtimefestival));
        } else {
            infoName6.setVisibility(View.GONE);
            infoData6.setVisibility(View.GONE);
        }

        if (!Data.program.equals("")) {
            infoName7.setText("행사프로그램");
            infoData7.setText(Html.fromHtml(Data.program));
        } else {
            infoName7.setVisibility(View.GONE);
            infoData7.setVisibility(View.GONE);
        }

        if (!Data.playtime.equals("")) {
            infoName8.setText("행사기간");
            infoData8.setText(Html.fromHtml(Data.playtime));
        } else {
            infoName8.setVisibility(View.GONE);
            infoData8.setVisibility(View.GONE);
        }

        if (!Data.subevent.equals("")) {
            infoName9.setText("부대행사");
            infoData9.setText(Html.fromHtml(Data.subevent));
        } else {
            infoName9.setVisibility(View.GONE);
            infoData9.setVisibility(View.GONE);
        }

        if (!Data.usetimefestival.equals("")) {
            infoName10.setText("이용요금");
            infoData10.setText(Html.fromHtml(Data.usetimefestival));
        } else {
            infoName10.setVisibility(View.GONE);
            infoData10.setVisibility(View.GONE);
        }
        infoName11.setVisibility(View.GONE);
        infoName12.setVisibility(View.GONE);

        infoData11.setVisibility(View.GONE);
        infoData12.setVisibility(View.GONE);
    }

    public void LeportsDisplay(LeportsItem Data) { // 레포츠 소개정보 뿌려주기

        if (Data.chkbabycarriageleports != null) {
            infoName1.setText("유모차");
            infoData1.setText(Data.chkbabycarriageleports);
        } else {
            infoName1.setVisibility(View.GONE);
            infoData1.setVisibility(View.GONE);
        }

        if (Data.chkcreditcardleports != null) {
            infoName2.setText("신용카드");
            infoData2.setText(Data.chkcreditcardleports);
        } else {
            infoName2.setVisibility(View.GONE);
            infoData2.setVisibility(View.GONE);
        }

        if (Data.chkpetleports != null) {
            infoName3.setText("애완동물");
            infoData3.setText(Data.chkpetleports);
        } else {
            infoName3.setVisibility(View.GONE);
            infoData3.setVisibility(View.GONE);
        }

        if (Data.infocenterleports != null) {
            infoName4.setText("문의");
            infoData4.setText(Html.fromHtml(Data.infocenterleports));
        } else {
            infoName4.setVisibility(View.GONE);
            infoData4.setVisibility(View.GONE);
        }

        if (!Data.parkingfeeleports.equals("")) {
            infoName5.setText("주차요금");
            infoData5.setText(Html.fromHtml(Data.parkingfeeleports));
        } else {
            infoName5.setVisibility(View.GONE);
            infoData5.setVisibility(View.GONE);
        }

        if (!Data.parkingleports.equals("")) {
            infoName6.setText("주차시설");
            infoData6.setText(Html.fromHtml(Data.parkingleports));
        } else {
            infoName6.setVisibility(View.GONE);
            infoData6.setVisibility(View.GONE);
        }

        if (Data.restdateleports != null) {
            infoName7.setText("쉬는날");
            infoData7.setText(Html.fromHtml(Data.restdateleports));
        } else {
            infoName7.setVisibility(View.GONE);
            infoData7.setVisibility(View.GONE);
        }

        if (!Data.usefeeleports.equals("")) {
            infoName8.setText("입장료");
            infoData8.setText(Html.fromHtml(Data.usefeeleports));
        } else {
            infoName8.setVisibility(View.GONE);
            infoData8.setVisibility(View.GONE);
        }

        if (!Data.usetimeleports.equals("")) {
            infoName9.setText("이용시간");
            infoData9.setText(Html.fromHtml(Data.usetimeleports));
        } else {
            infoName9.setVisibility(View.GONE);
            infoData9.setVisibility(View.GONE);
        }

        infoName10.setVisibility(View.GONE);
        infoName11.setVisibility(View.GONE);
        infoName12.setVisibility(View.GONE);

        infoData10.setVisibility(View.GONE);
        infoData11.setVisibility(View.GONE);
        infoData12.setVisibility(View.GONE);

    }

    public void LodgingDisplay(LodgingItem Data) { // 숙박 소개정보 뿌려주기

        if (Data.checkintime != null) {
            infoName1.setText("체크인");
            infoData1.setText(Data.checkintime);
        } else {
            infoName1.setVisibility(View.GONE);
            infoData1.setVisibility(View.GONE);
        }

        if (Data.checkouttime != null) {
            infoName2.setText("체크아웃");
            infoData2.setText(Data.checkouttime);
        } else {
            infoName2.setVisibility(View.GONE);
            infoData2.setVisibility(View.GONE);
        }

        if (Data.chkcooking != null) {
            infoName3.setText("객실취사");
            infoData3.setText(Data.chkcooking);
        } else {
            infoName3.setVisibility(View.GONE);
            infoData3.setVisibility(View.GONE);
        }

        if (Data.infocenterlodging != null) {
            infoName4.setText("예약안내");
            infoData4.setText(Html.fromHtml(Data.infocenterlodging));
        } else {
            infoName4.setVisibility(View.GONE);
            infoData4.setVisibility(View.GONE);
        }

        if (!Data.parkinglodging.equals("")) {
            infoName5.setText("주차시설");
            infoData5.setText(Data.parkinglodging);
        } else {
            infoName5.setVisibility(View.GONE);
            infoData5.setVisibility(View.GONE);
        }

        if (Data.pickup != null) {
            infoName6.setText("픽업서비스");
            infoData6.setText(Data.pickup);
        } else {
            infoName6.setVisibility(View.GONE);
            infoData6.setVisibility(View.GONE);
        }

        if (Data.roomcount != null) {
            infoName7.setText("객실 수");
            infoData7.setText(Data.roomcount);
        } else {
            infoName7.setVisibility(View.GONE);
            infoData7.setVisibility(View.GONE);
        }

        if (Data.scalelodging != null) {
            infoName8.setText("규모");
            infoData8.setText(Data.scalelodging);
        } else {
            infoName8.setVisibility(View.GONE);
            infoData8.setVisibility(View.GONE);
        }

        if (!Data.subfacility.equals("")) {
            infoName9.setText("부대시설");
            infoData9.setText(Html.fromHtml(Data.subfacility));
        } else {
            infoName9.setVisibility(View.GONE);
            infoData9.setVisibility(View.GONE);
        }

        if (Data.barbecue != 0) {
            infoName10.setText("바비큐장");
            infoData10.setText("있음");
        } else {
            infoName10.setVisibility(View.GONE);
            infoData10.setVisibility(View.GONE);
        }

        if (Data.seminar != 0) {
            infoName11.setText("세미나실");
            infoData11.setText("있음");
        } else {
            infoName11.setVisibility(View.GONE);
            infoData11.setVisibility(View.GONE);
        }

        infoName12.setVisibility(View.GONE);
        infoData12.setVisibility(View.GONE);
    }

    public void ShoppingDisplay(ShoppingItem Data) { // 숙박 소개정보 뿌려주기

        if (Data.chkbabycarriageshopping != null) {
            infoName1.setText("유모차");
            infoData1.setText(Data.chkbabycarriageshopping);
        } else {
            infoName1.setVisibility(View.GONE);
            infoData1.setVisibility(View.GONE);
        }

        if (Data.chkcreditcardshopping != null) {
            infoName2.setText("신용카드");
            infoData2.setText(Data.chkcreditcardshopping);
        } else {
            infoName2.setVisibility(View.GONE);
            infoData2.setVisibility(View.GONE);
        }

        if (Data.chkpetshopping != null) {
            infoName3.setText("애완동물");
            infoData3.setText(Data.chkpetshopping);
        } else {
            infoName3.setVisibility(View.GONE);
            infoData3.setVisibility(View.GONE);
        }

        if (Data.fairday != null) {
            infoName4.setText("장서는날");
            infoData4.setText(Data.fairday);
        } else {
            infoName4.setVisibility(View.GONE);
            infoData4.setVisibility(View.GONE);
        }

        if (Data.infocentershopping != null) {
            infoName5.setText("문의");
            infoData5.setText(Data.infocentershopping);
        } else {
            infoName5.setVisibility(View.GONE);
            infoData5.setVisibility(View.GONE);
        }

        if (Data.opendateshopping != null) {
            infoName6.setText("개장일");
            infoData6.setText(Html.fromHtml(Data.opendateshopping));
        } else {
            infoName6.setVisibility(View.GONE);
            infoData6.setVisibility(View.GONE);
        }

        if (Data.opentime != null) {
            infoName7.setText("영업시간");
            infoData7.setText(Html.fromHtml(Data.opentime));
        } else {
            infoName7.setVisibility(View.GONE);
            infoData7.setVisibility(View.GONE);
        }

        if (!Data.parkingshopping.equals("")) {
            infoName8.setText("주차시설");
            infoData8.setText(Html.fromHtml(Data.parkingshopping));
        } else {
            infoName8.setVisibility(View.GONE);
            infoData8.setVisibility(View.GONE);
        }

        if (Data.restdateshopping != null) {
            infoName9.setText("쉬는날");
            infoData9.setText(Data.restdateshopping);
        } else {
            infoName9.setVisibility(View.GONE);
            infoData9.setVisibility(View.GONE);
        }

        if (Data.restroom != null) {
            infoName10.setText("화장실");
            infoData10.setText(Html.fromHtml(Data.restroom));
        } else {
            infoName10.setVisibility(View.GONE);
            infoData10.setVisibility(View.GONE);
        }

        if (Data.saleitem != null) {
            infoName11.setText("판매품목");
            infoData11.setText(Data.saleitem);
        } else {
            infoName11.setVisibility(View.GONE);
            infoData11.setVisibility(View.GONE);
        }

        if (!Data.shopguide.equals("")) {
            infoName12.setText("매장안내");
            infoData12.setText(Html.fromHtml(Data.shopguide));
        } else {
            infoName12.setVisibility(View.GONE);
            infoData12.setVisibility(View.GONE);
        }
    }
    public void FoodDisplay(FoodItem Data) { // 숙박 소개정보 뿌려주기

        if (Data.chkcreditcardfood != null) {
            infoName1.setText("신용카드");
            infoData1.setText(Data.chkcreditcardfood);
        } else {
            infoName1.setVisibility(View.GONE);
            infoData1.setVisibility(View.GONE);
        }

        if (Data.opentimefood != null) {
            infoName2.setText("영업시간");
            infoData2.setText(Data.opentimefood);
        } else {
            infoName2.setVisibility(View.GONE);
            infoData2.setVisibility(View.GONE);
        }

        if (Data.restdatefood != null) {
            infoName3.setText("쉬는날");
            infoData3.setText(Data.restdatefood);
        } else {
            infoName3.setVisibility(View.GONE);
            infoData3.setVisibility(View.GONE);
        }

        if (!Data.parkingfood.equals("")) {
            infoName4.setText("주차시설");
            infoData4.setText(Data.parkingfood);
        } else {
            infoName4.setVisibility(View.GONE);
            infoData4.setVisibility(View.GONE);
        }

        if (Data.kidsfacility != 0) {
            infoName5.setText("놀이방");
            infoData5.setText("있음");
        } else {
            infoName5.setVisibility(View.GONE);
            infoData5.setVisibility(View.GONE);
        }

        if (Data.smoking != null) {
            infoName6.setText("금연여부");
            infoData6.setText(Data.smoking);
        } else {
            infoName6.setVisibility(View.GONE);
            infoData6.setVisibility(View.GONE);
        }

        if (Data.firstmenu != null) {
            infoName7.setText("대표메뉴");
            infoData7.setText(Html.fromHtml(Data.firstmenu));
        } else {
            infoName7.setVisibility(View.GONE);
            infoData7.setVisibility(View.GONE);
        }

        if (Data.treatmenu != null) {
            infoName8.setText("취급메뉴");
            infoData8.setText(Html.fromHtml(Data.treatmenu));
        } else {
            infoName8.setVisibility(View.GONE);
            infoData8.setVisibility(View.GONE);
        }

        if (Data.packing != null) {
            infoName9.setText("포장가능");
            infoData9.setText(Html.fromHtml(Data.packing));
        } else {
            infoName9.setVisibility(View.GONE);
            infoData9.setVisibility(View.GONE);
        }

        if (Data.infocenterfood != null) {
            infoName10.setText("예약안내");
            infoData10.setText(Html.fromHtml(Data.infocenterfood));
        } else {
            infoName10.setVisibility(View.GONE);
            infoData10.setVisibility(View.GONE);
        }

        infoName11.setVisibility(View.GONE);
        infoData11.setVisibility(View.GONE);
        infoName12.setVisibility(View.GONE);
        infoData12.setVisibility(View.GONE);
    }

}
