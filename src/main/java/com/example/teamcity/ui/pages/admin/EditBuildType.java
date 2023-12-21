package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;

import static com.codeborne.selenide.Selenide.element;

public class EditBuildType extends Page {
    private static SelenideElement successMessage = element(Selectors.byClass("successMessage "));

    public static String getSuccessMessageText() {
        return successMessage.getText();
    }
}
