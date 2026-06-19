package com.FirstProject.TinyURL.repository;

import com.FirstProject.TinyURL.Model.UrlMapping;
import com.FirstProject.TinyURL.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByShortCode(String shortCode);

    long deleteByExpirationDateBefore(LocalDateTime now);

    List<UrlMapping> findByOwnerOrderByCreatedDateDesc(User owner);

    boolean existsByShortCode(String shortCode);

    long countByOwner(User owner);

    long countByOwnerAndExpirationDateAfter(User owner, LocalDateTime now);

    long countByOwnerAndExpirationDateBefore(User owner,LocalDateTime now);
}
