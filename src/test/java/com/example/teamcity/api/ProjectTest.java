package com.example.teamcity.api;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class ProjectTest extends BaseApiTest {
    @Test
    public void createProject() {
        var testData = testDataStorage.addTestData();
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        var project = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_OK).extract().as(Project.class);
        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    @Test
    public void projectNameCannotBeEmpty() {
        var testData = testDataStorage.addTestData();
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        testData.getProject().setName("");

        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project name cannot be empty"));
    }

    @Test
    public void projectNameMustConsistOnlyLatinLetters() {
        var testData = testDataStorage.addTestData();
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        testData.getProject().setId("Новый_айди");

        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(Matchers.containsString("ID should start with a latin letter and contain only latin letters"));
    }

    @Test
    public void projectIdCannotBeEmpty() {
        var testData = testDataStorage.addTestData();
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        testData.getProject().setId("");

        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(Matchers.containsString("Project ID must not be empty"));
    }

    @Test
    public void projectIdShouldBeNoMore80Chars() {
        var testData = testDataStorage.addTestData();
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        testData.getProject().setId(RandomData.getCustomLengthString(81));

        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void projectNameShouldBeUnique() {
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(firstTestData.getUser());
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(secondTestData.getUser());

        secondTestData.getProject().setName(firstTestData.getProject().getName());

        new UncheckedProject(Specifications.getSpec().authSpec(firstTestData.getUser())).create(firstTestData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_OK);

        new UncheckedProject(Specifications.getSpec().authSpec(secondTestData.getUser())).create(secondTestData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project with this name already exists: " + secondTestData.getProject().getName()));
    }

    @Test
    public void projectIdShouldBeUnique() {
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(firstTestData.getUser());
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(secondTestData.getUser());

        secondTestData.getProject().setId(firstTestData.getProject().getId());

        new UncheckedProject(Specifications.getSpec().authSpec(firstTestData.getUser())).create(firstTestData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_OK);

        new UncheckedProject(Specifications.getSpec().superUserSpec()).create(secondTestData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project ID \"" + secondTestData.getProject().getId() + "\" is already used by another project"));
    }

    @Test
    public void projectShouldHaveValidParentProject() {
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(firstTestData.getUser());
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(secondTestData.getUser());

        Project parentProject = new UncheckedProject(Specifications.getSpec().authSpec(firstTestData.getUser()))
                .create(firstTestData.getProject()).then().extract().as(Project.class);

        secondTestData.getProject().setParentProject(parentProject);

        new UncheckedProject(Specifications.getSpec().authSpec(firstTestData.getUser()))
                .delete(firstTestData.getProject().getId());

        new UncheckedProject(Specifications.getSpec().authSpec(secondTestData.getUser()))
                .create(secondTestData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Project cannot be found by external id '" + parentProject.getId() + "'"));
    }
}
