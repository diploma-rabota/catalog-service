package ru.alexandr.catalotservice.repository

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.alexandr.catalotservice.dto.home.CatalogHomeResponse
import ru.alexandr.catalotservice.dto.home.CategoryShortResponse
import ru.alexandr.catalotservice.dto.home.SupplierCardResponse
import ru.alexandr.catalotservice.dto.search.ProductCardResponse
import java.math.BigDecimal

@Repository
class CatalogHomeRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    fun getHome(): CatalogHomeResponse {
        val categories = jdbcTemplate.query(
            """
            select
                c.id,
                c.name,
                c.description,
                count(p.id) as product_count
            from category c
            left join product p on p.category_id = c.id and p.is_active = true
            group by c.id, c.name, c.description
            order by c.name asc
            """.trimIndent(),
            emptyMap<String, Any>()
        ) { rs, _ ->
            CategoryShortResponse(
                id = rs.getLong("id"),
                name = rs.getString("name"),
                description = rs.getString("description"),
                productCount = rs.getLong("product_count")
            )
        }

        val popularProducts = jdbcTemplate.query(
            """
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
            from product p
            join category c on c.id = p.category_id
            join supplier s on s.id = p.supplier_id
            where p.is_active = true
            order by p.weekly_growth_percent desc, p.rating desc, p.id desc
            limit 8
            """.trimIndent(),
            emptyMap<String, Any>()
        ) { rs, _ ->
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

        val topSuppliers = jdbcTemplate.query(
            """
            select
                s.id,
                s.name,
                s.description,
                s.rating,
                s.image_url,
                s.badge,
                s.sales_count,
                s.revenue_amount,
                s.growth_percent
            from supplier s
            where s.is_active = true
            order by s.rating desc, s.sales_count desc, s.revenue_amount desc
            limit 6
            """.trimIndent(),
            emptyMap<String, Any>()
        ) { rs, _ ->
            SupplierCardResponse(
                id = rs.getLong("id"),
                name = rs.getString("name"),
                description = rs.getString("description"),
                rating = rs.getBigDecimal("rating") ?: BigDecimal.ZERO,
                imageUrl = rs.getString("image_url"),
                badge = rs.getString("badge"),
                salesCount = rs.getInt("sales_count"),
                revenueAmount = rs.getBigDecimal("revenue_amount"),
                growthPercent = rs.getInt("growth_percent")
            )
        }

        return CatalogHomeResponse(
            categories = categories,
            popularProducts = popularProducts,
            topSuppliers = topSuppliers
        )
    }
}