package com.symphony.bdk.workflow;

import com.symphony.bdk.workflow.engine.ResourceFolderProvider;
import com.symphony.bdk.workflow.engine.ResourceProvider;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.nio.file.Paths;

@TestConfiguration
@Profile("test")
public class IntegrationTestConfiguration {
  @Bean("workflowResourcesProvider")
  public ResourceProvider workflowResourcesProvider() {
    Class<?> currentTestClass = getClass();
    return currentTestClass::getResourceAsStream;
  }

  @Bean("workflowResourcesFolderProvider")
  public ResourceFolderProvider workflowResourcesFolderProvider() {
    return () -> Paths.get("src", "test", "resources").toFile().getAbsolutePath();
  }
}
