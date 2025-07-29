package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
        List<Service> findByProviderId(Long providerId);

        @Query("SELECT s FROM Service s " +
                        "JOIN s.provider p " +
                        "WHERE (:category IS NULL OR s.category = :category) " +
                        "AND (p.postalCode = :postalCode) " +
                        "AND NOT EXISTS (" +
                        "   SELECT 1 FROM Schedule sch " +
                        "   WHERE sch.provider = p " +
                        "   AND sch.date = :date " +
                        "   AND sch.startTime < :endTime " +
                        "   AND sch.endTime > :startTime" +
                        ")")
        List<Service> findAvailableServices(
                        @Param("category") String category,
                        @Param("postalCode") String postalCode,
                        @Param("date") LocalDate date,
                        @Param("startTime") LocalTime startTime,
                        @Param("endTime") LocalTime endTime);
}