package org.grails.forge.build.gradle

import org.grails.forge.ApplicationContextSpec
import org.grails.forge.application.ApplicationType
import org.grails.forge.fixture.CommandOutputFixture
import org.grails.forge.options.JdkVersion
import org.grails.forge.options.Options
import org.grails.forge.options.TestFramework

class GradleSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "test build properties"() {
        given:
        final def output = generate(ApplicationType.WEB, new Options(TestFramework.SPOCK))
        final String gradleProps = output["gradle.properties"]

        expect:
        gradleProps.contains("org.gradle.caching=true")
        gradleProps.contains("org.gradle.daemon=true")
        gradleProps.contains("org.gradle.parallel=true")
        gradleProps.contains("org.gradle.jvmargs=-Dfile.encoding=UTF-8 -Xmx1024M")
    }

    void "test build gradle"() {
        given:
        final def output = generate(ApplicationType.WEB, new Options(TestFramework.SPOCK))
        final String buildGradle = output["build.gradle"]

        expect:
        buildGradle.contains("eclipse")
        buildGradle.contains("idea")
        buildGradle.contains("war")
    }

    void "test settings.gradle"() {
        given:
        final def output = generate(ApplicationType.WEB, new Options(TestFramework.SPOCK), ["gradle-settings-file"])
        final String settingsGradle = output["settings.gradle"]

        expect:
        settingsGradle.contains("rootProject.name")
    }

    void "test buildSrc/build.gradle"() {
        given:
        def output = generate(ApplicationType.WEB, new Options(TestFramework.SPOCK), ["gradle-build-src"])
        String buildSrcGradle = output["buildSrc/build.gradle"]

        expect:
        buildSrcGradle.contains('repositories')
        buildSrcGradle.contains('dependencies')
    }

    void "no settings.gradle file is created without the 'gradle-settings-file' feature"() {
        given:
        def output = generate(ApplicationType.WEB, new Options(TestFramework.SPOCK))
        String settingsGradle = output["settings.gradle"]

        expect:
        !settingsGradle
    }

    void "no buildSrc/build.gradle file is created without the 'gradle-build-src' feature"() {
        given:
        def output = generate(ApplicationType.WEB, new Options(TestFramework.SPOCK))
        String buildSrcGradle = output["buildSrc/settings.gradle"]

        expect:
        !buildSrcGradle
    }
}
