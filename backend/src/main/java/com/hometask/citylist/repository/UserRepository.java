package com.hometask.citylist.repository;

import com.hometask.citylist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Elnur Mammadov
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);
}
