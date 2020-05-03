package com.ohboywerecamping.webapp.campsite;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.ohboywerecamping.campsite.CampsiteRepository;
import com.ohboywerecamping.domain.Campsite;
import com.ohboywerecamping.webapp.util.AwsUtils;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import static com.ohboywerecamping.webapp.util.DynamoUtils.bool;
import static com.ohboywerecamping.webapp.util.DynamoUtils.s;
import static java.util.stream.Collectors.toList;

public class DynamoCampsiteRepository implements CampsiteRepository {
    private final String tableName = "CAMPSITE_" + AwsUtils.environmentName();
    private final DynamoDbClient ddb;

    public DynamoCampsiteRepository(final DynamoDbClient ddb) {
        this.ddb = ddb;
    }

    @Override
    public List<Campsite> findByCampgroundId(final String campgroundId) {
        final QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName("CAMPSITES_BY_CAMPGROUND_ID")
                .projectionExpression("ID, ACTIVE, NAME, DESCRIPTION")
                .consistentRead(false)
                .expressionAttributeValues(Map.of(":campgroundId", s(campgroundId)))
                .keyConditionExpression("CAMPGROUND_ID = :campgroundId")
                .build();
        final QueryResponse response = ddb.query(request);
        return response.items().stream().map(item -> fromAttrMap(item)).collect(toList());
    }

    private static Campsite fromAttrMap(final Map<String, AttributeValue> item) {
        Campsite campsite = new Campsite();
        campsite.setId(item.get("ID").s());
        campsite.setActive(item.get("ACTIVE").bool());
        campsite.setName(item.get("NAME").s());
        campsite.setDescription(item.get("DESCRIPTION").s());
        return campsite;
    }

    @Override
    public List<Campsite> findByAreaId(final String areaId) {
        final QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName("CAMPSITES_BY_AREA_ID")
                .projectionExpression("ID, ACTIVE, NAME, DESCRIPTION")
                .consistentRead(false)
                .expressionAttributeValues(Map.of(":areaId", s(areaId)))
                .keyConditionExpression("AREA_ID = :areaId")
                .build();
        final QueryResponse response = ddb.query(request);
        return response.items().stream().map(item -> fromAttrMap(item)).collect(toList());
    }

    @Override
    public Optional<Campsite> findById(final String id) {
        final QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .projectionExpression("ID, ACTIVE, NAME, DESCRIPTION")
                .consistentRead(false)
                .expressionAttributeValues(Map.of(":campsiteId", s(id)))
                .keyConditionExpression("ID = :campsiteId")
                .build();
        final QueryResponse response = ddb.query(request);
        return response.items().stream().map(item -> fromAttrMap(item)).findFirst();
    }

    @Override
    public List<Campsite> findAll() {
        throw new UnsupportedOperationException("why do you need all the campsites?");
    }

    @Override
    public String save(final Campsite campsite) {
        if (campsite.getId() == null) {
            campsite.setId(UUID.randomUUID().toString());
            create(campsite);
        } else {
            update(campsite);
        }
        return campsite.getId();
    }

    private void create(final Campsite campsite) {
        final PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(toAttrMap(campsite))
                .build();
        ddb.putItem(request);
    }

    private static Map<String, AttributeValue> toAttrMap(final Campsite campsite) {
        return Map.of(
                "ID", s(campsite.getId()),
                "ACTIVE", bool(campsite.isActive()),
                "NAME", s(campsite.getName()),
                "DESCRIPTION", s(campsite.getDescription()));
    }

    private void update(final Campsite campsite) {
        final var attrMap = toAttrMap(campsite);
        final UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("ID", s(campsite.getId())))
                .expressionAttributeValues(Map.of(
                        ":active", attrMap.get("ACTIVE"),
                        ":name", attrMap.get("NAME"),
                        ":description", attrMap.get("DESCRIPTION")))
                .updateExpression("set ACTIVE = :active, NAME = :name, DESCRIPTION = :description")
                .build();
        ddb.updateItem(request);
    }

    @Override
    public void delete(final Campsite campsite) {
        final DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(Map.of(
                        "ID", s(campsite.getId())))
                .build();
        ddb.deleteItem(request);
    }
}
