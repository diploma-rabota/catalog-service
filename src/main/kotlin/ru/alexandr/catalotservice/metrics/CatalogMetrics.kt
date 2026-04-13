package ru.alexandr.catalotservice.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component

@Component
class CatalogMetrics(
    registry: MeterRegistry,
) {
    val homeTimer: Timer = Timer.builder("catalog.home.duration")
        .description("Catalog home processing duration")
        .register(registry)

    val homeErrors: Counter = Counter.builder("catalog.home.errors.total")
        .description("Catalog home errors")
        .register(registry)

    val searchTimer: Timer = Timer.builder("catalog.search.duration")
        .description("Catalog search processing duration")
        .publishPercentileHistogram()
        .register(registry)

    val searchErrors: Counter = Counter.builder("catalog.search.errors.total")
        .description("Catalog search errors")
        .register(registry)

    val searchEmpty: Counter = Counter.builder("catalog.search.empty.total")
        .description("Catalog search empty results")
        .register(registry)

    val searchResultSize: DistributionSummary = DistributionSummary
        .builder("catalog.search.result.size")
        .description("Number of items returned by catalog search")
        .register(registry)

    val searchRequestedLimit: DistributionSummary = DistributionSummary
        .builder("catalog.search.request.limit")
        .description("Requested search limit")
        .register(registry)

    val byArticlesTimer: Timer = Timer.builder("catalog.internal.by_articles.duration")
        .description("Internal by-articles processing duration")
        .publishPercentileHistogram()
        .register(registry)

    val byArticlesErrors: Counter = Counter.builder("catalog.internal.by_articles.errors.total")
        .description("Internal by-articles errors")
        .register(registry)

    val byArticlesRequestedSize: DistributionSummary = DistributionSummary
        .builder("catalog.internal.by_articles.requested.size")
        .description("Requested articles count")
        .register(registry)

    val byArticlesFoundSize: DistributionSummary = DistributionSummary
        .builder("catalog.internal.by_articles.found.size")
        .description("Found products count")
        .register(registry)
}