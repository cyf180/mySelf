/**
 * @Probject Name: 00_product_common
 * @Path: com.bailian.core.constantsComErrorCodeConstants.java
 * @Create By wangfei
 * @Create In 2014年9月29日 上午11:07:39
 * TODO
 */
package com.tdpro.common.constant;

/**
 * 通用系统异常信息
 * 
 * @Class Name ErrorCodeConstants
 * @Author lijie
 * @Create In 2014年9月29日
 */
public class ErrorCodeConstants {
	public enum ErrorCode {
		/**
		 * 错误编码规范： 长度：8位 00 0 00 000 含义： 12位：公共系统错误编码，00 3位：消息等级，默认为1 45位：模块，01
		 * 67位：具体错误 系统异常：00100001-00100099
		 */
		/* 21100** */
		SYSTEM_SUCCESS("00100000", "success"),
 		SYSTEM_ERROR("00100001", "系统异常!"),
		PARA_NORULE_ERROR("00100002", "请求参数格式不符合规则"),
		VALIDATE_ERROR("00100003", "校验有误"),
		DATA_OPER_ERROR("00100004", "数据操作异常"),
		APPLICATION_OPER_ERROR("00100006","系统业务异常"),
		ACCOUNT_EXIST_ERROR("00100007", "账号信息存在异常"),
		DATA_EMPTY_ERROR("00100005", "查询结果为空"),
		UPDATE_STATUS_ERROR("00100008","修改优惠券状态失败"),
		SIGN_VALID_ERROR("00100009", "签名验证失败"),
		SIGN_EMPTY_ERROR("00100010", "签名参数不能为空"),
		JSON_ERROR("21100021", "JSON格式不正确！"),
		MERCHANT_ERROR("00100012", "商家验证失败"),
		Params_EMPTY_ERROR("00100013","请求报文不能为空"),
		User_Code_Exist("00100014", "手机号已经注册过"),
		Id_Number_Exist("00100015", "身份证号已经注册过"),
		Waybill_Not_Exist("00100016","运单号不存在"),
		VALIDATE_USER_ERROR("00100017","账号或密码错误"),
		ENROLL_USER_SMS_STATUS("00100018","请输入手机验证码"),
		CABINET_QUANTITY_SHORTAGE("00100019","智能柜数量不足"),
		CABINET_QUANTITY_NOTUSE("00100020","智能柜箱格被占用"),
		SYSTEM_INSERT_ERROR("01000001","新增操作异常"),
		SYSTEM_UPDATE_ERROR("01000002","更新操作异常"),
		SYSTEM_DELETE_ERROR("01000003","删除操作异常"),
		SYSTEM_SELECT_ERROR("01000004","查询操作异常"),
		ORDER_NYMBER_ERROR("00100020","衣物数量不能和原始订单重复"),
		ORDER_TIME_ERROR("00100021","只有24小时之内的订单才可修改"),
		ORDER_AMOUNT_NO("00100022","尚未产生订单金额"),
		ACCOUNT_LACK_OF_BALANCE("00100023","用户余额不足"),
		VALIDATE_TOKEN_ERROR("401","token验证失败"),
		VALIDATE_ACCOUNT_ERROR("10000088","用户未登录"),
		VALIDATE_ORDER_ERROR("10000089","已检单"),
		CHECK_ORDER_ERROR("10000090","信息与用户下单时填写不一致"),
		CHECK_ORDERBOX_ERROR("10000091","柜子箱格状态与订单状态不符"),
		CHECK_ORDERNUMBER_ERROR("10000092","您提交的信息与物流信息不一致，且异常订单次数已超过3次，订单被取消，请尽快到智能柜前取衣"),
		CHECK_BOX_ERROR("10000093", "未选择箱格"),
		CHECK_BAG_ERROR("10000094", "未绑定洗护单号"),
		VALIDATE_URL_EXIST("10000095","无权限访问"),
		USER_NO_PERMITION("90000002", "用户授权失败"),
		PARAM_ERROR("90000003", "参数错误"),
		MSG_ERROR("90000004", "信息重复"),
		BAG_NOT_EXIST("90000008", "装衣袋不存在"),
		BUSINESS_ERROR("90000010","业务错误"),
		COUPON_ERROR("90000011","优惠券已失效或已使用"),
		WITHDRAW_TIME_IS_NOT("90000012","距离上次提现时间太短"),
		JUMP_LOGO("10000098","工厂检单跳转标识");

		private String errorCode;
		private String memo;

		private ErrorCode() {
		};

		private ErrorCode(String errorCode, String memo) {
			this.errorCode = errorCode;
			this.memo = memo;
		}

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}
	}
}
