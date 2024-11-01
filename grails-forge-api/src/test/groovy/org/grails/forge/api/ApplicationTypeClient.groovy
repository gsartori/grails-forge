package org.grails.forge.api

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client
import org.grails.forge.application.ApplicationType
import org.grails.forge.options.FeatureFilter

@Client('/')
interface ApplicationTypeClient extends ApplicationTypeOperations {

    @Get("/application-types/{type}/features{?filter*}")
    @Header(name = HttpHeaders.ACCEPT_LANGUAGE, value = "es")
    FeatureList spanishFeatures(ApplicationType type,
                                @Nullable FeatureFilter filter);

    @Get("/application-types/{type}/features/default{?filter*}")
    @Header(name = HttpHeaders.ACCEPT_LANGUAGE, value = "es")
    FeatureList spanishDefaultFeatures(ApplicationType type,
                                       @Nullable FeatureFilter filter);
}
