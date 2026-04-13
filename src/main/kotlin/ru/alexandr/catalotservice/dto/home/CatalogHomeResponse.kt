package ru.alexandr.catalotservice.dto.home

import ru.alexandr.catalotservice.dto.search.ProductCardResponse
import java.math.BigDecimal


data class CatalogHomeResponse(
    val categories: List<CategoryShortResponse>,
    val popularProducts: List<ProductCardResponse>,
    val topSuppliers: List<SupplierCardResponse>
)

data class CategoryShortResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val productCount: Long
)

data class SupplierCardResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val rating: BigDecimal,
    val imageUrl: String?,
    val badge: String?,
    val salesCount: Int,
    val revenueAmount: BigDecimal,
    val growthPercent: Int
)