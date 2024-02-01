package com.example.orderservice.config;

import org.junit.Ignore;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.time.Duration;

@Ignore
public class IntegrationTest {

    static DockerComposeContainer container;

    static {
        String dockerComposePath = System.getProperty("dockerComposePath");

        container = new DockerComposeContainer(new File(dockerComposePath))
                .withOptions("--compatibility")
                .withLocalCompose(true)
                .withExposedService("db", 5432, Wait.forLogMessage(".*database system is ready to accept connections.*", 1)
                        .withStartupTimeout(Duration.ofSeconds(60)))
                .withExposedService("zookeeper", 2181, Wait.forLogMessage(".*binding to port.*",1)
                        .withStartupTimeout(Duration.ofSeconds(60)))
                .withExposedService("schema-registry", 8081, Wait.forLogMessage(".*listening for requests....*",1)
                        .withStartupTimeout(Duration.ofSeconds(120)));

        container.start();
    }
}
