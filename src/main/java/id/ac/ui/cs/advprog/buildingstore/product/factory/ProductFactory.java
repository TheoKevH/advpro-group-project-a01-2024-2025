package id.ac.ui.cs.advprog.buildingstore.product.factory;

import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductDTO;

public class ProductFactory {

    public static Product fromDTO(ProductDTO dto) {
        if (dto == null) throw new IllegalArgumentException("ProductDTO must not be null");
        return Product.builder()
                .productId(dto.getProductId())
                .productName(dto.getProductName())
                .productDescription(dto.getProductDescription())
                .productQuantity(dto.getProductQuantity())
                .productPrice(dto.getProductPrice())
                .build();
    }

    public static ProductDTO toDTO(Product product) {
        if (product == null) return null;
        return new ProductDTO(
                product.getProductId(),
                product.getProductName(),
                product.getProductPrice(),
                product.getProductQuantity(),
                product.getProductDescription()
        );
    }
}