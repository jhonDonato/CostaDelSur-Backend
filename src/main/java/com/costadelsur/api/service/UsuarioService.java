package com.costadelsur.api.service;

import  com.costadelsur.api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface  UsuarioService extends GenericService<User, Integer> {
    Page<User> listPage(Pageable pageable);
}
