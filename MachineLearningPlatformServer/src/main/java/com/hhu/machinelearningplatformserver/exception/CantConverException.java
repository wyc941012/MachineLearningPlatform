package com.hhu.machinelearningplatformserver.exception;

/**
 * 无法转换错误
 *
 * @author hayes, @create 2017-12-11 21:57
 **/
public class CantConverException extends Exception {

    public CantConverException(String msg) {
        super(msg);
    }

    public CantConverException() {
    }
}
