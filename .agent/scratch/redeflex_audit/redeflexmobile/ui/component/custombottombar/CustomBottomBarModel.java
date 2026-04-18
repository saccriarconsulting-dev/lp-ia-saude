package com.axys.redeflexmobile.ui.component.custombottombar;

import androidx.fragment.app.Fragment;

/**
 * Created by Rogério Massa on 19/07/2018.
 */

public class CustomBottomBarModel {

    private CustomBottomBarModelTypeEnum type;
    private Integer icon;
    private String description;
    private Fragment target;
    private String externalUrl;

    public CustomBottomBarModel() {
    }

    public CustomBottomBarModel(CustomBottomBarModelTypeEnum type,
                                Integer icon,
                                String description) {
        this.type = type;
        this.icon = icon;
        this.description = description;
    }

    public CustomBottomBarModel(CustomBottomBarModelTypeEnum type,
                                Integer icon,
                                String description,
                                Fragment target) {
        this.type = type;
        this.icon = icon;
        this.description = description;
        this.target = target;
    }

    public CustomBottomBarModelTypeEnum getType() {
        return type;
    }

    public void setType(CustomBottomBarModelTypeEnum type) {
        this.type = type;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Fragment getTarget() {
        return target;
    }

    public void setTarget(Fragment target) {
        this.target = target;
    }

    public enum CustomBottomBarModelTypeEnum {
        ROUTES, REGISTER, MAP, RANKING
    }
}
