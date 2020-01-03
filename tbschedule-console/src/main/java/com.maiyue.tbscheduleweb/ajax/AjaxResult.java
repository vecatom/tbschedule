package com.maiyue.tbscheduleweb.ajax;

/**
 *
 */
public class AjaxResult {
    private Integer code;
    private String msg;
    private Object data;

    public static AjaxResult sucess(){
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(200);
        ajaxResult.setMsg("操作成功");
        return ajaxResult;
    }

    public static AjaxResult filed(Integer code){
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(code==null?500:code);
        ajaxResult.setMsg("处理失败！");
        return ajaxResult;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
