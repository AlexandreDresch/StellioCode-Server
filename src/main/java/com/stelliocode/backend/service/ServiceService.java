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
                .map(service -> new ServiceResponseDTO(
                        service.getId(),
                        service.getTitle(),
                        service.getDescription(),
                        service.getPrice()
                ))
                .collect(Collectors.toList());
    }

    public ServiceResponseDTO getServiceById(UUID id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found."));
        return new ServiceResponseDTO(service.getId(), service.getTitle(), service.getDescription(), service.getPrice());
    }

    @Transactional
    public ServiceResponseDTO createService(CreateServiceRequestDTO request) {
        Service service = Service.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        Service savedService = serviceRepository.save(service);
        return new ServiceResponseDTO(savedService.getId(), savedService.getTitle(), savedService.getDescription(), savedService.getPrice());
    }

    @Transactional
    public ServiceResponseDTO updateService(UUID id, CreateServiceRequestDTO request) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        service.setTitle(request.getTitle());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());

        serviceRepository.save(service);
        return new ServiceResponseDTO(service.getId(), service.getTitle(), service.getDescription(), service.getPrice());
    }

    @Transactional
    public void deleteService(UUID id) {
        if (!serviceRepository.existsById(id)) {
            throw new RuntimeException("Service not found.");
        }
        serviceRepository.deleteById(id);
    }
}
