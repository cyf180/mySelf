package com.tdpro.common.utils;

import com.tdpro.entity.POrder;
import com.tdpro.entity.PUser;
import lombok.Data;

@Data
public class PayReturn {
    private Boolean state;
    private String msg;
    private Integer type = new Integer(0);
    private Long orderId;
    private Object successRes;
}
