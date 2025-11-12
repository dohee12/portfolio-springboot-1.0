package com.example.portfoliospring1.controller;

import com.example.portfoliospring1.controller.response.BaseResponse;
import com.example.portfoliospring1.domain.dto.ProjectDto;
import com.example.portfoliospring1.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/get-projects")
    public BaseResponse<List<ProjectDto>> getProjects(Authentication authentication) {
        System.out.println("authentication.getPrincipal() " + authentication.getPrincipal());
        return new BaseResponse<>(projectService.getProjects());
    }
}
