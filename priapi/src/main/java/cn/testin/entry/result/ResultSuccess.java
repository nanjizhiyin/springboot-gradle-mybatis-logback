package cn.testin.entry.result;

/**
 * Copyright (C) 北京学信科技有限公司
 *
 * @Des:
 * @Author: Gaojindan
 * @Create: 2018/2/28 下午2:51
 **/
public class ResultSuccess extends ResultEntry {
    public ResultSuccess(Object obj){
        this.setCode(200);
        this.setData(obj);
    }
    public ResultSuccess(String msg,Object obj){
        this.setCode(200);
        this.setMsg(msg);
        this.setData(obj);
    }
}
