package ru.alexandr.catalotservice.dto.search

import java.math.BigDecimal

data class CatalogSearchResponse(
    val limit: Int,
    val offset: Int,
    val total: Long,
    val items: List<ProductCardResponse>
)

data class ProductCardResponse(
    val id: Long,
    val article: String,
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val wholesalePrice: BigDecimal,
    val minWholesaleQuantity: Int,
    val stockQuantity: Int,
    val imageUrl: String?,
    val rating: BigDecimal,
    val weeklyGrowthPercent: Int,
    val categoryId: Long,
    val categoryName: String,
    val supplierId: Long,
    val supplierName: String
)