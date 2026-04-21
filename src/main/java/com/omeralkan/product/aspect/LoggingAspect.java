package com.omeralkan.product.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect     // Bu sınıf bir Aspect — yani kesişen ilgileri (cross-cutting concerns) yönetir
@Component  // Spring bean olarak kaydet
@Slf4j      // Logger oluştur
public class LoggingAspect {

    // ==================== POINTCUT TANIMLARI ====================
    // Pointcut = "hangi metodlarda çalışacağım" tanımı
    // Bir kere tanımla, birden fazla yerde kullan

    // Controller paketindeki tüm sınıfların tüm metodları
    @Pointcut("within(com.omeralkan.product.controller..*)")
    public void controllerMethods() {}

    // Service/impl paketindeki tüm sınıfların tüm metodları
    @Pointcut("within(com.omeralkan.product.service.impl..*)")
    public void serviceMethods() {}

    // İkisini birleştir — controller VEYA service metodları
    @Pointcut("controllerMethods() || serviceMethods()")
    public void applicationMethods() {}

    // ==================== ADVICE TANIMLARI ====================

    // @Around: Metodun HEM ÖNCESİNDE hem SONRASINDA çalışır
    // Ayrıca metodun ne kadar sürdüğünü ölçebilirsin
    @Around("applicationMethods()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        // Metod adını ve parametrelerini al
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // GİRİŞ LOGU
        log.info("[ENTER] {}.{}() | Args: {}", className, methodName, Arrays.toString(args));

        // Süre ölçümü başlat
        long startTime = System.currentTimeMillis();

        // Asıl metodu çalıştır
        Object result = joinPoint.proceed();

        // Süre ölçümü bitir
        long duration = System.currentTimeMillis() - startTime;

        // ÇIKIŞ LOGU
        log.info("[EXIT] {}.{}() | Duration: {}ms", className, methodName, duration);

        return result;
    }

    // @AfterThrowing: Metod HATA fırlattığında çalışır
    @AfterThrowing(pointcut = "applicationMethods()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        log.error("[ERROR] {}.{}() | Exception: {} | Message: {}",
                className, methodName,
                exception.getClass().getSimpleName(),
                exception.getMessage());
    }
}