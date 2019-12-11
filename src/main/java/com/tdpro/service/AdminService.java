package com.tdpro.service;

import com.tdpro.entity.PAdmin;

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
}
