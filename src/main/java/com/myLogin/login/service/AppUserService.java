package com.myLogin.login.service;

import com.myLogin.login.model.AppUser;
import com.myLogin.login.model.LoginDto;
import com.myLogin.login.model.RegisterDto;
import com.myLogin.login.model.UserDetailModel;
import com.myLogin.login.repositories.AppUserRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("appUserService")
public class AppUserService implements UserDetailsService {

    private AppUserRepository repo;
    private PasswordEncoder encoder;

    public AppUserService(AppUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repo.findByUsername(username);
        System.out.println("appUser = "+appUser);
        return Optional.of(appUser).map(UserDetailModel::new).orElseThrow(()->new UsernameNotFoundException("Invalid Username"));
    }
    public Optional<UserDetails> saveUserToDB(RegisterDto dto) {
        AppUser appUser = repo.findByEmail(dto.getEmail());
        if(appUser != null)         // user with this email already exist in DB
            return Optional.empty();
        return Optional.of(dto).map(mapToAppUser).map(repo::save).map(UserDetailModel::new);
    }
    public Optional<UserDetails> loginUserFromDB(LoginDto login) {
        return Optional.ofNullable(loadUserByUsername(login.getUsername()))
                .filter(u -> encoder.matches(login.getPass(), u.getPassword()));
    }

    public Optional<AppUser> getAppUserByName(String name) {
        return Optional.of(name).map(repo::findByUsername);
    }
    private Function<RegisterDto, AppUser> mapToAppUser = r -> {
        AppUser user = new AppUser();
        user.setUsername(r.getUsername());
        user.setFullName(r.getFullName());
        user.setEmail(r.getEmail());
        user.setCreateDate(new Date());
        user.setRole("user");
        user.setPassword(encoder.encode(r.getPass()));
        return user;
    };
    // ---------------------------------------------------------
    public List<AppUser> fetchAllAppUsers() {
        return repo.findAll();
    }
}