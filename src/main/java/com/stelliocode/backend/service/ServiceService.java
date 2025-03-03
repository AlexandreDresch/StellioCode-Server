package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.CreateServiceRequestDTO;
import com.stelliocode.backend.dto.ServiceResponseDTO;
import com.stelliocode.backend.entity.Service;
import com.stelliocode.backend.repository.ProjectRepository;
import com.stelliocode.backend.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ProjectRepository projectRepository;

    public List<ServiceResponseDTO> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::mapToServiceResponseDTO)
                .collect(Collectors.toList());
    }

    public ServiceResponseDTO getServiceById(UUID id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found."));
        return mapToServiceResponseDTO(service);
    }

    @Transactional
    public ServiceResponseDTO createService(CreateServiceRequestDTO request) {
        Service service = Service.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .duration(request.getDuration())
                .isActive(request.isActive())
                .build();

        Service savedService = serviceRepository.save(service);
        return mapToServiceResponseDTO(savedService);
    }

    @Transactional
    public ServiceResponseDTO updateService(UUID id, CreateServiceRequestDTO request) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found."));

        service.setTitle(request.getTitle());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setCategory(request.getCategory());
        service.setDuration(request.getDuration());
        service.setActive(request.isActive());

        Service updatedService = serviceRepository.save(service);
        return mapToServiceResponseDTO(updatedService);
    }

    @Transactional
    public void deleteService(UUID id) {
        if (!serviceRepository.existsById(id)) {
            throw new RuntimeException("Service not found.");
        }
        serviceRepository.deleteById(id);
    }


    public Map<String, Integer> getServiceProjectCountsSinceStartOfYear() {
        LocalDateTime startOfYear = LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);

        List<Object[]> projectCounts = projectRepository.countProjectsByServiceSince(startOfYear);

        Map<String, Integer> result = new HashMap<>();

        for (Object[] row : projectCounts) {
            String serviceTitle = (String) row[1];
            Long count = (Long) row[2];
            result.put(serviceTitle, count != null ? count.intValue() : 0);
        }

        List<Service> allServices = serviceRepository.findAll();
        for (Service service : allServices) {
            result.putIfAbsent(service.getTitle(), 0);
        }

        return result;
    }

    private ServiceResponseDTO mapToServiceResponseDTO(Service service) {
        return ServiceResponseDTO.builder()
                .id(service.getId())
                .title(service.getTitle())
                .description(service.getDescription())
                .price(service.getPrice())
                .category(service.getCategory())
                .duration(service.getDuration())
                .isActive(service.isActive())
                .createdAt(service.getCreatedAt())
                .updatedAt(service.getUpdatedAt())
                .build();
    }
}