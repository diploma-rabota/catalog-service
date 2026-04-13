package ru.alexandr.catalotservice.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.alexandr.catalotservice.dto.home.CatalogHomeResponse
import ru.alexandr.catalotservice.dto.search.CatalogSearchRequest
import ru.alexandr.catalotservice.dto.search.CatalogSearchResponse
import ru.alexandr.catalotservice.metrics.CatalogMetrics
import ru.alexandr.catalotservice.repository.CatalogHomeRepository
import ru.alexandr.catalotservice.repository.CatalogSearchRepository

@Service
class CatalogShowcaseService(
    private val catalogHomeDaoRepository: CatalogHomeRepository,
    private val catalogSearchRepository: CatalogSearchRepository,
    private val metrics: CatalogMetrics,
) {
    private val log = LoggerFactory.getLogger(CatalogShowcaseService::class.java)

    fun getHome(): CatalogHomeResponse {
        return try {
            metrics.homeTimer.recordCallable {
                log.info("catalog.home started")

                val response = catalogHomeDaoRepository.getHome()

                log.info(
                    "catalog.home finished: categories={}, popularProducts={}, topSuppliers={}",
                    response.categories.size,
                    response.popularProducts.size,
                    response.topSuppliers.size
                )

                response
            }!!
        } catch (ex: Exception) {
            metrics.homeErrors.increment()
            log.error("catalog.home failed", ex)
            throw ex
        }
    }

    fun search(request: CatalogSearchRequest): CatalogSearchResponse {
        metrics.searchRequestedLimit.record((request.limit ?: 20).toDouble())

        return try {
            metrics.searchTimer.recordCallable {
                log.info(
                    "catalog.search started: search='{}', categoryIds={}, supplierIds={}, minPrice={}, maxPrice={}, minRating={}, onlyActive={}, onlyInStock={}, minWeeklyGrowthPercent={}, sortBy={}, limit={}, offset={}",
                    request.search,
                    request.categoryIds,
                    request.supplierIds,
                    request.minPrice,
                    request.maxPrice,
                    request.minRating,
                    request.onlyActive,
                    request.onlyInStock,
                    request.minWeeklyGrowthPercent,
                    request.sortBy,
                    request.limit,
                    request.offset
                )

                val response = catalogSearchRepository.search(request)

                metrics.searchResultSize.record(response.items.size.toDouble())
                if (response.items.isEmpty()) {
                    metrics.searchEmpty.increment()
                }

                log.info(
                    "catalog.search finished: total={}, returned={}",
                    response.total,
                    response.items.size
                )

                response
            }!!
        } catch (ex: Exception) {
            metrics.searchErrors.increment()
            log.error("catalog.search failed", ex)
            throw ex
        }
    }
}