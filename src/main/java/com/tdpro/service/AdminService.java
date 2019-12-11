package com.tdpro.service;

import com.tdpro.entity.PAdmin;

public interface AdminService {
    /**
     *
     * @param admin
     * @return
     */
    PAdmin findByPhone(PAdmin admin);
}
