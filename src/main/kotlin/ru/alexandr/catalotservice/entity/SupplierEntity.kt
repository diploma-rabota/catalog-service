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
@Table(name = "supplier")
class SupplierEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_seq")
    @SequenceGenerator(
        name = "supplier_seq",
        sequenceName = "sqn_supplier",
        allocationSize = 1
    )
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "rating", nullable = false)
    var rating: BigDecimal = BigDecimal.ZERO,

    @Column(name = "image_url")
    var imageUrl: String? = null,

    @Column(name = "badge")
    var badge: String? = null,

    @Column(name = "sales_count", nullable = false)
    var salesCount: Int = 0,

    @Column(name = "revenue_amount", nullable = false)
    var revenueAmount: BigDecimal = BigDecimal.ZERO,

    @Column(name = "growth_percent", nullable = false)
    var growthPercent: Int = 0,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,
)