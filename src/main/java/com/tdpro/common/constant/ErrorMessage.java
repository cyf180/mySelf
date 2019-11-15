package com.tdpro.common.constant;

/**
 * 操作错误提示
 */
public enum ErrorMessage {
    ORDER_STATUE_ERROR("当前订单状态不符合！"),
    BOX_STATUS_UPDATE_ERROR("箱格状态更新失败！"),
    BOX_INSUFFICIENT("箱格数量不足！"),
    BIG_BOX_INSUFFICIENT("大箱格数量不足！"),
    SNALL_BOX_INSUFFICIENT("小箱格数量不足！"),
    KEY_VALUE_ERROR("关键值错误！"),
    UNDISTRIBUTED_LOGISTICS("尚未分配物流人员！"),
    UNBIND_CABINET("该物流没有绑定该智能柜！"),
    CABINET_NOT_FIND("柜子不存在！"),
    CABINET_NUMBER_NOT_NULL("柜子编码不能为空！"),
    CABINET_INADEQUATE("当前无空闲柜子！"),
    CABINET_NOT_FIND_OR_NOR_ACTIVATE("智能柜不存在或未激活！"),
    GET_QR_CODE_ERROR("获取二维码失败！"),
    OPERATION_TYPE_ERROR("操作类型错误！"),
    OPERATION_FAILURE("101"),
    OPERATION_SUCCESS("100"),
    OPERATION_OPEN_SUCCESS("102");
    private String message;
    private ErrorMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
}
