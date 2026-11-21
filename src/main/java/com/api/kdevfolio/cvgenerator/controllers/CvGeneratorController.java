package com.api.kdevfolio.cvgenerator.controllers;

import com.api.kdevfolio.cvgenerator.dtos.GenerateCvRequest;
import com.api.kdevfolio.cvgenerator.dtos.GenerateCvResponse;
import com.api.kdevfolio.cvgenerator.services.CvGeneratorService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cv")
public class CvGeneratorController {

    private final CvGeneratorService cvGeneratorService;

    public CvGeneratorController(
            CvGeneratorService cvGeneratorService
    ) {
        this.cvGeneratorService = cvGeneratorService;
    }

    @PostMapping("/generate")
    public GenerateCvResponse generate(
            @RequestBody GenerateCvRequest request
    ) {
        return cvGeneratorService.generate(request);
    }
}