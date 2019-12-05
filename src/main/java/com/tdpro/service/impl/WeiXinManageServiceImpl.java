package com.tdpro.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.tdpro.config.weixin.WxMaProperties;
import com.tdpro.service.WeiXinManageService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class WeiXinManageServiceImpl implements WeiXinManageService {

    @Autowired
    private WxMaProperties wxMaProperties;

    @Autowired
    private WxMaService wxMaService;

    @Override
    public WxMaJscode2SessionResult getSessionInfo(String code) {
        wxMaService.setWxMaConfig(wxMaProperties);
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            return session;
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }
}
