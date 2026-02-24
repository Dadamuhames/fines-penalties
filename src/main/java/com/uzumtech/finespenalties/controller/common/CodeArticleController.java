package com.uzumtech.finespenalties.controller.common;

import com.uzumtech.finespenalties.dto.response.CodeArticleResponse;
import com.uzumtech.finespenalties.service.intr.CodeArticleService;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fines-penalties/common/code-articles")
public class CodeArticleController {

    private final CodeArticleService codeArticleService;

    @GetMapping
    public ResponseEntity<Page<CodeArticleResponse>> list(
        @RequestParam(defaultValue = "") String search,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") @Max(50) Integer pageSize
    ) {

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<CodeArticleResponse> codeArticles = codeArticleService.list(search, pageable);

        return ResponseEntity.ok(codeArticles);
    }

}
