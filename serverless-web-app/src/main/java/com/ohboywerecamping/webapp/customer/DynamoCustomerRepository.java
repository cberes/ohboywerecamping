package com.ohboywerecamping.webapp.customer;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.ohboywerecamping.customer.CustomerRepository;
import com.ohboywerecamping.domain.Customer;
import com.ohboywerecamping.webapp.util.AwsUtils;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import static com.ohboywerecamping.webapp.util.DynamoUtils.bool;
import static com.ohboywerecamping.webapp.util.DynamoUtils.s;

public class DynamoCustomerRepository implements CustomerRepository {
    private final String tableName = "CUSTOMER_" + AwsUtils.environmentName();
    private final DynamoDbClient ddb;

    public DynamoCustomerRepository(final DynamoDbClient ddb) {
        this.ddb = ddb;
    }

    @Override
    public Optional<Customer> findById(final String id) {
        final QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .projectionExpression("ID, EMAIL, ACTIVE, JOINED")
                .consistentRead(false)
                .expressionAttributeValues(Map.of(":customerId", s(id)))
                .keyConditionExpression("ID = :customerId")
                .build();
        final QueryResponse response = ddb.query(request);
        return response.items().stream().map(item -> fromAttrMap(item)).findFirst();
    }

    private static Customer fromAttrMap(final Map<String, AttributeValue> item) {
        Customer customer = new Customer();
        customer.setId(item.get("ID").s());
        customer.setEmail(item.get("EMAIL").s());
        customer.setJoined(ZonedDateTime.parse(item.get("JOINED").s()));
        customer.setActive(item.get("ACTIVE").bool());
        return customer;
    }

    @Override
    public Optional<Customer> findByEmail(final String email) {
        final QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName("customer-email")
                .projectionExpression("ID, EMAIL, ACTIVE, JOINED")
                .consistentRead(false)
                .expressionAttributeValues(Map.of(":email", s(email)))
                .keyConditionExpression("EMAIL = :email")
                .build();
        final QueryResponse response = ddb.query(request);
        return response.items().stream().map(item -> fromAttrMap(item)).findFirst();
    }

    @Override
    public List<Customer> findAll() {
        throw new UnsupportedOperationException("why do you need all the customers?");
    }

    @Override
    public String save(final Customer customer) {
        if (customer.getId() == null) {
            customer.setId(UUID.randomUUID().toString());
        }

        final PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(toAttrMap(customer))
                .build();
        ddb.putItem(request);
        return customer.getId();
    }

    private static Map<String, AttributeValue> toAttrMap(final Customer customer) {
        return Map.of(
                "ID", s(customer.getId()),
                "EMAIL", s(customer.getEmail()),
                "JOINED", s(customer.getJoined()),
                "ACTIVE", bool(customer.isActive()));
    }

    @Override
    public void delete(final Customer customer) {
        final DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(Map.of(
                        "ID", s(customer.getId())))
                .build();
        ddb.deleteItem(request);

    }
}
