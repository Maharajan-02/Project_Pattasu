package com.pattasu.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pattasu.dto.GetOrderListDTO;
import com.pattasu.dto.OrderRequestDTO;
import com.pattasu.dto.OrderResponseDTO;
import com.pattasu.dto.UpdateOrderDTO;
import com.pattasu.entity.Order;
import com.pattasu.entity.User;
import com.pattasu.service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequestDTO orderRequestDto, 
    	    @AuthenticationPrincipal User user){
    	try {
        	Order order = orderService.placeOrder(orderRequestDto.getAddress(), user);
            return ResponseEntity.ok(order);
        }catch(Exception e) {
        	log.info("error during order placement {}", e.getMessage());
        	return ResponseEntity
        			.status(HttpStatus.BAD_REQUEST)
        			.body(new Order());
        }
    }
    
    @GetMapping("/{orderId}/invoice")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long orderId) {
        byte[] pdfBytes = orderService.generateInvoicePdf(orderId); // your PDF generator logic

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + orderId + ".pdf")
            .body(pdfBytes);
    }

    @GetMapping
    public ResponseEntity<List<GetOrderListDTO>> getUserOrders(@AuthenticationPrincipal User user) {
        List<GetOrderListDTO> orders = orderService.getUserOrders(user);
        return ResponseEntity.ok(orders);
    }
    
    @PreAuthorize("hasAuthority('admin')")
    @PutMapping("/update")
    public ResponseEntity<String> updateOrders(@RequestBody UpdateOrderDTO updateOrder) {
        return orderService.updateOrder(updateOrder);
    }
    
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/allOrders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrder(){
    	
    	return orderService.getAllOrderWithUserInfo();
    	
    }
    
}
