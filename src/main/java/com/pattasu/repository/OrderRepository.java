package com.pattasu.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pattasu.entity.Order;
import com.pattasu.entity.User;
import com.pattasu.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUserOrderByIdDesc(User user);
    
    @EntityGraph(attributePaths = { "items", "items.product"} )
    Optional<Order> findById(Long id);
    
    @Query("SELECT o FROM Order o "
    		+ "JOIN FETCH o.user u "
    		+ "JOIN FETCH o.items i "
    		+ "JOIN FETCH i.product p "
    		+ "ORDER BY o.orderDate DESC")
    List<Order> findAllWithUserAndItems();
    
    @Query("SELECT SUM(o.totalPrice) FROM Order o")
    Double sumAllOrderTotals();
    
    long countByOrderStatusIn(Collection<OrderStatus> statuses);
    
}
