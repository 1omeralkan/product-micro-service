package com.omeralkan.product.service;

import com.omeralkan.product.repository.ErrorMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrorMessageService {

    private final ErrorMessageRepository errorMessageRepository;

    private static final String DEFAULT_LANGUAGE = "tr";
    private static final String FALLBACK_MESSAGE = "Beklenmeyen bir hata oluştu.";

    public String getMessage(String errorCode, String language) {
        // Gelen dil null veya boşsa varsayılan olarak Türkçe kullan
        String lang = (language == null || language.isBlank()) ? DEFAULT_LANGUAGE : language;

        // Veritabanından mesajı çek, bulamazsa fallback mesaj dön
        return errorMessageRepository.findByErrorCodeAndLanguage(errorCode, lang)
                .map(entity -> entity.getMessage())
                .orElse(FALLBACK_MESSAGE);
    }
}