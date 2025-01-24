package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.DeveloperResponseDTO;
import com.stelliocode.backend.entity.User;
import com.stelliocode.backend.repository.DeveloperProjectRepository;
import com.stelliocode.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final UserRepository userRepository;
    private final DeveloperProjectRepository developerProjectRepository;

    public Page<DeveloperResponseDTO> getDevelopers(String name, String status, Pageable pageable) {
        String nameFilter = name == null ? "%" : "%" + name + "%";

        Page<User> developers = userRepository.findAllByRoleAndFilters(
                "developer",
                nameFilter,
                status,
                pageable
        );

        return developers.map(developer -> {
            long activeProjects = developerProjectRepository.countActiveProjectsByDeveloperId(developer.getId());
            return DeveloperResponseDTO.builder()
                    .id(developer.getId())
                    .fullName(developer.getFullName())
                    .status(developer.getStatus())
                    .level(developer.getLevel())
                    .activeProjects(activeProjects)
                    .build();
        });
    }
}
