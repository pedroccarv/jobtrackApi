package com.pedro.jobtrackapi.service;

import com.pedro.jobtrackapi.dto.technology.CreateTechnologyRequest;
import com.pedro.jobtrackapi.dto.technology.TechnologyResponse;
import com.pedro.jobtrackapi.dto.technology.UpdateTechnologyRequest;
import com.pedro.jobtrackapi.exception.ResourceNotFoundException;
import com.pedro.jobtrackapi.model.Technology;
import com.pedro.jobtrackapi.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    public TechnologyResponse findTechnologyById(Long id) {
        Technology technology = findEntityById(id);
        return toResponse(technology);
    }

    public List<TechnologyResponse> findAllTechnologies() {
        List<Technology> technology = technologyRepository.findAll();
        return technology.stream().map(this::toResponse).toList();
    }

    public TechnologyResponse createTechnology(CreateTechnologyRequest createTechnologyRequest) {
        return  toResponse(technologyRepository.save(toEntity(createTechnologyRequest)));
    }

    public void deleteTechnology(Long id){
        Technology technology = findEntityById(id);
        technologyRepository.delete(technology);
    }

    public TechnologyResponse updateTechnology(Long id, UpdateTechnologyRequest updateTechnologyRequest) {
        Technology entityById = findEntityById(id);
        entityById.setName(updateTechnologyRequest.name());
        return toResponse(technologyRepository.save(entityById));
    }

    private Technology findEntityById(Long id) {
        return  technologyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technology not found"));
    }


    private TechnologyResponse toResponse(Technology technology) {
        return new TechnologyResponse(
                technology.getId(),
                technology.getName()
        );
    }

    private Technology toEntity(CreateTechnologyRequest technologyRequest) {
        Technology technology = new Technology();
        technology.setName(technologyRequest.name());
        return technology;
    }
}
