package com.ohboywerecamping.webapp.campground;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.ohboywerecamping.campground.CampgroundRepository;
import com.ohboywerecamping.domain.Campground;
import com.ohboywerecamping.webapp.util.AwsUtils;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import static com.ohboywerecamping.webapp.util.DynamoUtils.bool;
import static com.ohboywerecamping.webapp.util.DynamoUtils.s;

public class DynamoCampgroundRepository implements CampgroundRepository {
    private final String tableName = "CAMPGROUND_" + AwsUtils.environmentName();
    private final DynamoDbClient ddb;

    public DynamoCampgroundRepository(final DynamoDbClient ddb) {
        this.ddb = ddb;
    }

    @Override
    public Optional<Campground> findById(final String id) {
        final QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .projectionExpression("ID, ACTIVE, HOSTNAME, NAME, DESCRIPTION")
                .consistentRead(false)
                .expressionAttributeValues(Map.of(":campgroundId", s(id)))
                .keyConditionExpression("ID = :campgroundId")
                .build();
        final QueryResponse response = ddb.query(request);
        return response.items().stream().map(item -> fromAttrMap(item)).findFirst();
    }

    private static Campground fromAttrMap(final Map<String, AttributeValue> item) {
        Campground campground = new Campground();
        campground.setId(item.get("ID").s());
        campground.setActive(item.get("ACTIVE").bool());
        campground.setHostname(item.get("HOSTNAME").s());
        campground.setName(item.get("NAME").s());
        campground.setDescription(item.get("DESCRIPTION").s());
        return campground;
    }

    @Override
    public Optional<Campground> findByHostname(final String hostname) {
        return findIdByHostname(hostname).flatMap(this::findById);
    }

    private Optional<String> findIdByHostname(final String hostname) {
        final QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName("CAMPGROUNDS_BY_HOSTNAME")
                .projectionExpression("ID")
                .consistentRead(false)
                .expressionAttributeValues(Map.of(":hostname", s(hostname)))
                .keyConditionExpression("HOSTNAME = :hostname")
                .build();
        final QueryResponse response = ddb.query(request);
        return response.items().stream().map(item -> item.get("ID").s()).findFirst();
    }

    @Override
    public List<Campground> findAll() {
        throw new UnsupportedOperationException("why do you need all the campgrounds?");
    }

    @Override
    public String save(final Campground campground) {
        if (campground.getId() == null) {
            campground.setId(UUID.randomUUID().toString());
            create(campground);
        } else {
            update(campground);
        }
        return campground.getId();
    }

    private void create(final Campground campground) {
        final PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(toAttrMap(campground))
                .build();
        ddb.putItem(request);
    }

    private static Map<String, AttributeValue> toAttrMap(final Campground campground) {
        return Map.of(
                "ID", s(campground.getId()),
                "ACTIVE", bool(campground.isActive()),
                "HOSTNAME", s(campground.getHostname()),
                "NAME", s(campground.getName()),
                "DESCRIPTION", s(campground.getDescription()));
    }

    private void update(final Campground campground) {
        final var attrMap = toAttrMap(campground);
        final UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("ID", s(campground.getId())))
                .expressionAttributeValues(Map.of(
                        ":active", attrMap.get("ACTIVE"),
                        ":hostname", attrMap.get("HOSTNAME"),
                        ":name", attrMap.get("NAME"),
                        ":description", attrMap.get("DESCRIPTION")))
                .updateExpression("set ACTIVE = :active, HOSTNAME = :hostname, NAME = :name, DESCRIPTION = :description")
                .build();
        ddb.updateItem(request);
    }

    @Override
    public void delete(final Campground campground) {
        final DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(Map.of(
                        "ID", s(campground.getId())))
                .build();
        ddb.deleteItem(request);
    }
}
