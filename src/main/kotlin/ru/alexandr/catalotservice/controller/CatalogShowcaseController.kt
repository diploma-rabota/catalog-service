package ru.alexandr.catalotservice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.alexandr.catalotservice.dto.home.CatalogHomeResponse
import ru.alexandr.catalotservice.dto.search.CatalogSearchRequest
import ru.alexandr.catalotservice.dto.search.CatalogSearchResponse
import ru.alexandr.catalotservice.service.CatalogShowcaseService

@RestController
@RequestMapping("/api/catalog")
class CatalogShowcaseController(
    private val catalogShowcaseService: CatalogShowcaseService
) {

    @GetMapping("/home")
    fun getHome(): CatalogHomeResponse {
        return catalogShowcaseService.getHome()
    }

    @PostMapping("/search")
    fun search(@RequestBody request: CatalogSearchRequest): CatalogSearchResponse {
        return catalogShowcaseService.search(request)
    }
}