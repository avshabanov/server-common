package com.alexshabanov.mwa.service;

/**
 * @author Alexander Shabanov
 */
public class DefaultSampleService implements SampleService {
    @Override
    public String getGreeting(int param) {
        return "Hello#" + param;
    }
}
