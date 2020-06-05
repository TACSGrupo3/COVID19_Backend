package com.tacs.rest.dao;

import org.springframework.stereotype.Repository;

import com.tacs.rest.entity.User;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserDAO extends CrudRepository<User, String>  {

	

}