package com.tdpro.service;

import com.tdpro.common.constant.KnotType;
import com.tdpro.common.utils.Response;
import com.tdpro.entity.PUserKnot;
import com.tdpro.entity.extend.UserKnotETD;
import com.tdpro.entity.extend.UserKnotPageETD;

import java.math.BigDecimal;
import java.util.List;

public interface UserKnotService {
    /**
     * 添加结算记录
     * @param uid
     * @param payUid
     * @param orderId
     * @param monthId
     * @param knotPrice
     * @param payPrice
     * @param knotType
     * @return
     */
    Boolean insertUserKnot(Long uid, Long payUid, Long orderId, Long monthId, BigDecimal knotPrice, BigDecimal payPrice, KnotType knotType);

    PUserKnot insertKnot(Long uid, Long payUid, Long orderId, Long monthId, BigDecimal knotPrice, BigDecimal payPrice, KnotType knotType);

    /**
     * 修改月结算日志monthId
     * @return
     */
    int updateMonthKnotByIdList(List<Long> idList,Long monthId);

    Response userKnotList(UserKnotETD userKnotETD);

    Response getAdminList(UserKnotPageETD userKnotPageETD);


}
