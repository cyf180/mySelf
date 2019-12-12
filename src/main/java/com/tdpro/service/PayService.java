package com.tdpro.service;

import com.tdpro.common.constant.PayType;
import com.tdpro.common.utils.PayReturn;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.weixin.entity.WxPayET;
import com.tdpro.entity.PUser;
import com.tdpro.entity.extend.OrderPayETD;

import java.util.Map;

public interface PayService {
    /**
     *
     * @param payETD
     * @return
     */
    PayReturn unifyPay(OrderPayETD payETD);

    /**
     * 会员购买
     * @param user
     * @return
     */
    Response userPay(PUser user);
}
