package com.axys.redeflexmobile.shared.services.network.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class JsonExcludeStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getAnnotation(JsonExclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
