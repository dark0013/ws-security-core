package com.darkross.wssecuritycore.repository;

import com.darkross.wssecuritycore.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByRol(String rol);

    boolean existsByRol(String rol);
}
