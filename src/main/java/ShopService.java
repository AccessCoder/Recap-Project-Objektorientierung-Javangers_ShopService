import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();

    public Order addOrder(List<String> productIds) throws ProductNotFoundException {
        List<Product> products = new ArrayList<>();

        for (String productId : productIds) {
            Product productToOrder = productRepo.getProductById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product with Id: "+ productId +" not found"));

            products.add(productToOrder);
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING, Instant.now());

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getOrderByStatus(OrderStatus status) {
        return orderRepo.getOrders().stream()
                .filter(order -> order.status().equals(status))
                .toList();
    }

    public OrderStatus updateStatus(String id, OrderStatus status) {
        Order order = orderRepo.getOrderById(id)
                .withStatus(status);
        orderRepo.removeOrder(id);
        orderRepo.addOrder(order);
        return order.status();
    }
}
