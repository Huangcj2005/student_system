package com.example.student_system.common;

import lombok.Getter;

@Getter
public class CommonResponse<T>{
    private final int status;
    private final String message;
    private T data;

    private CommonResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private CommonResponse(int status, String message){
        this.status = status;
        this.message = message;
    }

    // 手动添加getter方法，确保编译通过
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    //常用成功
    public static <T>CommonResponse<T> createForSuccess(int code,String message,T data){
        return new CommonResponse<T>(code,message,data);
    }
    //返回数据为空
    public static <T>CommonResponse<T> createForSuccess(int code,String message){
        return new CommonResponse<T>(code,message);
    }


    //    public static <T>CommonResponse<T> createForError(){
//        return new CommonResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDescription());
//    }
    public static <T>CommonResponse<T> createForError(int code,String message){
        return new CommonResponse<T>(code, message);
    }

    public static <T>CommonResponse<T> createForSuccessMessage(String msg) {
        return new CommonResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
}

