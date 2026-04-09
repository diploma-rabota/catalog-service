package ru.alexandr.catalotservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alexandr.catalotservice.entity.CategoryEntity

interface CategoryRepository : JpaRepository<CategoryEntity, Long>