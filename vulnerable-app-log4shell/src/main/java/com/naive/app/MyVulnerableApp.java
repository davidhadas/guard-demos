package com.naive.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

@RestController
public class MyVulnerableApp {
    public static final Logger logger = LogManager.getLogger(MyVulnerableApp.class);

    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    @GetMapping(value = {"/", "/index.html"})
    public String hello(@RequestHeader("User-agent") String agent) throws java.io.IOException{
        String str;
        logger.info("Received request from " + agent);
        str = readFile("Banner");

        return "<html><body><pre>\n"+str+"</pre></body></html>\n";
    }
}
