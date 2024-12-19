package com.example.side.project_event.planner.repository;

import com.example.side.project_event.planner.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//repositories are for accessing database
public interface UserRepository extends JpaRepository<User, String> {
    User findUserByUsername(String username);

    User findByEmail(String email);
}
