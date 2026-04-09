package ru.alexandr.catalotservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alexandr.catalotservice.entity.ProductEntity

interface ProductRepository : JpaRepository<ProductEntity, Long> {

    fun findAllByArticleIn(articles: Collection<String>): List<ProductEntity>

    fun findByArticle(article: String): ProductEntity?
}