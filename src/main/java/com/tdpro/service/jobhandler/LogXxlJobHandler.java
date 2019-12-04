package com.tdpro.service.jobhandler;

import com.tdpro.service.LogService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@JobHandler(value="logXxlJobHandler")
@Component
public class LogXxlJobHandler extends IJobHandler {
    @Autowired
    private LogService logService;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("消息推送定时任务处理开始");
        boolean result =logService.insertLog(1L,"订单任务测试","定时任务测试");
        if (result){
            XxlJobLogger.log("添加日志失败");
        }
        XxlJobLogger.log("成功");
        return SUCCESS;
    }
}
