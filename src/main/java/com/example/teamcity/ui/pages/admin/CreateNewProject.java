package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.selector.ByAttribute;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;

import static com.codeborne.selenide.Selenide.element;

public class CreateNewProject extends Page {
    private final SelenideElement urlInput = element(Selectors.byId("url"));
    private final SelenideElement projectNameInput = element(Selectors.byId("projectName"));
    private final SelenideElement buildTypeNameInput = element(Selectors.byId("buildTypeName"));
    private final SelenideElement createManuallyButton = element(new ByAttribute("data-hint-container-id", "create-project"));
    private final SelenideElement nameInput = element(Selectors.byId("name"));
    private final SelenideElement projectIdInput = element(Selectors.byId("externalId"));
    private final SelenideElement descriptionInput = element(Selectors.byId("description"));
    private static final SelenideElement errorName = element(Selectors.byId("errorName"));
    private static final SelenideElement errorExternalId = element(Selectors.byId("errorExternalId"));

    public CreateNewProject open(String parentProjectId) {
        Selenide.open("/admin/createObjectMenu.html?projectId=" + parentProjectId + "&showMode=createProjectMenu&cameFromUrl=http%3A%2F%2Flocalhost%3A8111%2Ffavorite%2Fprojects#createManually");
        waitUntilPageIsLoaded();
        return this;
    }

    public CreateNewProject createProjectByUrl(String url) {
        urlInput.sendKeys(url);
        submit();
        return this;
    }

    public void setupProject(String projectName, String buildTypeName) {
        waitUntilElementIsAppeared(projectNameInput);
        projectNameInput.clear();
        projectNameInput.sendKeys(projectName);
        buildTypeNameInput.clear();
        buildTypeNameInput.sendKeys(buildTypeName);
        submit();
    }

    public EditProject createProjectManually(NewProjectDescription newProjectDescription) {
        waitUntilElementIsAppeared(createManuallyButton);
        createManuallyButton.click();
        nameInput.sendKeys(newProjectDescription.getName());
        projectIdInput.clear();
        projectIdInput.sendKeys(newProjectDescription.getId());
        descriptionInput.sendKeys(newProjectDescription.getDescription());
        submit();
        return new EditProject();
    }

    public static String getErrorNameMessage() {
        return errorName.getText();
    }

    public static String getErrorExternalIdMessage() {
        return errorExternalId.getText();
    }
}
