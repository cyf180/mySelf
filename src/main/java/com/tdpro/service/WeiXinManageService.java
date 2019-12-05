package com.tdpro.service;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;

public interface WeiXinManageService {

    WxMaJscode2SessionResult getSessionInfo(String code);
}
