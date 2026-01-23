package com.uzumtech.finespenalties.service.intr;

import com.uzumtech.finespenalties.dto.response.CodeArticleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CodeArticleService {

    Page<CodeArticleResponse> list(String search, Pageable pageable);

}
