package com.automotive.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automotive.login.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

	User findByUserEmail(String userEmail);

}
