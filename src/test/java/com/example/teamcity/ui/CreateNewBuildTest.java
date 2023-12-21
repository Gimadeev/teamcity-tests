package com.example.teamcity.ui;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.admin.CreateBuildTypeMenu;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import com.example.teamcity.ui.pages.admin.EditBuildType;
import com.example.teamcity.ui.texts.Errors;
import com.example.teamcity.ui.texts.InfoTexts;
import org.testng.annotations.Test;

public class CreateNewBuildTest extends BaseUiTest{
    @Test
    public void authorizedUserShouldBeAbleToCreateBuildConfiguration() {
        var testData = testDataStorage.addTestData();

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectManually(testData.getProject())
                .clickToCreateBuildConfigBtn()
                .createBuildType(testData.getBuildType());

        var build = new CheckedBuildConfig(Specifications.getSpec().superUserSpec())
                .get(testData.getBuildType().getId());

        softy.assertThat(EditBuildType.getSuccessMessageText())
                .isEqualTo(InfoTexts.BUILD_SUCCESSFULLY_CREATED);
        softy.assertThat(build.getName()).isEqualTo(testData.getBuildType().getName());
        softy.assertThat(build.getId()).isEqualTo(testData.getBuildType().getId());
    }

    @Test
    public void newBuildNameMustNotBeEmpty() {
        var testData = testDataStorage.addTestData();

        loginAsUser(testData.getUser());

        testData.getBuildType().setName("");

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectManually(testData.getProject())
                .clickToCreateBuildConfigBtn()
                .createBuildType(testData.getBuildType());

        softy.assertThat(CreateBuildTypeMenu.getErrorBuildTypeNameText()).isEqualTo(Errors.EMPTY_BUILD_TYPE_NAME);
    }

    @Test
    public void newBuildIdMustNotBeEmpty() {
        var testData = testDataStorage.addTestData();

        loginAsUser(testData.getUser());

        testData.getBuildType().setId("");

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectManually(testData.getProject())
                .clickToCreateBuildConfigBtn()
                .createBuildType(testData.getBuildType());

        softy.assertThat(CreateBuildTypeMenu.getErrorBuildTypeIdText()).isEqualTo(Errors.EMPTY_BUILD_TYPE_ID);
    }

    @Test
    public void newBuildIdMustMeetRequirements() {
        var testData = testDataStorage.addTestData();
        String invalidId = "-";

        loginAsUser(testData.getUser());

        testData.getBuildType().setId(invalidId);

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectManually(testData.getProject())
                .clickToCreateBuildConfigBtn()
                .createBuildType(testData.getBuildType());

        softy.assertThat(CreateBuildTypeMenu.getErrorBuildTypeIdText())
                .isEqualTo(String.format(Errors.INVALID_BUILD_TYPE_ID, invalidId, invalidId));
    }

    @Test
    public void newBuildIdMustNotExceedLimitCharacters() {
        var testData = testDataStorage.addTestData();
        String invalidId = RandomData.getCustomLengthString(226);

        loginAsUser(testData.getUser());

        testData.getBuildType().setId(invalidId);

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectManually(testData.getProject())
                .clickToCreateBuildConfigBtn()
                .createBuildType(testData.getBuildType());

        softy.assertThat(CreateBuildTypeMenu.getErrorBuildTypeIdText())
                .isEqualTo(String.format(Errors.LONG_BUILD_TYPE_ID, invalidId, invalidId.length()));
    }
}
