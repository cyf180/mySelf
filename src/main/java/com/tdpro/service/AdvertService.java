package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.PAdvert;
import com.tdpro.entity.extend.AdvertETD;

import java.util.List;

public interface AdvertService {
    /**
     * 轮播列表
     * @return
     */
    List<PAdvert> advertList();

    /**
     * 后台列表
     * @param advertETD
     * @return
     */
    Response adminPageList(AdvertETD advertETD);

    Response adminAdvertInfo(Long id);

    /**
     * 修改轮播
     * @param advert
     * @return
     */
    Response updateAdvert(PAdvert advert);

    Response deleteById(Long id);
}
