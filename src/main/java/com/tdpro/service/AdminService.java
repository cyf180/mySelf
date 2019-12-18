package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.PAdmin;
import com.tdpro.entity.extend.AdminPageETD;

public interface AdminService {
    /**
     *
     * @param admin
     * @return
     */
    PAdmin findByPhone(PAdmin admin);

    /**
     * Id查询
     * @param id
     * @return
     */
    PAdmin findById(Long id);

    Response pageList(AdminPageETD adminPageETD);

    Response addAdmin(PAdmin admin);

    Response updateState(Long id);

    Response adminInfo(Long id);

    Response passwordUpdate(PAdmin admin);

    Response adminHome();
}
