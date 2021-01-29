package com.example.module_user.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.module_user.R;
import com.example.module_user.utils.EditTextUtil;


public class LoginView extends RelativeLayout implements TextWatcher, View.OnClickListener, View.OnFocusChangeListener {
    private EditText mPhoneNumber_edit;
    private EditText mVerification_edit;
    private EditText mPassWord_edit;

    private ImageView mDeletePhoneNumber_image;
    private ImageView mDeletePassWord_image;
    private ImageView mShowPassWord_image;
    private String TAG = "LoginView";
    private TextView mGetVerification_tx;
    private Button mLogin_bt;
    private int flag;
    private boolean mGetVerificationState = false;
    private boolean mVerificationState = false;
    private boolean mPassWordState = false;
    private boolean isCurrentDown = false;
    private onStateClickListener monStateClickListener=null;
    private CountDownTimer mCountDownTimer;

    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
    private static final int VERIFICATION_COLOR= R.color.login_getverification_selector;
    private int mVerificationColor=VERIFICATION_COLOR;
    private int mBeginTime=60000;
    private int mPasswordLength=32;
    private boolean mIsShow=true;


    public LoginView(Context context) {
        this(context, null);
    }

    public LoginView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intAttrs(context,attrs);
        intView(context);
        intListener();
        intEvent();

    }

    private void intAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoginView);
        mVerificationColor = typedArray.getResourceId(R.styleable.LoginView_verificationTextColor, R.color.login_getverification_selector);
        mBeginTime = typedArray.getInt(R.styleable.LoginView_countDownTime, mBeginTime);
        mPasswordLength = typedArray.getInt(R.styleable.LoginView_passwordLength, mPasswordLength);

    }

    private void intListener() {
        mPhoneNumber_edit.addTextChangedListener(this);
        mVerification_edit.addTextChangedListener(this);
        mPassWord_edit.addTextChangedListener(this);
        mDeletePhoneNumber_image.setOnClickListener(this);
        mDeletePassWord_image.setOnClickListener(this);
        mShowPassWord_image.setOnClickListener(this);
        mGetVerification_tx.setOnClickListener(this);
        mPhoneNumber_edit.setOnFocusChangeListener(this);
        mPassWord_edit.setOnFocusChangeListener(this);
        mVerification_edit.setOnFocusChangeListener(this);
        mLogin_bt.setOnClickListener(this);

    }

    private void intEvent() {
    }

    private void intView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.diy_login_view, this, true);
        mPhoneNumber_edit = findViewById(R.id.login_phone_number);
        mVerification_edit = findViewById(R.id.login_verification_code);
        mPassWord_edit = findViewById(R.id.login_password);
        mGetVerification_tx = findViewById(R.id.login_get_verification_code);
        mDeletePhoneNumber_image = findViewById(R.id.login_delete_number);
        mDeletePassWord_image = findViewById(R.id.login_delete_pwd);
        mShowPassWord_image = findViewById(R.id.login_show_pwd);
        mLogin_bt = findViewById(R.id.login_bt);
        mShowPassWord_image.setImageResource(R.mipmap.pwd_show_normal);
       EditTextUtil.setEditTextInputSpace(mPassWord_edit,mPasswordLength);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        boolean empty = TextUtils.isEmpty(s);
        EditText focus = getEditFocus();
        if (focus == mPhoneNumber_edit) {
            String phone = mPhoneNumber_edit.getText().toString().trim();
            mDeletePhoneNumber_image.setVisibility(empty ? GONE : VISIBLE);
            mGetVerificationState = phone.length() == 11;
        } else if (focus == mPassWord_edit) {
            mDeletePassWord_image.setVisibility(empty ? GONE : VISIBLE);
            mPassWordState = mPassWord_edit.getText().length() > 5;
        } else if (focus == mVerification_edit) {
            mVerificationState = mVerification_edit.getText().length() > 4;
        }
        updateState();
    }

    @Override
    public void afterTextChanged(Editable s) {
            EditTextUtil.isChinese(s);
    }

    private EditText getEditFocus() {
        View view = findFocus();
        if (view instanceof EditText) {
            return (EditText) view;
        }
        return null;
    }

    private void updateState() {
        if (!isCurrentDown) {
            mGetVerification_tx.setEnabled(mGetVerificationState);
        }


        mLogin_bt.setEnabled(mGetVerificationState & mPassWordState  & mVerificationState);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.login_delete_number) {
            mPhoneNumber_edit.setSelection(0);
            mPhoneNumber_edit.setText("");
        } else if (id == R.id.login_delete_pwd) {
            mPassWord_edit.setSelection(0);
            mPassWord_edit.setText("");
        } else if (id == R.id.login_show_pwd) {
            EditTextUtil.changePwdShow(mPassWord_edit,mShowPassWord_image,mIsShow);
            mIsShow=!mIsShow;
            mPassWord_edit.setSelection(mPassWord_edit.getText().length());
        } else if (id == R.id.login_get_verification_code) {
            String phone = mPhoneNumber_edit.getText().toString().trim();
            mVerification_edit.requestFocus();
            startCurrentDownTimer();
            if (monStateClickListener != null) {
                monStateClickListener.getVerificationCodeClick(phone);
            }
        }  else if (id == R.id.login_bt) {
            String code1 = mVerification_edit.getText().toString().trim();
            String phone = mPhoneNumber_edit.getText().toString().trim();
            String password = mPassWord_edit.getText().toString().trim();
            if (monStateClickListener != null) {
                monStateClickListener.onLoginClick(phone, code1, password);
            }
        }
    }

    private void startCurrentDownTimer() {
        mGetVerification_tx.setEnabled(false);
        isCurrentDown = true;

        mCountDownTimer = new CountDownTimer(mBeginTime, 1000) {

            public void onTick(long millisUntilFinished) {
                mGetVerification_tx.setText( millisUntilFinished / 1000+"s后重发");

            }

            public void onFinish() {
                mGetVerification_tx.setText("重新发送");
                mGetVerification_tx.setEnabled(true);
                isCurrentDown = false;
            }
        }.start();


    }

    public void setGetVerificationError() {
        mCountDownTimer.onFinish();
        mCountDownTimer.cancel();
        mVerification_edit.setText("");

    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v.getId() == R.id.login_verification_code) {
                mDeletePhoneNumber_image.setVisibility(GONE);
                mDeletePassWord_image.setVisibility(GONE);
            } else{
                mDeletePhoneNumber_image.setVisibility(v.getId() == R.id.login_phone_number &&
                        !TextUtils.isEmpty(mPhoneNumber_edit.getText().toString().trim()) ? VISIBLE : GONE);

                mDeletePassWord_image.setVisibility(v.getId() == R.id.login_password&&
                        !TextUtils.isEmpty(mPassWord_edit.getText().toString().trim())? VISIBLE : GONE);
            }
        }

    }

    public void setLoginBtText(String msg) {
        mLogin_bt.setText(msg);
    }


    public void setonStateClickListener(onStateClickListener listener) {
        this.monStateClickListener=listener;
    }

    public interface onStateClickListener {
        void getVerificationCodeClick(String number);

        void onLoginClick(String phone, String code, String password);

    }
}

