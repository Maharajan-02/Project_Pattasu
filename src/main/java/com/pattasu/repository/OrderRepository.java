package com.pattasu.repository;

import com.pattasu.entity.Order;
import com.pattasu.entity.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    
    @EntityGraph(attributePaths = { "items", "items.product"} )
    Optional<Order> findById(Long id);
}
