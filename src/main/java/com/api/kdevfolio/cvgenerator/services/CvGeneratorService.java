package com.api.kdevfolio.cvgenerator.services;

import com.api.kdevfolio.cvgenerator.dtos.GenerateCvRequest;
import com.api.kdevfolio.cvgenerator.dtos.GenerateCvResponse;
import com.api.kdevfolio.jobapplications.enums.CvProfile;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class CvGeneratorService {

    private final Client geminiClient;
    private final ResourceLoader resourceLoader;

    @Value("${gemini.model:gemini-flash-latest}")
    private String model;

    public CvGeneratorService(
            Client geminiClient,
            ResourceLoader resourceLoader
    ) {
        this.geminiClient = geminiClient;
        this.resourceLoader = resourceLoader;
    }

    public GenerateCvResponse generate(GenerateCvRequest request) {

        if (request.profileType() == null) {
            throw new IllegalArgumentException("CV profile is required.");
        }

        if (request.jobDescription() == null ||
                request.jobDescription().isBlank()) {
            throw new IllegalArgumentException("Job description is required.");
        }

        String profileFolder = request.profileType()
                .name()
                .toLowerCase();

        String template = loadResource(
                "classpath:cv/" + profileFolder + "/cv-" + profileFolder + ".tex"
        );

        String guide = loadResource(
                "classpath:cv/" + profileFolder + "/cv-" + profileFolder + "-guide.md"
        );

        String prompt = buildPrompt(
                request.profileType(),
                template,
                guide,
                request.jobDescription()
        );

        prompt = "Generate a random number from 10 to 100";

        GenerateContentResponse response =
                geminiClient.models.generateContent(model, prompt, null);

//        String latexCode = cleanLatex(response.text());
        String latexCode = response.text();
//        if (latexCode.isBlank() ||
//                !latexCode.contains("\\documentclass") ||
//                !latexCode.contains("\\begin{document}")) {
//            throw new IllegalStateException(
//                    "Gemini returned an invalid LaTeX document."
//            );
//        }

        return new GenerateCvResponse(
                request.profileType(),
                latexCode
        );
    }

    private String loadResource(String location) {
        try {
            Resource resource = resourceLoader.getResource(location);

            if (!resource.exists()) {
                throw new IllegalStateException(
                        "CV resource not found: " + location
                );
            }

            return new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to load CV resource: " + location,
                    exception
            );
        }
    }

    private String buildPrompt(
            CvProfile profile,
            String template,
            String guide,
            String jobDescription
    ) {
        return """
            You are an expert technical CV writer and LaTeX specialist.

            YOUR TASK:
            Generate a complete, customized LaTeX CV for the job offer
            provided below.

            SELECTED PROFILE:
            %s

            PROFILE GUIDE:
            %s

            ORIGINAL LATEX TEMPLATE:
            %s

            JOB DESCRIPTION:
            %s

            STRICT REQUIREMENTS:

            1. Return a complete, compilable LaTeX document.
            2. Preserve the original template's layout, commands,
               typography, packages, and overall visual identity.
            3. Tailor the professional summary, skills, and experience
               to the job requirements.
            4. Prioritize relevant keywords without keyword stuffing.
            5. Never invent employers, qualifications, dates, metrics,
               technologies, or professional achievements.
            6. Preserve factual information from the original template.
            7. Do not remove relevant experience just to shorten the CV.
            8. Use professional, concise language.
            9. Escape LaTeX special characters correctly.
            10. Do not add explanations, Markdown fences, or commentary.

            OUTPUT:
            Return ONLY the complete LaTeX source code.
            """.formatted(
                profile.name(),
                guide,
                template,
                jobDescription
        );
    }

    private String cleanLatex(String response) {
        if (response == null) {
            return "";
        }

        String result = response.trim();

        if (result.startsWith("```latex")) {
            result = result.substring(8);
        } else if (result.startsWith("```tex")) {
            result = result.substring(6);
        } else if (result.startsWith("```")) {
            result = result.substring(3);
        }

        if (result.endsWith("```")) {
            result = result.substring(0, result.length() - 3);
        }

        return result.trim();
    }
}