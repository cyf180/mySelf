package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.PUserAsk;
import com.tdpro.entity.extend.UserAskPageETD;

public interface UserAskService {
    Response addUserAsk(PUserAsk userAsk);

    Response adminPageList(UserAskPageETD userAskPageETD);

    Response info(Long uid);
}
