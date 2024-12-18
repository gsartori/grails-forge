/*
 * Copyright 2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.forge.feature.build.gradle;

import org.grails.forge.application.ApplicationType;
import org.grails.forge.feature.Category;
import org.grails.forge.feature.OneOfFeature;

public interface GradleSettingsFileFeature extends OneOfFeature {

    @Override
    default Class<?> getFeatureClass() {
        return GradleSettingsFileFeature.class;
    }

    @Override
    default boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    default String getCategory() {
        return Category.CONFIGURATION;
    }
}
