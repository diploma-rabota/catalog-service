package ru.alexandr.catalotservice.controller


import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.alexandr.catalotservice.dto.CatalogProductDto
import ru.alexandr.catalotservice.dto.CatalogProductsByArticlesRequest
import ru.alexandr.catalotservice.service.ProductService

@RestController
@RequestMapping("/internal/products")
class InternalProductController(
    private val productService: ProductService
) {

    @PostMapping("/by-articles")
    fun getProductsByArticles(
        @RequestBody request: CatalogProductsByArticlesRequest
    ): List<CatalogProductDto> {
        return productService.getProductsByArticles(request.articles)
    }
}