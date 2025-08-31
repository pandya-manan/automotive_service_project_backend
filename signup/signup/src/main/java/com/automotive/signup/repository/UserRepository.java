package com.automotive.signup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.automotive.signup.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUserEmail(String userEmail);
    boolean existsByUserPhoneNumber(String phoneNumber);

}
