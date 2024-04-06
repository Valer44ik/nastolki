package com.example.Campaign.Calculator.repo;


import com.example.Campaign.Calculator.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByLoginAndPassword(String username, String password);
    User findByLogin(String username);
}
