package ru.alexandr.catalotservice.service

import ru.alexandr.catalotservice.dto.CatalogProductDto


interface ProductService {
    fun getProductsByArticles(articles: List<String>): List<CatalogProductDto>
}