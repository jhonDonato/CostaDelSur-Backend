package com.costadelsur.api.repo;

import com.costadelsur.api.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByActiveTrueAndEndDateBefore(LocalDateTime now);
}
