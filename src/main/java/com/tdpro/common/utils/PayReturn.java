package com.tdpro.common.utils;

import com.tdpro.entity.POrder;
import com.tdpro.entity.PUser;
import lombok.Data;

@Data
public class PayReturn {
    private Boolean state = new Boolean(false);
    private String msg;
    private int type = 0;
    private Long orderId;
    private Object successRes;
}
