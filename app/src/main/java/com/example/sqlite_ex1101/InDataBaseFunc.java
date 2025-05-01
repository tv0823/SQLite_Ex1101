package com.example.sqlite_ex1101;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Tal Weintraub <tv0823@bs.amalnet.k12.il>
 * @version	1
 * @since 6/4/2025
 * Provides functionality to check if a specific value exists in a database table.
 */
public class InDataBaseFunc {

    /**
     * Checks if a given parameter exists in a specified database table.
     *
     * @param dataBaseName The dataBaseName.
     * @param param The param.
     * @param paramType The paramType.
     * @param hlp The hlp.
     * @return {@code true} if the parameter exists in the database, {@code false} otherwise.
     */
    public static boolean inDataBase(String dataBaseName, String param, String paramType, HelperDB hlp) {
        int crsrLen = 0;

        SQLiteDatabase db=hlp.getReadableDatabase();
        Cursor crsr = db.query(dataBaseName, new String[]{paramType}, paramType+"=?", new String[]{param}, paramType+"=?", null, null, null);

        crsr.moveToFirst();
        crsrLen = crsr.getCount();
        crsr.close();
        db.close();

        // if there is no elements returned - the param not in the db!
        return crsrLen != 0;
    }
}