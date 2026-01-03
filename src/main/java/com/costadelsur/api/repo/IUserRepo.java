package com.costadelsur.api.repo;

import com.costadelsur.api.model.User;

public interface IUserRepo extends  IGenericRepo<User, Integer>{
    //SELECT * FROM User u WHERE u.username = ?
    // @Query("FROM User u WHERE u.username = :username")
    // DerivedQueries
    User findOneByUsername(String username);
}