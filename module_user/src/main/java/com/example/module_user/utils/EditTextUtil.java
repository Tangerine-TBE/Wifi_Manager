package com.example.module_user.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.module_user.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Administrator
 * @date: 2020/7/11 0011
 */
public class EditTextUtil {

    //是否是汉字
    public static void isChinese(Editable s) {
        if (s.length() > 0) {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c >= 0x4e00 && c <= 0X9fff) { // 根据字节码判断
                    // 如果是中文，则清除输入的字符，否则保留
                    s.delete(i,i+1);
                }
            }
        }
    }

    public static void setEditTextInputSpace(EditText editText, int maxLength) {

        final Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher emojiMatcher = emoji.matcher(source);
                //禁止特殊符号
               String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？_-]";
               Pattern pattern = Pattern.compile(speChat);
               Matcher matcher = pattern.matcher(source.toString());

                if (matcher.find()) {
                    return "";
                }
                //禁止输入空格
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                    //禁止输入表情
                } else if (emojiMatcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(maxLength)});
    }


    public static void changePwdShow(EditText mPassWord_edit, ImageView mShowPassWord_image, boolean isShow) {
        if (isShow) {
            mShowPassWord_image.setImageResource(R.mipmap.pwd_show_select);
            mPassWord_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            mShowPassWord_image.setImageResource(R.mipmap.pwd_show_normal);
            mPassWord_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

}
