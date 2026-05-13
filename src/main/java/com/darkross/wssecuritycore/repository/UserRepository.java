package com.darkross.wssecuritycore.repository;

import com.darkross.wssecuritycore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCedula(String cedula);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByCedula(String cedula);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
