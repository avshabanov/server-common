package com.alexshabanov.mwa.service;

/**
 * @author Alexander Shabanov
 */
public class DefaultSampleService implements SampleService {

  private String greeting;

  public DefaultSampleService() {
    greeting = "Hello";
  }

  @Override
  public String getGreeting(int param) {
    return greeting + "#" + param;
  }
}
