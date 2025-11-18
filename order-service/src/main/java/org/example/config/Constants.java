package org.example.config;

public class Constants {

    public static final String BASE_URL_PAYMENT = "http://localhost:8081";
    public static final String BASE_URL_ORDERS = "http://localhost:8080";
    public static final String ORDER_SERVICE = "order-service";
    public static final String CREATE_TABLE_QUERY_FILE = "order_service_create_table.sql";
    public static final String EXISTS_TABLE_QUERY_FILE = "order_service_existsQuery.sql";
    public static final String ORDER_TABLE_NAME = "orders";

    //
    public static final String ORDER_EVENTS_TOPIC = "order-events";
    public static final String PAYMENT_EVENTS_TOPIC = "payment-events";
    public static final String ORDER_EVENTS_RETRY_TOPIC = "order-events-retry";
    public static final String ORDER_EVENTS_DLQ_TOPIC = "order-events-dlq";

    //
    public static final String PAYMENT_GROUP = "payment-group";
    public static final String ORDER_GROUP = "order-group";
}
