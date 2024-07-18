package com.afis.api.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afis.api.model.AfisApiRequestBody;
import com.afis.api.service.PyramidMatcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sourceAfisApi")
@CrossOrigin
class SourceAfisApiServlet {
	@Autowired
	PyramidMatcher pyramidMatcher;

	@PostMapping(produces = "application/json")
    protected ResponseEntity<Map<String, Object>> doPyramidVerification(@RequestBody AfisApiRequestBody requestBody) {
        
        String probe = requestBody.getProbe();
        String candidate = requestBody.getCandidate();
        
        String[] candidateSplit = candidate.split(",");
        String[] probeSplit = probe.split(",");

        byte[] candidateBytes = Base64.getDecoder().decode(candidateSplit[1]);
         byte[] probeBytes = Base64.getDecoder().decode(probeSplit[1]);

        double score = 0.0;
        HttpStatus statusCode = HttpStatus.OK;

        try {
            final long startTime = System.currentTimeMillis();
             score = pyramidMatcher.verify(probeBytes, candidateBytes);
            final long endTime = System.currentTimeMillis();
            log.info("Match time: {} ms", (endTime - startTime));
            log.info("Match score: {}", String.format("%.2f",score));

        } catch(Exception e) {
            log.error("Caught an exception during verification", e);
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        Map<String, Object> response = new HashMap<>();

        response.put("status", statusCode == HttpStatus.OK ? "success" : "error");
        response.put("probe", probe);
        response.put("candidate", candidate);
        response.put("score", score);
        
        return ResponseEntity.status(statusCode)
                             .body(response);

    }
}