package com.example.teamcity.api.texts;

import lombok.Getter;

@Getter
public final class Errors {
    public static final String CREATING_BUILD_EMPTY_NAME = "When creating a build type, non empty name should be provided";
    public static final String CREATING_BUILD_EMPTY_ID = "Build configuration or template ID must not be empty";
    public static final String CREATING_BUILD_UNIQUE_NAME = "Build configuration with name \"%s\" already exists in project: \"%s\"";
    public static final String CREATING_BUILD_UNIQUE_ID = "ID \"%s\" is already used by another configuration";
}
