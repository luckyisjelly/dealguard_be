package com.dealguard.product;

import com.dealguard.global.NotFoundException;
import com.dealguard.product.dto.ProductPostRequest;
import com.dealguard.product.dto.ProductPostResponse;
import com.dealguard.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductPostService {

    private final ProductPostRepository productPostRepository;

    public ProductPostService(ProductPostRepository productPostRepository) {
        this.productPostRepository = productPostRepository;
    }

    @Transactional
    public ProductPostResponse create(User user, ProductPostRequest request) {
        ProductPost post = new ProductPost(
                user,
                request.title(),
                request.category(),
                request.description(),
                request.listedPrice(),
                request.conditionDescription(),
                request.defectDescription(),
                request.refundPolicyText(),
                request.tradeLocationText(),
                Boolean.TRUE.equals(request.deliveryAvailable()));
        return ProductPostResponse.from(productPostRepository.save(post));
    }

    @Transactional(readOnly = true)
    public ProductPost getEntity(Long id) {
        return productPostRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("product post not found"));
    }

    @Transactional(readOnly = true)
    public ProductPostResponse get(Long id) {
        return ProductPostResponse.from(getEntity(id));
    }

    @Transactional
    public ProductPostResponse update(Long id, ProductPostRequest request) {
        ProductPost post = getEntity(id);
        post.update(
                request.title(),
                request.category(),
                request.description(),
                request.listedPrice(),
                request.conditionDescription(),
                request.defectDescription(),
                request.refundPolicyText(),
                request.tradeLocationText(),
                request.deliveryAvailable());
        return ProductPostResponse.from(post);
    }

    @Transactional
    public void delete(Long id) {
        productPostRepository.delete(getEntity(id));
    }
}
