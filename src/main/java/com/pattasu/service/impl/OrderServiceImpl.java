package com.pattasu.service.impl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itextpdf.layout.properties.UnitValue;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.pattasu.dto.GetOrderListDTO;
import com.pattasu.dto.OrderItemDTO;
import com.pattasu.entity.Cart;
import com.pattasu.entity.Order;
import com.pattasu.entity.OrderItem;
import com.pattasu.entity.Product;
import com.pattasu.entity.User;
import com.pattasu.enums.OrderStatus;
import com.pattasu.exception.EmptyCartException;
import com.pattasu.exception.InventoryException;
import com.pattasu.exception.PdfHandleException;
import com.pattasu.repository.CartRepository;
import com.pattasu.repository.OrderRepository;
import com.pattasu.repository.ProductRepository;
import com.pattasu.repository.UserRepository;
import com.pattasu.service.OrderService;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    
    private static final String logoPath = "src/main/resources/static/logo.png";

    public OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository, ProductRepository productRepository
    		, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Order placeOrder(String address,  User user) {
        List<Cart> cartItems = cartRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new EmptyCartException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setAddress(address);

        user.setAddress(address);
        userRepository.save(user);
        
        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (Cart cart : cartItems) {
            Product product = cart.getProduct();
            int quantity = cart.getQuantity();
            double price = product.getPrice();

            // Check stock
            if (product.getStockQuantity() < quantity) {
                throw new InventoryException("Insufficient stock for: " + product.getName());
            }

            // Deduct stock
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);

            // Create order item
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setPrice(price);

            orderItems.add(item);
            total += price * quantity;
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);
        order.setOrderStatus(OrderStatus.PLACED);

        // Save the order and cascade items
        Order savedOrder = orderRepository.save(order);

        // Clear the cart
        cartRepository.deleteAll(cartItems);

        return savedOrder;
    }

    @Override
    @Transactional
    public List<GetOrderListDTO> getUserOrders(User user) {
        List<Order> orderList = orderRepository.findByUser(user);
        
        List<GetOrderListDTO> orderListDto = new ArrayList<>();
        for(Order order : orderList) {
        	GetOrderListDTO orderDto = new GetOrderListDTO();
        	orderDto.setOrderId(order.getId());
        	orderDto.setAddress(order.getAddress());
        	orderDto.setOrderDate(order.getOrderDate());
        	orderDto.setStatus(order.getOrderStatus());
        	List<OrderItemDTO> orderItemList = order.getItems().stream()
        		    .map(OrderItemDTO::new)  // uses the conversion constructor
        		    .collect(Collectors.toList());
        	orderDto.setOrderItemDto(orderItemList);
        	orderDto.setNumberOfItems(orderItemList.size());
        	
        	orderListDto.add(orderDto);
        }
        
        return orderListDto;
    }

    @Override
//    @Transactional
    public byte[] generateInvoicePdf(Long orderId) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            // Company Logo
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(80, 60);
            logo.setAlignment(Image.ALIGN_LEFT);
//            document.add(logo);

            PdfPTable headerTable = new PdfPTable(3);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[] {2f, 4f, 3f});
            
            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(PdfPCell.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerTable.addCell(logoCell);
            
            PdfPCell centreCell = new PdfPCell(new Phrase("Surya Pyro Park", 
            		FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            centreCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            centreCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            centreCell.setBorder(PdfPCell.NO_BORDER);
            headerTable.addCell(centreCell);
            
            PdfPCell rightcell = new PdfPCell(new Phrase("Ph. 9876543210\n support@pattasu.com", 
            		FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
            rightcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            rightcell.setBorder(PdfPCell.NO_BORDER);
            headerTable.addCell(rightcell);
            document.add(headerTable);
            
            document.add(new Paragraph(" "));
            
            
            // Company Name & Date
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Invoice Date: " + new Date()));
            document.add(new Paragraph("Order ID: " + orderId));
            document.add(Chunk.NEWLINE);

            // Table Header
            PdfPTable table = new PdfPTable(4); // 4 columns: Product, Quantity, Price, Total
            table.setWidthPercentage(100);
            table.addCell("Product");
            table.addCell("Quantity");
            table.addCell("Price");
            table.addCell("Total");

            // Fetch order & items
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            double totalAmount = 0;

            for (OrderItem item : order.getItems()) {
                table.addCell(item.getProduct().getName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(String.format("₹ %.2f", item.getProduct().getPrice()));
                double itemTotal = item.getQuantity() * item.getProduct().getPrice();
                table.addCell(String.format("₹ %.2f", itemTotal));
                totalAmount += itemTotal;
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            // Total Amount
            Paragraph total = new Paragraph("Total: ₹ " + String.format("%.2f", totalAmount),
                    new Font(Font.HELVETICA, 14, Font.BOLD));
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new PdfHandleException("Failed to generate invoice -" + e);
        }
    }
}
