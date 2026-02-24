package com.uzumtech.finespenalties.repository;

import com.uzumtech.finespenalties.entity.CodeArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CodeArticleRepository extends JpaRepository<CodeArticleEntity, Long> {

    @Query("SELECT c FROM CodeArticleEntity c WHERE c.reference LIKE %?1%")
    Page<CodeArticleEntity> findAllWithSearch(String search, Pageable pageable);

}
