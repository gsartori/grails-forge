package org.grails.forge

import io.micronaut.context.ApplicationContext
import io.micronaut.core.version.SemanticVersion
import org.grails.forge.fixture.ContextFixture
import org.grails.forge.fixture.ProjectFixture
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

abstract class ApplicationContextSpec extends Specification implements ProjectFixture, ContextFixture {

    Map<String, Object> getConfiguration() {
        [:]
    }

    @Shared
    @AutoCleanup
    ApplicationContext beanContext = ApplicationContext.run(configuration)

    protected static Optional<SemanticVersion> parsePropertySemanticVersion(String template, String propertyName) {
        List<String> lines = template.split("\n")
        for (String line : lines) {
            if (line.contains("<" + propertyName + ">") && line.contains("</" + propertyName + ">")) {
                String version = line.substring(line.indexOf("<" + propertyName + ">") + ("<" + propertyName + ">").length(), line.indexOf("</" + propertyName + ">"))
                return Optional.of(new SemanticVersion(version))
            }
        }
        return Optional.empty()
    }

    protected static Optional<SemanticVersion> parseDependencySemanticVersion(String template, String groupArtifactId) {
        List<String> lines = template.split("\n")
        for (String line : lines) {
            if (line.contains(groupArtifactId)) {
                String str = line.substring(line.indexOf(groupArtifactId) + groupArtifactId.length() + ":".length())
                String version = str.substring(0, str.indexOf("\""))
                return Optional.of(new SemanticVersion(version))
            }
        }
        return Optional.empty()
    }

    protected static Optional<String> parseCommunityGradlePluginVersion(String gradlePluginId, String template) {
        String applyPlugin = 'id "' + gradlePluginId + '" version "'
        List<String> lines = template.split('\n')
        String pluginLine = lines.find { line ->
            line.contains(applyPlugin)
        }
        if (!pluginLine) {
            return Optional.empty()
        }
        String version = pluginLine.substring(pluginLine.indexOf(applyPlugin) + applyPlugin.length())
        if (version.endsWith('"')) {
            version = version.substring(0, version.length() - 1)
        }
        Optional.of(version)
    }
}
