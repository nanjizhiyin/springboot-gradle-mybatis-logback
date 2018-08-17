package cn.testin.entry.result;

/**
 * Copyright (C) 北京学信科技有限公司
 *
 * @Des:
 * @Author: Gaojindan
 * @Create: 2018/2/28 下午2:54
 **/
public class ResultError extends ResultEntry {
    public ResultError(Integer code, String msg){
        this.setCode(code);
        this.setMsg(msg);
    }
}
