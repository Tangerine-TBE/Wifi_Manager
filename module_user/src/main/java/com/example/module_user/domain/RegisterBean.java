package com.example.module_user.domain;

public class RegisterBean {


    /**
     * ret : 700
     * msg : 该手机号码已经注册
     * data :
     */

    private int ret;
    private String msg;
    private String data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RegisterBean{" +
                "ret=" + ret +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
