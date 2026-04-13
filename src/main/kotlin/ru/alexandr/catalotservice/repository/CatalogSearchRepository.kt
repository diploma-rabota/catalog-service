package ru.alexandr.catalotservice.repository

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.alexandr.catalotservice.dto.search.CatalogSearchRequest
import ru.alexandr.catalotservice.dto.search.CatalogSearchResponse
import ru.alexandr.catalotservice.dto.search.ProductCardResponse
import java.math.BigDecimal

@Repository
class CatalogSearchRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    fun search(request: CatalogSearchRequest): CatalogSearchResponse {
        val limit = normalizeLimit(request.limit)
        val offset = normalizeOffset(request.offset)

        val params = MapSqlParameterSource()
        val conditions = mutableListOf<String>()

        val fromSql = """
            from product p
            join category c on c.id = p.category_id
            join supplier s on s.id = p.supplier_id
        """.trimIndent()

        if (request.onlyActive == true) {
            conditions += "p.is_active = true"
        }

        if (request.onlyInStock == true) {
            conditions += "p.stock_quantity > 0"
        }

        if (!request.search.isNullOrBlank()) {
            conditions += """
                (
                    lower(p.name) like :search
                    or lower(coalesce(p.description, '')) like :search
                    or lower(p.article) like :search
                    or lower(c.name) like :search
                    or lower(s.name) like :search
                )
            """.trimIndent()
            params.addValue("search", "%${request.search.trim().lowercase()}%")
        }

        if (!request.categoryIds.isNullOrEmpty()) {
            conditions += "p.category_id in (:categoryIds)"
            params.addValue("categoryIds", request.categoryIds)
        }

        if (!request.supplierIds.isNullOrEmpty()) {
            conditions += "p.supplier_id in (:supplierIds)"
            params.addValue("supplierIds", request.supplierIds)
        }

        if (request.minPrice != null) {
            conditions += "p.price >= :minPrice"
            params.addValue("minPrice", request.minPrice)
        }

        if (request.maxPrice != null) {
            conditions += "p.price <= :maxPrice"
            params.addValue("maxPrice", request.maxPrice)
        }

        if (request.minRating != null) {
            conditions += "p.rating >= :minRating"
            params.addValue("minRating", request.minRating)
        }

        if (request.minWeeklyGrowthPercent != null) {
            conditions += "p.weekly_growth_percent >= :minWeeklyGrowthPercent"
            params.addValue("minWeeklyGrowthPercent", request.minWeeklyGrowthPercent)
        }

        val whereSql = if (conditions.isEmpty()) {
            ""
        } else {
            "where " + conditions.joinToString("\n  and ")
        }

        val orderBySql = resolveOrderBy(request.sortBy)

        val countSql = """
            select count(*)
            $fromSql
            $whereSql
        """.trimIndent()

        val total = jdbcTemplate.queryForObject(
            countSql,
            params,
            Long::class.java
        ) ?: 0L

        params.addValue("limit", limit)
        params.addValue("offset", offset)

        val dataSql = """
            select
                p.id,
                p.article,
                p.name,
                p.description,
                p.price,
                p.wholesale_price,
                p.min_wholesale_quantity,
                p.stock_quantity,
                p.image_url,
                p.rating,
                p.weekly_growth_percent,
                c.id as category_id,
                c.name as category_name,
                s.id as supplier_id,
                s.name as supplier_name
            $fromSql
            $whereSql
            $orderBySql
            limit :limit offset :offset
        """.trimIndent()

        val items = jdbcTemplate.query(dataSql, params) { rs, _ ->
            ProductCardResponse(
                id = rs.getLong("id"),
                article = rs.getString("article"),
                name = rs.getString("name"),
                description = rs.getString("description"),
                price = rs.getBigDecimal("price"),
                wholesalePrice = rs.getBigDecimal("wholesale_price"),
                minWholesaleQuantity = rs.getInt("min_wholesale_quantity"),
                stockQuantity = rs.getInt("stock_quantity"),
                imageUrl = rs.getString("image_url"),
                rating = rs.getBigDecimal("rating") ?: BigDecimal.ZERO,
                weeklyGrowthPercent = rs.getInt("weekly_growth_percent"),
                categoryId = rs.getLong("category_id"),
                categoryName = rs.getString("category_name"),
                supplierId = rs.getLong("supplier_id"),
                supplierName = rs.getString("supplier_name")
            )
        }

        return CatalogSearchResponse(
            limit = limit,
            offset = offset,
            total = total,
            items = items
        )
    }

    private fun resolveOrderBy(sortBy: String?): String {
        return when (sortBy?.uppercase()) {
            "PRICE_ASC" -> "order by p.price asc, p.id desc"
            "PRICE_DESC" -> "order by p.price desc, p.id desc"
            "RATING_DESC" -> "order by p.rating desc, p.id desc"
            "NEWEST" -> "order by p.id desc"
            "GROWTH_DESC" -> "order by p.weekly_growth_percent desc, p.rating desc, p.id desc"
            "NAME_ASC" -> "order by p.name asc, p.id desc"
            else -> "order by p.weekly_growth_percent desc, p.rating desc, p.id desc"
        }
    }

    private fun normalizeLimit(limit: Int?): Int {
        val value = limit ?: 20
        return value.coerceIn(1, 100)
    }

    private fun normalizeOffset(offset: Int?): Int {
        val value = offset ?: 0
        return value.coerceAtLeast(0)
    }
}