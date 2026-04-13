package ru.alexandr.catalotservice.service

import org.springframework.stereotype.Service
import ru.alexandr.catalotservice.repository.CatalogSearchRepository
import ru.alexandr.catalotservice.dto.home.CatalogHomeResponse
import ru.alexandr.catalotservice.dto.search.CatalogSearchRequest
import ru.alexandr.catalotservice.dto.search.CatalogSearchResponse
import ru.alexandr.catalotservice.repository.CatalogHomeRepository

@Service
class CatalogShowcaseService(
    private val catalogHomeDaoRepository: CatalogHomeRepository,
    private val catalogSearchRepository: CatalogSearchRepository,
) {

    fun getHome(): CatalogHomeResponse {
        return catalogHomeDaoRepository.getHome()
    }

    fun search(request: CatalogSearchRequest): CatalogSearchResponse {
        return catalogSearchRepository.search(request)
    }
}