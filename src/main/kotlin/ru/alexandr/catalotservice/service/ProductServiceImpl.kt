package ru.alexandr.catalotservice.service

import org.springframework.stereotype.Service
import ru.alexandr.catalotservice.dto.CatalogProductDto
import ru.alexandr.catalotservice.repository.ProductRepository

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository
) : ProductService {

    override fun getProductsByArticles(articles: List<String>): List<CatalogProductDto> {
        if (articles.isEmpty()) {
            return emptyList()
        }

        return productRepository.findAllByArticleIn(articles.distinct())
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
    }
}