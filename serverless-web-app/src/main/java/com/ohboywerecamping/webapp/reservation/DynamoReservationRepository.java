package com.ohboywerecamping.webapp.reservation;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ohboywerecamping.domain.Campsite;
import com.ohboywerecamping.domain.Order;
import com.ohboywerecamping.domain.Reservation;
import com.ohboywerecamping.reservation.ReservationRepository;
import com.ohboywerecamping.webapp.util.AwsUtils;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import static com.ohboywerecamping.webapp.util.DynamoUtils.l;
import static com.ohboywerecamping.webapp.util.DynamoUtils.s;
import static java.util.stream.Collectors.toList;

public class DynamoReservationRepository implements ReservationRepository {
    private final DynamoDbClient ddb = DynamoDbClient.builder().region(AwsUtils.region()).build();

    private final String tableName = "RESERVATION_" + AwsUtils.environmentName();

    @Override
    public List<Reservation> findByCampsiteBetweenDates(final String campsiteId, final LocalDate start, final LocalDate end) {
        final QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .projectionExpression("ORDER_ID, CAMPSITE_ID, RESERVATION_DATE")
                .consistentRead(false)
                .expressionAttributeValues(Map.of(
                        ":campsiteId", s(campsiteId),
                        ":start", s(start),
                        ":end", s(end)))
                .keyConditionExpression("CAMPSITE_ID = :campsiteId and (RESERVATION_DATE between :start and :end)")
                .build();
        final QueryResponse response = ddb.query(request);
        return response.items().stream().map(item -> fromAttrMap(item)).collect(toList());
    }

    private static Reservation fromAttrMap(final Map<String, AttributeValue> item) {
        Reservation res = new Reservation();
        res.setCampsite(new Campsite());
        res.getCampsite().setId(item.get("CAMPSITE_ID").s());
        res.setOrder(new Order());
        res.getOrder().setId(item.get("ORDER_ID").s());
        res.setDate(LocalDate.parse(item.get("RESERVATION_DATE").s()));
        return res;
    }

    @Override
    public List<Reservation> findByCampsitesBetweenDates(final Collection<String> campsiteIds, final LocalDate start, final LocalDate end) {
        final QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .projectionExpression("ORDER_ID, CAMPSITE_ID, RESERVATION_DATE")
                .consistentRead(false)
                .expressionAttributeValues(Map.of(
                        ":campsiteIds", l(campsiteIds),
                        ":start", s(start),
                        ":end", s(end)))
                .keyConditionExpression("CAMPSITE_ID IN :campsiteIds and (RESERVATION_DATE between :start and :end)")
                .build();
        final QueryResponse response = ddb.query(request);
        return response.items().stream().map(item -> fromAttrMap(item)).collect(toList());
    }

    @Override
    public List<Reservation> findByOrder(final String orderId) {
        final QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName("reservation-by-order-id")
                .projectionExpression("ORDER_ID, CAMPSITE_ID, RESERVATION_DATE")
                .consistentRead(false)
                .expressionAttributeValues(Map.of(":orderId", s(orderId)))
                .keyConditionExpression("ORDER_ID = :orderId")
                .build();
        final QueryResponse response = ddb.query(request);
        return response.items().stream().map(item -> fromAttrMap(item)).collect(toList());
    }

    @Override
    public void store(final Reservation reservation) {
        final PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(toAttrMap(reservation))
                .build();
        ddb.putItem(request);
    }

    private static Map<String, AttributeValue> toAttrMap(final Reservation reservation) {
        return Map.of(
                "CAMPSITE_ID", s(reservation.getCampsite().getId()),
                "RESERVATION_DATE", s(reservation.getDate()),
                "ORDER_ID", s(reservation.getOrder().getId()));
    }

    @Override
    public void delete(final Reservation reservation) {
        final DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(Map.of(
                        "CAMPSITE_ID", s(reservation.getCampsite().getId()),
                        "RESERVATION_DATE", s(reservation.getDate())))
                .build();
        ddb.deleteItem(request);
    }
}
