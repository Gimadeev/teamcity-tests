package com.example.teamcity.ui.texts;

public class Errors {
    public static final String EMPTY_PROJECT_NAME = "Project name is empty";
    public static final String EMPTY_PROJECT_ID = "Project ID must not be empty.";
    public static final String EMPTY_BUILD_TYPE_NAME = "Name must not be empty";
    public static final String EMPTY_BUILD_TYPE_ID = "The ID field must not be empty.";
    public static final String INVALID_BUILD_TYPE_ID = "Build configuration or template ID \"%s\" is invalid: starts with non-letter character '%s'. ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters).";
    public static final String LONG_BUILD_TYPE_ID = "Build configuration or template ID \"%s\" is invalid: it is %s characters long while the maximum length is 225. ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters).";
}
