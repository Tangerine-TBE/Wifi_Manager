package com.example.module_tool.utils;

import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

public class KeyBoardUtlis {

    /**
     *自动弹出软键盘
     * */
    public static void autoShowKeyBoard(final EditText edit){
        Log.e("自动弹出软键盘","开始");
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run()
            {
                InputMethodManager inputManager = (InputMethodManager)edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(edit, 0);
                Log.e("自动弹出软键盘","完毕");
            }
        }, 500);
    }

    /**
     *取消软键盘
     * */
    public static void autoDismissKeyBoard(final EditText... edits){
        for (EditText edit:edits) {
            final EditText edit1 = edit;
            edit.clearFocus();
            edit.setFocusable(true);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run()
                {
                    InputMethodManager inputManager = (InputMethodManager)edit1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(edit1.getWindowToken(),0);
                }
            }, 100);
        }
    }
}
