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
package org.grails.forge.feature.test;

import jakarta.inject.Singleton;
import org.grails.forge.application.ApplicationType;
import org.grails.forge.application.generator.GeneratorContext;
import org.grails.forge.build.dependencies.Dependency;
import org.grails.forge.feature.Category;
import org.grails.forge.feature.Feature;

@Singleton
public class Hamcrest  implements Feature {

    @Override
    public String getName() {
        return "hamcrest";
    }

    @Override
    public String getTitle() {
        return "Hamcrest";
    }

    @Override
    public String getDescription() {
        return "Hamcrest matchers for JUnit";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://hamcrest.org/JavaHamcrest/";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("org.hamcrest")
                .artifactId("hamcrest")
                .testImplementation());
    }
}
