package com.myLogin.login.repositories;

import com.myLogin.login.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    public AppUser findByUsername(String username);
    public AppUser findByEmail(String email);
}