package com.example.sqlite_ex1101;

/**
 * The type Credits activity.
 *
 * @author      Tal Weintraub <tv0823@bs.amalnet.k12.il>
 * @version	    1
 * @since		6/4/2025
 * The type Company database constants.
 * This class holds constants related to the structure of the "Companies" database table.
 */
public class Company {
    /**
     * The name of the "Companies" table in the database.
     */
    public static final String TABLE_COMPANY = "Companies";
    /**
     * The column name for the unique identifier of a company.
     */
    public static final String COMPANY_ID = "Company_id";
    /**
     * The column name for the name of the company.
     */
    public static final String COMPANY_NAME = "Company_name";
    /**
     * The column name for the main phone number of the company.
     */
    public static final String MAIN_PHONE = "Main_phone";
    /**
     * The column name for the secondary phone number of the company (if any).
     */
    public static final String SECOND_PHONE = "Second_phone";
}