/*
 * Copyright 2017-2024 original authors
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
package org.grails.forge.feature.grails;

import jakarta.inject.Singleton;
import org.grails.forge.application.ApplicationType;
import org.grails.forge.application.generator.GeneratorContext;
import org.grails.forge.build.dependencies.Coordinate;
import org.grails.forge.build.dependencies.CoordinateResolver;
import org.grails.forge.build.dependencies.Dependency;
import org.grails.forge.build.dependencies.LookupFailedException;
import org.grails.forge.build.gradle.GradlePlugin;
import org.grails.forge.feature.DefaultFeature;
import org.grails.forge.feature.Feature;
import org.grails.forge.feature.view.GrailsGsp;
import org.grails.forge.feature.web.GrailsWeb;
import org.grails.forge.options.Options;

import java.util.Set;

@Singleton
class GrailsGradlePlugin implements DefaultFeature {

    private final CoordinateResolver resolver;

    GrailsGradlePlugin(CoordinateResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public String getName() {
        return "grails-gradle-plugin";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return true;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        final String artifactId = "grails-gradle-plugin";
        final Coordinate grailsGradlePluginCoordinate = resolver.resolve(artifactId).orElseThrow(() -> new LookupFailedException(artifactId));
        final ApplicationType applicationType = generatorContext.getApplicationType();
        generatorContext.addBuildscriptDependency(Dependency.builder()
                .groupId("org.grails")
                .lookupArtifactId("grails-gradle-plugin")
                .buildSrc());
        if (applicationType == ApplicationType.PLUGIN || applicationType == ApplicationType.WEB_PLUGIN) {
            generatorContext.addBuildPlugin(GradlePlugin.builder().id("org.grails.grails-plugin").version(grailsGradlePluginCoordinate.getVersion()).useApplyPlugin(true).build());
        }
        if (generatorContext.getFeature(GrailsWeb.class).isPresent()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder().id("org.grails.grails-web").version(grailsGradlePluginCoordinate.getVersion()).useApplyPlugin(true).build());
        }
        if (generatorContext.getFeature(GrailsGsp.class).isPresent()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder().id("org.grails.grails-gsp").version(grailsGradlePluginCoordinate.getVersion()).useApplyPlugin(true).build());
        }
        generatorContext.getBuildProperties().put("grailsGradlePluginVersion", grailsGradlePluginCoordinate.getVersion());
    }
}
