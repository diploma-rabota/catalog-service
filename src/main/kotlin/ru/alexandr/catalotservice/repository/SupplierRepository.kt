package ru.alexandr.catalotservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alexandr.catalotservice.entity.SupplierEntity

interface SupplierRepository : JpaRepository<SupplierEntity, Long>