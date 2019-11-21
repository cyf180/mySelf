package com.tdpro.service;

public interface LogService {
    /**
     * 日志添加
     * @param uid
     * @param operation
     * @param note
     * @return
     */
    Boolean insertLog(Long uid,String operation,String note);
}
