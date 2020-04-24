package com.example.asynch;

import java.io.Serializable;

public class BaseResponseBean<T> implements Serializable {

    /**
     * 0：成功，0001：系统异常，0002：请求参数不合法
     */
    public String retCode;

    public String retMsg;

    public T data;

    public boolean success() {
        return "0".equals(retCode);
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "retCode='" + retCode + '\'' +
                ", retMsg='" + retMsg + '\'' +
                ", data=" + data +
                '}';
    }


}
