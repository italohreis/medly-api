package com.italohreis.medly.repositories;

import com.italohreis.medly.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndActiveTrue(String email);

    @Modifying
    @Query("UPDATE User u SET u.active = false WHERE u.id = :id")
    void softDeleteById(@Param("id") UUID id);
}
