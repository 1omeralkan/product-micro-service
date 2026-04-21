package com.omeralkan.product.repository;

import com.omeralkan.product.entity.ErrorMessageEntity;
import com.omeralkan.product.entity.ErrorMessageId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ErrorMessageRepository extends JpaRepository<ErrorMessageEntity, ErrorMessageId> {

    Optional<ErrorMessageEntity> findByErrorCodeAndLanguage(String errorCode, String language);
}