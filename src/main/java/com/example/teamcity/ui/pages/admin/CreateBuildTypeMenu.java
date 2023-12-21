package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;

import static com.codeborne.selenide.Selenide.element;

public class CreateBuildTypeMenu extends Page {
    private static SelenideElement buildTypeNameInput = element(Selectors.byId("buildTypeName"));
    private static SelenideElement buildTypeExternalIdInput = element(Selectors.byId("buildTypeExternalId"));
    private static SelenideElement buildTypeDescriptionInput = element(Selectors.byId("description"));
    private static SelenideElement errorBuildTypeName = element(Selectors.byId("error_buildTypeName"));
    private static SelenideElement errorBuildTypeExternalId = element(Selectors.byId("error_buildTypeExternalId"));

    public void createBuildType(BuildType buildType) {
        waitUntilElementIsAppeared(buildTypeNameInput);
        buildTypeNameInput.sendKeys(buildType.getName());
        buildTypeExternalIdInput.clear();
        buildTypeExternalIdInput.sendKeys(buildType.getId());
        buildTypeDescriptionInput.sendKeys(buildType.getDescription());
        submit();
    }

    public static String getErrorBuildTypeNameText() {
        return errorBuildTypeName.getText();
    }

    public static String getErrorBuildTypeIdText() {
        return errorBuildTypeExternalId.getText();
    }
}
