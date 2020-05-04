package com.ohboywerecamping.webapp.campsite;

import java.util.*;

import com.ohboywerecamping.campsite.CampsiteRepository;
import com.ohboywerecamping.domain.*;
import com.ohboywerecamping.webapp.util.AwsUtils;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import static com.ohboywerecamping.webapp.util.DynamoUtils.bool;
import static com.ohboywerecamping.webapp.util.DynamoUtils.n;
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
        // TODO this index needs to have more fields
        final QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName("CAMPSITES_BY_CAMPGROUND_ID")
                .projectionExpression("ID, AREA_ID, CAMPGROUND_ID, ACTIVE, #name, DESCRIPTION, " +
                        "#type, ACCESS, #size, MAX_OCCUPANCY, MAX_VEHICLES, PETS_ALLOWED, ELECTRIC, WATER, SEWER")
                .consistentRead(false)
                .expressionAttributeNames(Map.of(
                        "#name", "NAME",
                        "#type", "TYPE",
                        "#size", "SIZE"))
                .expressionAttributeValues(Map.of(":campgroundId", s(campgroundId)))
                .keyConditionExpression("CAMPGROUND_ID = :campgroundId")
                .build();
        final QueryResponse response = ddb.query(request);
        return response.items().stream().map(item -> fromAttrMap(item)).collect(toList());
    }

    private static Campsite fromAttrMap(final Map<String, AttributeValue> item) {
        Campsite campsite = new Campsite();
        campsite.setId(item.get("ID").s());
        campsite.setArea(new Area());
        campsite.getArea().setId(item.get("AREA_ID").s());
        campsite.setCampground(new Campground());
        campsite.getCampground().setId(item.get("CAMPGROUND_ID").s());
        campsite.setActive(item.get("ACTIVE").bool());
        campsite.setName(item.get("NAME").s());
        campsite.setDescription(item.get("DESCRIPTION").s());
        Optional.ofNullable(item.get("NOTES")).map(it -> it.s()).ifPresent(campsite::setNotes);
        campsite.setType(SiteType.valueOf(item.get("TYPE").s()));
        campsite.setAccess(Access.valueOf(item.get("ACCESS").s()));
        campsite.setSize(Integer.parseInt(item.get("SIZE").n()));
        campsite.setMaxOccupancy(Integer.parseInt(item.get("MAX_OCCUPANCY").n()));
        campsite.setMaxVehicles(Integer.parseInt(item.get("MAX_VEHICLES").n()));
        campsite.setPetsAllowed(Integer.parseInt(item.get("PETS_ALLOWED").n()));
        campsite.setElectric(Electric.valueOf(item.get("ELECTRIC").s()));
        campsite.setWater(Water.valueOf(item.get("WATER").s()));
        campsite.setSewer(Sewer.valueOf(item.get("SEWER").s()));
        return campsite;
    }

    @Override
    public List<Campsite> findByAreaId(final String areaId) {
        final QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName("CAMPSITES_BY_AREA_ID")
                .projectionExpression("ID, AREA_ID, CAMPGROUND_ID, ACTIVE, #name, DESCRIPTION, " +
                        "#type, ACCESS, #size, MAX_OCCUPANCY, MAX_VEHICLES, PETS_ALLOWED, ELECTRIC, WATER, SEWER")
                .consistentRead(false)
                .expressionAttributeNames(Map.of(
                        "#name", "NAME",
                        "#type", "TYPE",
                        "#size", "SIZE"))
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
                .projectionExpression("ID, AREA_ID, CAMPGROUND_ID, ACTIVE, #name, DESCRIPTION, NOTES, " +
                        "#type, ACCESS, #size, MAX_OCCUPANCY, MAX_VEHICLES, PETS_ALLOWED, ELECTRIC, WATER, SEWER")
                .consistentRead(false)
                .expressionAttributeNames(Map.of(
                        "#name", "NAME",
                        "#type", "TYPE",
                        "#size", "SIZE"))
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
                .expressionAttributeNames(Map.of(
                        "#name", "NAME",
                        "#type", "TYPE",
                        "#size", "SIZE"))
                .build();
        ddb.putItem(request);
    }

    private static Map<String, AttributeValue> toAttrMap(final Campsite campsite) {
        final Map<String, AttributeValue> attrs = new LinkedHashMap<>();
        attrs.put("ID", s(campsite.getId()));
        attrs.put("CAMPGROUND_ID", s(campsite.getCampground().getId()));
        attrs.put("AREA_ID", s(campsite.getArea().getId()));
        attrs.put("ACTIVE", bool(campsite.isActive()));
        attrs.put("#name", s(campsite.getName()));
        attrs.put("DESCRIPTION", s(campsite.getDescription()));
        attrs.put("NOTES", s(campsite.getNotes()));
        attrs.put("#type", s(campsite.getType()));
        attrs.put("ACCESS", s(campsite.getAccess()));
        attrs.put("#size", n(campsite.getSize()));
        attrs.put("MAX_OCCUPANCY", n(campsite.getMaxOccupancy()));
        attrs.put("MAX_VEHICLES", n(campsite.getMaxVehicles()));
        attrs.put("PETS_ALLOWED", n(campsite.getPetsAllowed()));
        attrs.put("ELECTRIC", s(campsite.getElectric()));
        attrs.put("WATER", s(campsite.getWater()));
        attrs.put("SEWER", s(campsite.getSewer()));
        return attrs;
    }

    private void update(final Campsite campsite) {
        final UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("ID", s(campsite.getId())))
                .expressionAttributeNames(Map.of(
                        "#name", "NAME",
                        "#type", "TYPE",
                        "#size", "SIZE"))
                .expressionAttributeValues(updateAttrMap(campsite))
                .updateExpression("set CAMPGROUND_ID = :campgroundId, " +
                        "AREA_ID = :areaId, " +
                        "ACTIVE = :active, " +
                        "#name = :name, " +
                        "DESCRIPTION = :description, " +
                        "NOTES = :notes, " +
                        "#type = :type, " +
                        "ACCESS = :access, " +
                        "#size = :size, " +
                        "MAX_OCCUPANCY = :maxOccupancy, " +
                        "MAX_VEHICLES = :maxVehicles, " +
                        "PETS_ALLOWED = :petsAllowed, " +
                        "ELECTRIC = :electric, " +
                        "WATER = :water, " +
                        "SEWER = :sewer")
                .build();
        ddb.updateItem(request);
    }

    private static Map<String, AttributeValue> updateAttrMap(final Campsite campsite) {
        final Map<String, AttributeValue> create = toAttrMap(campsite);
        final Map<String, AttributeValue> attrs = new LinkedHashMap<>();
        attrs.put(":campgroundId", create.get("CAMPGROUND_ID"));
        attrs.put(":areaId", create.get("AREA_ID"));
        attrs.put(":active", create.get("ACTIVE"));
        attrs.put(":name", create.get("#name"));
        attrs.put(":description", create.get("DESCRIPTION"));
        attrs.put(":notes", create.get("NOTES"));
        attrs.put(":type", create.get("#type"));
        attrs.put(":access", create.get("ACCESS"));
        attrs.put(":size", create.get("#size"));
        attrs.put(":maxOccupancy", create.get("MAX_OCCUPANCY"));
        attrs.put(":maxVehicles", create.get("MAX_VEHICLES"));
        attrs.put(":petsAllowed", create.get("PETS_ALLOWED"));
        attrs.put(":electric", create.get("ELECTRIC"));
        attrs.put(":water", create.get("WATER"));
        attrs.put(":sewer", create.get("SEWER"));
        return attrs;
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
