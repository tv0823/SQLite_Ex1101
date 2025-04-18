package com.example.sqlite_ex1101;

/**
 * The type Credits activity.
 *
 * @author      Tal Weintraub <tv0823@bs.amalnet.k12.il>
 * @version	    1
 * @since		6/4/2025
 * The type Order database constants.
 * This class holds constants related to the structure of the "Orders" database table.
 */
public class Order {
    /**
     * The name of the "Orders" table in the database.
     */
    public static final String TABLE_ORDER = "Orders";
    /**
     * The column name for the unique identifier of an order.
     */
    public static final String ORDER_ID = "Order_id";
    /**
     * The column name for the date the order was placed.
     */
    public static final String DATE = "Date";
    /**
     * The column name for the hour the order was placed.
     */
    public static final String HOUR = "Hour";
    /**
     * The column name for the ID of the worker who handled the order.
     */
    public static final String WORKER_ID = "Worker_id";
    /**
     * The column name for the ID of the meal associated with the order.
     */
    public static final String MEAL_ID = "Meal_id";
    /**
     * The column name for the company associated with the order (if any).
     */
    public static final String COMPANY = "Company";
}