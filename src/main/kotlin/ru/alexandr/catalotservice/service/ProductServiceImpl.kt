package ru.alexandr.catalotservice.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.alexandr.catalotservice.dto.CatalogProductDto
import ru.alexandr.catalotservice.metrics.CatalogMetrics
import ru.alexandr.catalotservice.repository.ProductRepository

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val metrics: CatalogMetrics,
) : ProductService {

    private val log = LoggerFactory.getLogger(ProductServiceImpl::class.java)

    override fun getProductsByArticles(articles: List<String>): List<CatalogProductDto> {
        metrics.byArticlesRequestedSize.record(articles.size.toDouble())

        return try {
            metrics.byArticlesTimer.recordCallable {
                log.info("catalog.internal.by-articles started: requestedArticles={}", articles.size)

                if (articles.isEmpty()) {
                    metrics.byArticlesFoundSize.record(0.0)
                    emptyList()
                } else {
                    val result = productRepository.findAllByArticleIn(articles.distinct())
                        .map { product ->
                            CatalogProductDto(
                                article = product.article,
                                name = product.name,
                                price = product.price,
                                wholesalePrice = product.wholesalePrice,
                                minWholesaleQuantity = product.minWholesaleQuantity,
                                stockQuantity = product.stockQuantity,
                                isActive = product.isActive
                            )
                        }

                    metrics.byArticlesFoundSize.record(result.size.toDouble())

                    log.info(
                        "catalog.internal.by-articles finished: requestedUnique={}, found={}",
                        articles.distinct().size,
                        result.size
                    )

                    result
                }
            }!!
        } catch (ex: Exception) {
            metrics.byArticlesErrors.increment()
            log.error("catalog.internal.by-articles failed", ex)
            throw ex
        }
    }
}