package org.grails.forge

import io.micronaut.context.ApplicationContext
import io.micronaut.context.BeanContext
import org.grails.forge.application.ApplicationType
import org.grails.forge.application.OperatingSystem
import org.grails.forge.application.Project
import org.grails.forge.application.generator.GeneratorContext
import org.grails.forge.build.dependencies.Coordinate
import org.grails.forge.build.dependencies.CoordinateResolver
import org.grails.forge.build.dependencies.LookupFailedException
import org.grails.forge.build.gradle.GradleBuild
import org.grails.forge.build.gradle.GradleBuildCreator
import org.grails.forge.feature.Features
import org.grails.forge.feature.build.gradle.templates.buildGradle
import org.grails.forge.feature.build.gradle.templates.buildSrcBuildGradle
import org.grails.forge.fixture.ContextFixture
import org.grails.forge.fixture.ProjectFixture
import org.grails.forge.options.*

import java.util.function.Function

class BuildBuilder implements ProjectFixture, ContextFixture {

    private List<String> features
    private TestFramework testFramework
    private ApplicationType applicationType
    private JdkVersion jdkVersion
    private GormImpl gormImpl
    private ServletImpl servletImpl
    private OperatingSystem operatingSystem
    private Project project
    private ApplicationContext ctx
    private GradleBuildCreator gradleDependencyResolver

    BuildBuilder(ApplicationContext ctx) {
        this.ctx = ctx
        this.gormImpl = GormImpl.DEFAULT_OPTION
        this.servletImpl = ServletImpl.DEFAULT_OPTION
        this.operatingSystem = OperatingSystem.DEFAULT
    }

    BuildBuilder(ApplicationContext ctx,
                 GormImpl gormImpl,
                 ServletImpl servletImpl) {
        this.ctx = ctx
        this.gormImpl = gormImpl;
        this.servletImpl = servletImpl;
    }

    BuildBuilder features(List<String> features) {
        this.features = features
        this
    }

    BuildBuilder testFramework(TestFramework testFramework) {
        this.testFramework = testFramework
        this
    }

    BuildBuilder applicationType(ApplicationType applicationType) {
        this.applicationType = applicationType
        this
    }

    BuildBuilder jdkVersion(JdkVersion jdkVersion) {
        this.jdkVersion = jdkVersion
        this
    }

    BuildBuilder gormImpl(GormImpl gormImpl) {
        this.gormImpl = gormImpl
        this
    }

    BuildBuilder servletImpl(ServletImpl servletImpl) {
        this.servletImpl = servletImpl
        this
    }

    BuildBuilder project(Project project) {
        this.project = project
        this
    }

    String render() {
        List<String> featureNames = this.features ?: []
        TestFramework testFramework = this.testFramework ?: TestFramework.SPOCK
        ApplicationType type = this.applicationType ?: ApplicationType.WEB
        Project project = this.project ?: buildProject()
        JdkVersion jdkVersion = this.jdkVersion ?: JdkVersion.DEFAULT_OPTION

        final Options options = new Options(testFramework, gormImpl, servletImpl, jdkVersion, operatingSystem)
        Features features = getFeatures(featureNames, options, type)

        GradleBuild build = gradleBuild(options, features, project, type)
        CoordinateResolver resolver = ctx.getBean(CoordinateResolver);
        Function<String, Coordinate> coordinateResolver = (artifactId) -> resolver.resolve(artifactId).orElseThrow(() -> new LookupFailedException(artifactId))
        return buildGradle.template(type, project, coordinateResolver, features, build).render().toString()
    }

    String renderBuildSrc() {
        List<String> featureNames = this.features ?: []
        featureNames.add("gradle-build-src")
        TestFramework testFramework = this.testFramework ?: TestFramework.SPOCK
        ApplicationType type = this.applicationType ?: ApplicationType.WEB
        Project project = this.project ?: buildProject()
        JdkVersion jdkVersion = this.jdkVersion ?: JdkVersion.DEFAULT_OPTION

        Options options = new Options(testFramework, jdkVersion)
        Features features = getFeatures(featureNames, options, type)

        GradleBuild build = gradleBuild(options, features, project, type)
        return buildSrcBuildGradle.template(type, project, features, build).render().toString()
    }

    private GradleBuildCreator getGradleDependencyResolver() {
        if (gradleDependencyResolver == null) {
            gradleDependencyResolver = ctx.getBean(GradleBuildCreator)
        }
        gradleDependencyResolver
    }

    GradleBuild gradleBuild(Options options, Features features, Project project, ApplicationType type) {
        GeneratorContext ctx = createGeneratorContextAndApplyFeatures(options, features, project, type)
        getGradleDependencyResolver().create(ctx)
    }

    GeneratorContext createGeneratorContextAndApplyFeatures(Options options, Features features, Project project, ApplicationType type) {
        GeneratorContext ctx = new GeneratorContext(project, type, options, null, features.features, ctx.getBean(CoordinateResolver))
        features.features.each { feat -> feat.apply(ctx) }
        ctx
    }

    @Override
    BeanContext getBeanContext() {
        ctx
    }
}
