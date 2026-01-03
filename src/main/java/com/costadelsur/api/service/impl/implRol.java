package com.costadelsur.api.service.impl;

import org.springframework.stereotype.Service;

import com.costadelsur.api.model.Role;
import com.costadelsur.api.repo.IGenericRepo;
import com.costadelsur.api.repo.RolRepository;
import com.costadelsur.api.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class implRol extends implGenericService<Role, Integer> implements RolService {
    private final RolRepository repo;

    @Override
    protected IGenericRepo<Role, Integer> getRepo(){
        return repo;
    }
    @Override
    public Page<Role> listPage(Pageable pageable) {
        return repo.findAll(pageable);
    }

}
