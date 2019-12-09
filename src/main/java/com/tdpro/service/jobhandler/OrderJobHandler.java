package com.tdpro.service.jobhandler;

import com.tdpro.service.OrderService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@JobHandler(value="monthKnotJobHandler")
@Component
public class OrderJobHandler extends IJobHandler {
    @Autowired
    private OrderService orderService;
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("月计算开始.");
        Future<Boolean> knot =  orderService.overdueOrder();
        return SUCCESS;
    }
}
