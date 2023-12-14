package com.example.teamcity.ui;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.selector.ByAttribute;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.element;

public class CreateNewProjectTest extends BaseUiTest{
    @Test
    public void authorizedUserShouldBeAbleToCreateNewProject() {
        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        Selenide.open("/login.html");

        var usernameInput = element(new ByAttribute("id", "username"));
        var passwordInput = element(new ByAttribute("id", "password"));
        var loginButton = element(new ByAttribute("type", "submit"));

        usernameInput.sendKeys(testData.getUser().getUsername());
        passwordInput.sendKeys(testData.getUser().getPassword());

        loginButton.click();
    }
}
