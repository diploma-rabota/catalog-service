package ru.alexandr.catalotservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "product")
class ProductEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(
        name = "product_seq",
        sequenceName = "sqn_product",
        allocationSize = 1
    )
    val id: Long? = null,

    @Column(name = "article", nullable = false)
    var article: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "price", nullable = false)
    var price: BigDecimal,

    @Column(name = "wholesale_price", nullable = false)
    var wholesalePrice: BigDecimal,

    @Column(name = "min_wholesale_quantity", nullable = false)
    var minWholesaleQuantity: Int,

    @Column(name = "stock_quantity", nullable = false)
    var stockQuantity: Int,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @Column(name = "category_id", nullable = false)
    var categoryId: Long,

    @Column(name = "supplier_id", nullable = false)
    var supplierId: Long,

    @Column(name = "image_url")
    var imageUrl: String? = null,

    @Column(name = "rating", nullable = false)
    var rating: BigDecimal = BigDecimal.ZERO,

    @Column(name = "weekly_growth_percent", nullable = false)
    var weeklyGrowthPercent: Int = 0,
)