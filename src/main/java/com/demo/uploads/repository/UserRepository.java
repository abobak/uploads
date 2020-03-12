package com.demo.uploads.repository;

import com.demo.uploads.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CrudRepository<User, Long> {

    User findUserByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);

    @Query("select user from User user " +
            "left join fetch user.myFiles mf " +
            "where user.id = :id")
    User findWithMyFiles(@Param("id") Long id);

    @Query("select user from User user " +
            "left join fetch user.sharedWithMe swm " +
            "where user.id = :id")
    User findWithFilesSharedWithMe(@Param("id") Long id);
}
