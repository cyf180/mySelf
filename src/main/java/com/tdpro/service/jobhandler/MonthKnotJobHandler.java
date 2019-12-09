package com.tdpro.service.jobhandler;

import com.tdpro.service.KnotService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@JobHandler(value="monthKnotJobHandler")
@Component
public class MonthKnotJobHandler extends IJobHandler {
    @Autowired
    private KnotService knotService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("月计算开始.");
        Future<Boolean> knot =  knotService.monthKnot();
        return SUCCESS;
    }
}
