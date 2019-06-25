package com.korlab.foodex.delivery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.korlab.foodex.delivery.Data.Courier;
import com.korlab.foodex.delivery.FireServer.Auth;
import com.korlab.foodex.delivery.FireServer.FireRequest;
import com.korlab.foodex.delivery.Technical.Helper;
import com.korlab.foodex.delivery.UI.InputCodeLayout;
import com.korlab.foodex.delivery.UI.MaterialButton;

import java.util.HashMap;

import kotlin.Unit;
import spencerstudios.com.bungeelib.Bungee;

public class AuthorizeVerification extends AppCompatActivity {
    private static int TIME_OUT = 60;

    private TextView timer, textPhone, buttonWrong, buttonResetCode;
    private ProgressBar progress;
    private MaterialButton buttonResend;
    private InputCodeLayout inputCodeLayout;
    private Courier courier;

    private boolean isButtonResend;

    private static AuthorizeVerification instance;

    public static AuthorizeVerification getInstance() {
        return instance;
    }

    @Override
    public void onBackPressed() {
        Helper.showDialog(getInstance(), LayoutInflater.from(getInstance().getBaseContext()).inflate(R.layout.dialog_exit, null), (v) -> this.finishAffinity(), null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize_verification);
        instance = this;
        Helper.setStatusBarColor(getWindow(), ContextCompat.getColor(getBaseContext(), R.color.white));
        Helper.setStatusBarIconWhite(getWindow());

        findView();
        courier = Helper.getCourierData();
//        if (courier.getPhoneNumber().equals("")) {
//            Helper.setCourierData(courier);
//            startActivity(new Intent(getInstance(), InfoFullName.class));
//            Bungee.slideLeft(getInstance());
//            finish();
//        }
        textPhone.setText(courier.getPhoneNumber());
        countDownTimer(TIME_OUT);
        inputCodeLayout.setOnInputCompleteListener(code -> {
            Helper.hideKeyboard(getInstance(), inputCodeLayout);
//            Helper.setCourierData(courier);
            Auth.INSTANCE.checkEnteredCodeVerification(
                    code,
                    AuthorizeVerification::onRightSms,
                    AuthorizeVerification::onWrongSms);
        });
        buttonWrong.setOnClickListener(v -> {
            instance = null;
            super.finish();
        });
        buttonResetCode.setOnClickListener(v -> inputCodeLayout.clear());
        buttonResend.setOnClickListener(v -> resendSms());
    }

    private void resendSms() {
        if (isButtonResend) {
            isButtonResend = false;
            buttonResend.setTextColor(getResources().getColor(R.color.dark_text));
            countDownTimer(TIME_OUT);
            // TODO: 5/24/2019 resend sms from firebase
        }
    }

    private void findView() {
        timer = findViewById(R.id.timer);
        buttonWrong = findViewById(R.id.button_wrong);
        buttonResetCode = findViewById(R.id.button_reset_code);
        buttonResend = findViewById(R.id.button_resend);
        textPhone = findViewById(R.id.text_phone);
        progress = findViewById(R.id.progress);
        inputCodeLayout = findViewById(R.id.input_code);
    }

    private void countDownTimer(int s) {
        new Thread(() -> {
            for (int i = s; i >= 0; i--) {
                int minutes = i / 60;
                int seconds = i % 60;
                int pr = 100 - (i * 100 / s);
                runOnUiThread(() -> {
                    progress.setProgress(pr);
                    timer.setText(String.format("%02d:%02d", minutes, seconds));
                });
                if (seconds == 0 && minutes == 0) {
                    isButtonResend = true;
                    if(getInstance() != null)
                        buttonResend.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }

    public static Unit onRightSms() {
        Helper.log("Right sms code");
        FireRequest.Companion.callFunction("isMyCustomerAccountExists", new HashMap<String, Object>(), AuthorizeVerification::onCourierExist, AuthorizeVerification::onCourierNotExist);
//        FireRequest.Companion.getData("customers",Auth.INSTANCE.getRealCourierId(), AuthorizeVerification::onSuccessGotCourier, AuthorizeVerification::onFailGotCourier);
        return Unit.INSTANCE;
    }

    // Check registered courier
    private static Unit onCourierExist(HashMap<String, Object> responseHashMap) {
        if(Boolean.parseBoolean(responseHashMap.get("exists").toString())) {
            Helper.log("Courier exist");
            launchActivity(MainActivity.class);
        }
        return Unit.INSTANCE;
    }

    private static Unit onCourierNotExist() {
        Helper.log("isMyCustomerAccountExists error");
        return Unit.INSTANCE;
    }

    public static Unit onWrongSms() {
        Helper.log("Wrong sms code");
        return Unit.INSTANCE;
    }

    private static void launchActivity(Class<?> activityClass) {
        if(getInstance() == null) {
            startActivity(Authorize.getInstance(), activityClass);
        } else {
            startActivity(getInstance(), activityClass);
        }
    }

    private static void startActivity(Activity activity, Class<?> activityClass) {
        activity.startActivity(new Intent(activity, activityClass));
        Bungee.slideLeft(activity);
        instance = null;
        activity.finish();
    }

}
