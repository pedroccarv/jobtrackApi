package com.pedro.jobtrackapi.controller;

import com.pedro.jobtrackapi.dto.technology.CreateTechnologyRequest;
import com.pedro.jobtrackapi.dto.technology.TechnologyResponse;
import com.pedro.jobtrackapi.dto.technology.UpdateTechnologyRequest;
import com.pedro.jobtrackapi.service.TechnologyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/technologies")
@RequiredArgsConstructor
public class TechnologyController {

    private final TechnologyService technologyService;

    @GetMapping("/{id}")
    public ResponseEntity<TechnologyResponse> getTechnology(@PathVariable Long id) {
        TechnologyResponse technologyById = technologyService.findTechnologyById(id);
        return ResponseEntity.ok(technologyById);
    }

    @GetMapping
    public ResponseEntity<List<TechnologyResponse>> getAllTechnologies() {
        List<TechnologyResponse> allTechnologies = technologyService.findAllTechnologies();
        return ResponseEntity.ok(allTechnologies);
    }

    @PostMapping
    public ResponseEntity<TechnologyResponse>  createTechnology(@Valid @RequestBody CreateTechnologyRequest createTechnologyRequest) {
        TechnologyResponse technology = technologyService.createTechnology(createTechnologyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(technology);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnology(@PathVariable Long id) {
        technologyService.deleteTechnology(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnologyResponse> updateTechnology(@PathVariable Long id, @Valid @RequestBody UpdateTechnologyRequest updateTechnologyRequest) {
        TechnologyResponse technologyResponse = technologyService.updateTechnology(id, updateTechnologyRequest);
        return ResponseEntity.ok(technologyResponse);
    }
}
