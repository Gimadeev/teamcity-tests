package com.example.teamcity.api;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.api.texts.Errors;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class BuildConfigurationTest extends BaseApiTest {
    @Test
    public void createBuildConfiguration() {
        var testData = testDataStorage.addTestData();
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        var buildConfiguration = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType());

        softy.assertThat(buildConfiguration.getId()).isEqualTo(testData.getBuildType().getId());
        softy.assertThat(buildConfiguration.getName()).isEqualTo(testData.getBuildType().getName());
        softy.assertThat(buildConfiguration.getProject().getId()).isEqualTo(testData.getBuildType().getProject().getId());
    }

    @Test
    public void buildNameCannotBeEmpty() {
        var testData = testDataStorage.addTestData();
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        testData.getBuildType().setName("");

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(Errors.CREATING_BUILD_EMPTY_NAME));
    }

    @Test
    public void buildIdCannotBeEmpty() {
        var testData = testDataStorage.addTestData();
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        testData.getBuildType().setId("");

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(Matchers.containsString(Errors.CREATING_BUILD_EMPTY_ID));
    }

    @Test
    public void buildIdShouldBeUnique() {
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(firstTestData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(firstTestData.getUser()))
                .create(firstTestData.getProject());

        secondTestData.getBuildType().setId(firstTestData.getBuildType().getId());
        secondTestData.getBuildType().setProject(firstTestData.getProject());

        new CheckedBuildConfig(Specifications.getSpec().authSpec(firstTestData.getUser()))
                .create(firstTestData.getBuildType());

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(firstTestData.getUser()))
                .create(secondTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(String.format(Errors.CREATING_BUILD_UNIQUE_ID,
                        secondTestData.getBuildType().getId())));
    }

    @Test
    public void buildNameShouldBeUnique() {
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(firstTestData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(firstTestData.getUser()))
                .create(firstTestData.getProject());

        secondTestData.getBuildType().setName(firstTestData.getBuildType().getName());
        secondTestData.getBuildType().setProject(firstTestData.getProject());

        new CheckedBuildConfig(Specifications.getSpec().authSpec(firstTestData.getUser()))
                .create(firstTestData.getBuildType());

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(firstTestData.getUser()))
                .create(secondTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(String.format(Errors.CREATING_BUILD_UNIQUE_NAME,
                        secondTestData.getBuildType().getName(),
                        firstTestData.getProject().getName())));
    }

    @Test
    public void createMultipleBuildConfigurationsInOneProject() {
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();
        var thirdTestData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec()).create(firstTestData.getUser());

        Project project = new CheckedProject(Specifications.getSpec()
                .authSpec(firstTestData.getUser()))
                .create(firstTestData.getProject());

        secondTestData.getBuildType().setProject(firstTestData.getProject());
        thirdTestData.getBuildType().setProject(firstTestData.getProject());

        new CheckedBuildConfig(Specifications.getSpec().authSpec(firstTestData.getUser()))
                .create(firstTestData.getBuildType());

        new CheckedBuildConfig(Specifications.getSpec().authSpec(firstTestData.getUser()))
                .create(secondTestData.getBuildType());

        new CheckedBuildConfig(Specifications.getSpec().authSpec(firstTestData.getUser()))
                .create(thirdTestData.getBuildType());

        Project response = new UncheckedProject(Specifications.getSpec().authSpec(firstTestData.getUser()))
                .get(project.getId()).then().assertThat()
                .body(Matchers.containsString(firstTestData.getBuildType().getId()))
                .body(Matchers.containsString(secondTestData.getBuildType().getId()))
                .body(Matchers.containsString(thirdTestData.getBuildType().getId()))
                .extract().as(Project.class);

        softy.assertThat(response.getBuildTypes().getCount()).isEqualTo(3);
    }

    @Test
    public void buildIdShouldBeNoMore80Chars() {
        var testData = testDataStorage.addTestData();
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        testData.getBuildType().setId(RandomData.getCustomLengthString(81));

        new CheckedProject(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getProject());

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
