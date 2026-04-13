package ru.alexandr.catalotservice.dto.search

import java.math.BigDecimal

data class CatalogSearchRequest(
    val search: String? = null,
    val categoryIds: List<Long>? = null,
    val supplierIds: List<Long>? = null,
    val minPrice: BigDecimal? = null,
    val maxPrice: BigDecimal? = null,
    val minRating: BigDecimal? = null,
    val onlyActive: Boolean? = true,
    val onlyInStock: Boolean? = false,
    val minWeeklyGrowthPercent: Int? = null,
    val sortBy: String? = "POPULAR",
    val limit: Int? = 20,
    val offset: Int? = 0,
)