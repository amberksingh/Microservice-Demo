package org.example.config;

public class Constants {

    public static final String PAYMENT_SERVICE = "payment-service";

    public static final String CREATE_TABLE_QUERY_FILE = "payment_service_create_table.sql";
    public static final String EXISTS_TABLE_QUERY_FILE = "payment_service_existsQuery.sql";
    public static final String PAYMENT_TABLE_NAME = "payments";


    public static final String ORDER_EVENTS_TOPIC = "order-events";
    public static final String PAYMENT_EVENTS_TOPIC = "payment-events";
    public static final String USER_BALANCE_REQUESTS = "user-balance-requests";
    public static final String USER_BALANCE_RESPONSES = "user-balance-responses";

    public static final String ORDER_EVENTS_RETRY_TOPIC = "order-events-retry";
    public static final String ORDER_EVENTS_DLQ_TOPIC = "order-events-dlq";


    public static final String PAYMENT_GROUP = "payment-group";
    public static final String ORDER_GROUP = "order-group";
}
