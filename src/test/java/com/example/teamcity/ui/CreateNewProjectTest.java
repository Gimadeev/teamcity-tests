package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import com.example.teamcity.ui.pages.admin.EditProject;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import com.example.teamcity.ui.texts.Errors;
import com.example.teamcity.ui.texts.InfoTexts;
import org.testng.annotations.Test;

public class CreateNewProjectTest extends BaseUiTest {
    @Test
    public void authorizedUserShouldBeAbleToCreateNewProjectByUrl() {
        var testData = testDataStorage.addTestData();
        var url = Config.getProperty("githubTestProject");

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));
    }

    @Test
    public void authorizedUserShouldBeAbleToCreateNewProjectManually() {
        var testData = testDataStorage.addTestData();

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectManually(testData.getProject());

        var project = new CheckedProject(Specifications.getSpec().superUserSpec())
                .get(testData.getProject().getId());

        softy.assertThat(EditProject.getSuccessMessageText())
                .isEqualTo(String.format(InfoTexts.PROJECT_SUCCESSFULLY_CREATED, testData.getProject().getName()));
        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
        softy.assertThat(project.getName()).isEqualTo(testData.getProject().getName());
        softy.assertThat(project.getDescription()).isEqualTo(testData.getProject().getDescription());
    }

    @Test
    public void nameMustNotBeEmpty() {
        var testData = testDataStorage.addTestData();

        loginAsUser(testData.getUser());

        testData.getProject().setName("");

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectManually(testData.getProject());

        softy.assertThat(CreateNewProject.getErrorNameMessage()).isEqualTo(Errors.EMPTY_PROJECT_NAME);
    }

    @Test
    public void externalIdMustNotBeEmpty() {
        var testData = testDataStorage.addTestData();

        loginAsUser(testData.getUser());

        testData.getProject().setId("");

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectManually(testData.getProject());

        softy.assertThat(CreateNewProject.getErrorExternalIdMessage()).isEqualTo(Errors.EMPTY_PROJECT_ID);
    }
}
