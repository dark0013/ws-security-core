package com.darkross.wssecuritycore.repository;

import com.darkross.wssecuritycore.entity.Rol;
import com.darkross.wssecuritycore.entity.User;
import com.darkross.wssecuritycore.entity.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, Long> {

    Optional<UsuarioRol> findByUsuarioAndRol(User usuario, Rol rol);

    List<UsuarioRol> findByUsuario(User usuario);

    List<UsuarioRol> findByRol(Rol rol);

    boolean existsByUsuarioAndRol(User usuario, Rol rol);
}

