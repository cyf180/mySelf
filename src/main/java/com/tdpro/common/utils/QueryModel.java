package com.tdpro.common.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * 页面查询模型
 * @author lijie
 * @email  lijie@6mi.com
 * @time   2016年11月14日 下午2:35:16
 */
@Data
public class QueryModel implements ClearBean{

	private Integer pageNo; //当前页面
	private Integer pageSize;   //每页几条
	private Date startTime;
	private Date endTime;
    private Date createTime;
    private String createName;
   /* private String isDelete;*/
	private Integer rows;	//总条数
	@JsonIgnore
	/*private Integer aid;*/
	private String ip;
	private Long uId;

	@Override
	public void clear() {
		pageNo=null;
		pageSize=null;
		startTime=null;
		endTime=null;
		rows=null;
	}
	

}
