package com.afis.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.afis.api.service.PyramidMatcher;

@SpringBootTest
class PyramidMatcherTest {
	@Autowired
	PyramidMatcher pyramidMatcher;
	
     @Test 
     void verifySuccessful() throws IOException, URISyntaxException {
         byte[] probeBytes = Files.readAllBytes(Paths.get(getTestResource("static/touchless_set2_1.png")));
         byte[] candidateBytes = Files.readAllBytes(Paths.get(getTestResource("static/touchless_set1_1.png")));
         assertTrue(pyramidMatcher.verify(probeBytes, candidateBytes) > 300.0);
     }

     private URI getTestResource(String name) throws URISyntaxException {
         return Objects.requireNonNull(getClass().getClassLoader().getResource(name)).toURI();
     }

     @Test
     void verifyFailure() throws IOException, URISyntaxException {
         byte[] probeBytes = Files.readAllBytes(Paths.get(getTestResource("static/touchless_set1_6.png")));
         byte[] candidateBytes = Files.readAllBytes(Paths.get(getTestResource("static/touchless_set1_1.png")));

         assertTrue(pyramidMatcher.verify(probeBytes, candidateBytes) < 50.0);
     }
    
}