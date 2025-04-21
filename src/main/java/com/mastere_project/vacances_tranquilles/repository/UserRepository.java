package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
