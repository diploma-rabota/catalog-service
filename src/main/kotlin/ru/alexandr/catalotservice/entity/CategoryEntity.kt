package ru.alexandr.catalotservice.entity

import jakarta.persistence.*

@Entity
@Table(name = "category")
class CategoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(
        name = "category_seq",
        sequenceName = "sqn_category",
        allocationSize = 1
    )
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "description")
    var description: String? = null,
)