package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.selector.ByText;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;

import static com.codeborne.selenide.Selenide.element;

public class EditProject extends Page {
    private SelenideElement messageProjectCreated = element(Selectors.byId("message_projectCreated"));
    private SelenideElement nameInput = element(Selectors.byId("name"));
    private static SelenideElement projectIdInput = element(Selectors.byId("externalId"));
    private static SelenideElement successMessage = element(Selectors.byId("message_projectCreated"));
    private static SelenideElement createBuildConfigButton = element(new ByText("Create build configuration"));

    public static String getSuccessMessageText() {
        return successMessage.getText();
    }

    public CreateBuildTypeMenu clickToCreateBuildConfigBtn() {
        createBuildConfigButton.click();
        return new CreateBuildTypeMenu();
    }
}
