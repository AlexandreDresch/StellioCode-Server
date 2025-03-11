package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.SummaryDTO;
import com.stelliocode.backend.dto.SummaryMetricDTO;
import com.stelliocode.backend.repository.ClientRepository;
import com.stelliocode.backend.repository.UserRepository;
import com.stelliocode.backend.repository.ProjectRepository;
import com.stelliocode.backend.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
public class SummaryService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PaymentRepository paymentRepository;
    private final ClientRepository clientRepository;

    public SummaryService(UserRepository userRepository, ProjectRepository projectRepository, PaymentRepository paymentRepository, ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.paymentRepository = paymentRepository;
        this.clientRepository = clientRepository;
    }

    public SummaryDTO getSummary() {
        LocalDate firstDayCurrentMonth = YearMonth.now().atDay(1);
        LocalDate firstDayPreviousMonth = YearMonth.now().minusMonths(1).atDay(1);

        int revenueCurrent = projectRepository.getTotalRevenueByMonth(firstDayCurrentMonth);
        int revenuePrevious = projectRepository.getTotalRevenueByMonth(firstDayPreviousMonth);
        SummaryMetricDTO totalRevenue = calculateChange(revenueCurrent, revenuePrevious);

        int completedProjectsCurrent = projectRepository.getCompletedProjectsByMonth(firstDayCurrentMonth);
        int completedProjectsPrevious = projectRepository.getCompletedProjectsByMonth(firstDayPreviousMonth);
        SummaryMetricDTO completedProjects = calculateChange(completedProjectsCurrent, completedProjectsPrevious);

        int newProjectsCurrent = projectRepository.getNewProjectsByMonth(firstDayCurrentMonth);
        int newProjectsPrevious = projectRepository.getNewProjectsByMonth(firstDayPreviousMonth);
        SummaryMetricDTO newProjects = calculateChange(newProjectsCurrent, newProjectsPrevious);

        int newClientsCurrent = clientRepository.getNewClientsByMonth(firstDayCurrentMonth);
        int newClientsPrevious = clientRepository.getNewClientsByMonth(firstDayPreviousMonth);
        SummaryMetricDTO newClients = calculateChange(newClientsCurrent, newClientsPrevious);

        return new SummaryDTO(totalRevenue, completedProjects, newProjects, newClients);
    }

    private SummaryMetricDTO calculateChange(int current, int previous) {
        double change;

        if (previous == 0) {
            change = (current == 0) ? 0.0 : 100.0;
        } else {
            change = ((double) (current - previous) / previous) * 100;
        }

        return new SummaryMetricDTO(current, previous, change);
    }
}
