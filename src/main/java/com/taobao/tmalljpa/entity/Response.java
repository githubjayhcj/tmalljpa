package com.taobao.tmalljpa.entity;

public class Response<T> {
    public final static int SUCCESS = 1;
    public final static int FAIL = 0;

    private int code = 1;
    private String message;
    private T result;

    public Response() {
    }

    public Response(int code) {
        this.code = code;
    }

    public Response(String message) {
        this.message = message;
    }

    public Response(T result) {
        this.result = result;
    }

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(int code, T result) {
        this.code = code;
        this.result = result;
    }

    public Response(String message,T result) {
        this.message = message;
        this.result = result;
    }

    public Response(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static int getSUCCESS() {
        return SUCCESS;
    }

    public static int getFAIL() {
        return FAIL;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
