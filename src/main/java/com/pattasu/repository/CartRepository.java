package com.pattasu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pattasu.entity.Cart;
import com.pattasu.entity.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser(User user);
    
    List<Cart> findByUserId(Long userId);

    Optional<Cart> findByUserAndProductId(User user, Long productId);

    void deleteByUserAndProductId(User user, Long productId);
}
