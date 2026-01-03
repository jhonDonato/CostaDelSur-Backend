package com.costadelsur.api.service;

import com.costadelsur.api.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface  RolService extends GenericService<Role, Integer>{
    Page<Role> listPage(Pageable pageable);
}
