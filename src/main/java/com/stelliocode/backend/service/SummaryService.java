package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.SummaryDTO;
import com.stelliocode.backend.repository.UserRepository;
import com.stelliocode.backend.repository.ProjectRepository;
import com.stelliocode.backend.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class SummaryService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PaymentRepository paymentRepository;

    public SummaryService(UserRepository userRepository, ProjectRepository projectRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.paymentRepository = paymentRepository;
    }

    public SummaryDTO getSummary() {
        Long totalUsers = userRepository.countAllUsers();
        Long totalProjects = projectRepository.countAllProjects();
        Long completedProjects = projectRepository.countCompletedProjects();
        Long pendingProjects = projectRepository.countPendingProjects();
        Double totalRevenue = paymentRepository.calculateTotalRevenue();

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);

        Integer newClients = userRepository.countNewUsers(startOfMonth, endOfMonth);
        Integer newProjects = projectRepository.countNewProjects(startOfMonth, endOfMonth);

        return new SummaryDTO(totalUsers, totalProjects, completedProjects, pendingProjects, totalRevenue, newClients, newProjects);
    }
}
