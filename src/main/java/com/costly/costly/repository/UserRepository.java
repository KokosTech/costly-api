package com.costly.costly.repository;

import com.costly.costly.model.User;
import com.costly.costly.model.List;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    @Query("SELECT lists FROM User u JOIN u.lists lists WHERE u.id = :userId AND lists.deletedAt IS NULL")
    java.util.List<List> findListByUserId(Long userId);

    User findByEmail(String email);
}
