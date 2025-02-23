package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.CreateServiceRequestDTO;
import com.stelliocode.backend.dto.ServiceResponseDTO;
import com.stelliocode.backend.entity.Service;
import com.stelliocode.backend.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

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