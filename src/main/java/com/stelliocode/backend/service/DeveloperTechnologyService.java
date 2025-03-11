package com.stelliocode.backend.service;

import com.stelliocode.backend.entity.Technology;
import com.stelliocode.backend.entity.User;
import com.stelliocode.backend.entity.UserTechnology;
import com.stelliocode.backend.repository.TechnologyRepository;
import com.stelliocode.backend.repository.UserTechnologyRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeveloperTechnologyService {

    private final TechnologyRepository technologyRepository;
    private final UserTechnologyRepository userTechnologyRepository;

    public DeveloperTechnologyService(TechnologyRepository technologyRepository, UserTechnologyRepository userTechnologyRepository) {
        this.technologyRepository = technologyRepository;
        this.userTechnologyRepository = userTechnologyRepository;
    }


    public void updateTechnologies(User developer, List<String> newTechnologies) {
        if (newTechnologies == null) return;

        List<String> currentTechnologies = userTechnologyRepository.findTechnologiesByDeveloperId(developer.getId());

        Set<String> newTechSet = new HashSet<>(newTechnologies);
        Set<String> currentTechSet = new HashSet<>(currentTechnologies);

        Set<String> toAdd = new HashSet<>(newTechSet);
        toAdd.removeAll(currentTechSet);

        Set<String> toRemove = new HashSet<>(currentTechSet);
        toRemove.removeAll(newTechSet);

        if (!toRemove.isEmpty()) {
            userTechnologyRepository.removeTechnologies(developer.getId(), toRemove);
        }

        if (!toAdd.isEmpty()) {
            List<Technology> existingTechnologies = technologyRepository.findByNameIn(toAdd);
            Set<String> existingTechNames = existingTechnologies.stream()
                    .map(Technology::getName)
                    .collect(Collectors.toSet());

            List<Technology> newTechEntities = toAdd.stream()
                    .filter(name -> !existingTechNames.contains(name))
                    .map(Technology::new)
                    .toList();

            if (!newTechEntities.isEmpty()) {
                technologyRepository.saveAll(newTechEntities);
                existingTechnologies.addAll(newTechEntities);
            }

            List<UserTechnology> userTechnologies = existingTechnologies.stream()
                    .map(tech -> new UserTechnology(developer, tech))
                    .toList();

            userTechnologyRepository.saveAll(userTechnologies);
        }
    }
}
