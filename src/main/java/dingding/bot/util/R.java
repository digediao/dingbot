package dingding.bot.util;

import lombok.Data;

import java.io.Serializable;

import static dingding.bot.util.constant.RConstant.*;

@Data
public class R<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    public R(int code){
        this.code = code;
    }
    public R(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
    public R(int code,String msg,T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    //success
    public R success(){
        return new R(SUCCESS_CODE);
    }
    public R<T> success(String msg){
        return new R<>(SUCCESS_CODE,msg);
    }
    public R<T> success(T data) {
        return new R<>(SUCCESS_CODE, SUCCESS_MSG, data);
    }
    public R<T> success(String msg,T data) {
        return new R<>(SUCCESS_CODE,msg, data);
    }

    //error
    public R error(){
        return new R(ERROR_CODE);
    }
    public R<T> error(String msg){
        return new R<>(ERROR_CODE,msg);
    }
    public R<T> error(T data) {
        return new R<>(ERROR_CODE,ERROR_MSG, data);
    }
    public R<T> error(String msg,T data) {
        return new R<>(ERROR_CODE,msg, data);
    }
}
