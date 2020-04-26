package com.ohboywerecamping.webapp.order;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.ohboywerecamping.domain.Customer;
import com.ohboywerecamping.domain.Order;
import com.ohboywerecamping.order.OrderRepository;
import com.ohboywerecamping.webapp.util.AwsUtils;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import static com.ohboywerecamping.webapp.util.DynamoUtils.s;
import static java.util.stream.Collectors.toList;

public class DynamoOrderRepository implements OrderRepository {
    private final DynamoDbClient ddb = DynamoDbClient.builder().region(AwsUtils.region()).build();

    private final String tableName = "ORDER_" + AwsUtils.environmentName();

    @Override
    public List<Order> findByCustomer(final String customerId) {
        final QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName("order-customer-id")
                .projectionExpression("ID, CUSTOMER_ID")
                .consistentRead(false)
                .expressionAttributeValues(Map.of(":customerId", s(customerId)))
                .keyConditionExpression("CUSTOMER_ID = :customerId")
                .build();
        final QueryResponse response = ddb.query(request);
        return response.items().stream().map(item -> fromAttrMap(item)).collect(toList());
    }

    private static Order fromAttrMap(final Map<String, AttributeValue> item) {
        Order order = new Order();
        order.setId(item.get("ORDER_ID").s());
        order.setCustomer(new Customer());
        order.getCustomer().setId(item.get("CUSTOMER_ID").s());
        order.setCreated(ZonedDateTime.parse(item.get("CREATED").s()));
        return order;
    }

    @Override
    public List<Order> findAll() {
        throw new UnsupportedOperationException("why do you need all the orders?");
    }

    @Override
    public Optional<Order> findById(final String id) {
        final QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .projectionExpression("ID, CUSTOMER_ID")
                .consistentRead(false)
                .expressionAttributeValues(Map.of(":orderId", s(id)))
                .keyConditionExpression("ID = :orderId")
                .build();
        final QueryResponse response = ddb.query(request);
        return response.items().stream().map(item -> fromAttrMap(item)).findFirst();
    }

    @Override
    public String save(final Order order) {
        if (order.getId() == null) {
            order.setId(UUID.randomUUID().toString());
        }

        final PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(toAttrMap(order))
                .build();
        ddb.putItem(request);
        return order.getId();
    }

    private static Map<String, AttributeValue> toAttrMap(final Order order) {
        return Map.of(
                "ID", s(order.getId()),
                "CUSTOMER_ID", s(order.getCustomer().getId()),
                "CREATED", s(order.getCreated()));
    }

    @Override
    public void delete(final Order order) {
        final DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(Map.of(
                        "ID", s(order.getId())))
                .build();
        ddb.deleteItem(request);
    }
}
