package com.uzumtech.finespenalties.service.impl;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.dto.response.CodeArticleResponse;
import com.uzumtech.finespenalties.entity.CodeArticleEntity;
import com.uzumtech.finespenalties.exception.CodeArticleIdInvalidException;
import com.uzumtech.finespenalties.mapper.CodeArticleMapper;
import com.uzumtech.finespenalties.repository.CodeArticleRepository;
import com.uzumtech.finespenalties.service.intr.CodeArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CodeArticleServiceImpl implements CodeArticleService {
    private final CodeArticleRepository codeArticleRepository;
    private final CodeArticleMapper codeArticleMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CodeArticleResponse> list(String search, Pageable pageable) {
        Page<CodeArticleEntity> codeArticles = codeArticleRepository.findAllWithSearch(search, pageable);

        return codeArticles.map(codeArticleMapper::entityToResponse);
    }


    public CodeArticleEntity findByIdOrThrowBadRequestException(Long codeArticleId) {
        return codeArticleRepository.findById(codeArticleId).orElseThrow(() -> new CodeArticleIdInvalidException(ErrorCode.CODE_ARTICLE_NOT_FOUND));
    }
}
