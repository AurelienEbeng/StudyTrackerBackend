package com.aurelien.study_tracker.authentication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Integer>{

    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUserId(Long user_id);
    void delete(PasswordResetToken token);

}