package com.microservices.firstjobapp.Review.impl;

import com.microservices.firstjobapp.Review.Review;
import com.microservices.firstjobapp.Review.ReviewRepository;
import com.microservices.firstjobapp.Review.ReviewService;
import com.microservices.firstjobapp.company.Company;
import com.microservices.firstjobapp.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final CompanyService companyService;

    public ReviewServiceImpl(CompanyService companyService, ReviewRepository reviewRepository) {
        this.companyService = companyService;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public boolean addReview(Long companyId, Review review) {
        Company company = companyService.getCompanyById(companyId);
        if (company != null) {
            review.setCompany(company);
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public Review getReview(Long companyId, Long reviewId) {
        List<Review> reviews = reviewRepository.findByCompanyId(companyId);
        return reviews.stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean updateReview(Long companyId, Long reviewId, Review updatedReview) {
        if(companyService.getCompanyById(companyId) != null) {
            updatedReview.setCompany(companyService.getCompanyById(companyId));
            updatedReview.setId(reviewId);
            reviewRepository.save(updatedReview);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean deleteReview(Long companyId, Long reviewId) {
        if (companyService.getCompanyById(companyId) != null && reviewRepository.existsById(reviewId)) {
           Review review = reviewRepository.findById(reviewId).orElse(null);
           Company company = review.getCompany();
           company.getReviews().remove(review);
//           review.setCompany(null);
           companyService.updateCompany(company,companyId);
           reviewRepository.deleteById(reviewId);
           return true;
        }
        return false;
    }
}
