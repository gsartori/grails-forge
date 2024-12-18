/*
 * Copyright 2017-2020 original authors
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
package org.grails.forge.feature.other;

import jakarta.inject.Singleton;
import org.grails.forge.application.ApplicationType;
import org.grails.forge.application.generator.GeneratorContext;
import org.grails.forge.build.dependencies.Dependency;
import org.grails.forge.feature.Category;
import org.grails.forge.feature.Feature;

@Singleton
public class HibernateValidator implements Feature {

    @Override
    public String getName() {
        return "hibernate-validator";
    }

    @Override
    public String getTitle() {
        return "Hibernate Validator";
    }

    @Override
    public String getDescription() {
        return "Adds support for the Hibernate Validator";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("org.hibernate")
                .lookupArtifactId("hibernate-validator")
                .implementation());
    }

    @Override
    public String getCategory() {
        return Category.VALIDATION;
    }

    @Override
    public String getDocumentation() {
        return "https://hibernate.org/validator/";
    }
}
