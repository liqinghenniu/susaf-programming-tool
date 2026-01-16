package com.yyq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonResult {
    private int code;
    private String message;
    private Object data;
    public static JsonResult success(){
        return new JsonResult(200,"操作成功",null);
    }
    public static JsonResult success(Object data){
        return new JsonResult(200,"操作成功",data);
    }
    public static JsonResult error(){
        return new JsonResult(500,"操作失败",null);
    }
    public static JsonResult error(String message,Object data){
        return new JsonResult(500,message,data);
    }
}
