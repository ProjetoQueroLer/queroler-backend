package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserIgnoreCase(String user);
}