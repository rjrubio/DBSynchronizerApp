package com.example.dbprototypeapp;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 *
 *
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // Fields

    public static final String DB_NAME = "student_manager.db";
    private static final int DB_VERSION = 1;

    // Public methods

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
        try {

            // Create Table with given table name with columnName
            TableUtils.createTable(cs, College.class);
            TableUtils.createTable(cs, DBMetaData.class);
            TableUtils.createTable(cs, Enrollment.class);
            TableUtils.createTable(cs, Program.class);
            TableUtils.createTable(cs, Person.class);
            TableUtils.createTable(cs, Subject.class);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, College.class, true);
            TableUtils.dropTable(connectionSource, DBMetaData.class, true);
            TableUtils.dropTable(connectionSource, Enrollment.class, true);
            TableUtils.dropTable(connectionSource, Program.class, true);
            TableUtils.dropTable(connectionSource, Person.class, true);
            TableUtils.dropTable(connectionSource, Subject.class, true);


            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
    }

    public <T> List getAll(Class clazz) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryForAll();
    }

    public <T> T getById(Class clazz, Object aId) throws SQLException {
        Dao<T, Object> dao = getDao(clazz);
        return dao.queryForId(aId);
    }

    public <T> Dao.CreateOrUpdateStatus createOrUpdate(T obj) throws SQLException {
        Dao<T, ?> dao = (Dao<T, ?>) getDao(obj.getClass());
        return dao.createOrUpdate(obj);
    }

    public <T> int deleteById(Class clazz, T aId) throws SQLException {
        Dao<T, Object> dao = getDao(clazz);
        return dao.delete(aId);
    }


    public String stringToSha256(String stringData) {
        String sha = "";
        try {
            MessageDigest md = MessageDigest.getInstance( "SHA-256" );
            String text = "Text to hash, cryptographically.";
            // Change this to UTF-16 if needed
            md.update( text.getBytes( StandardCharsets.UTF_8 ) );
            byte[] digest = md.digest();
            sha = String.format( "%064x", new BigInteger( 1, digest ) );

        }catch (NoSuchAlgorithmException ex){
            Log.e("Hashing error ",ex.toString());
        }
    return sha;

    }

    public <T> GenericRawResults<String[]> queryStatement(Class clazz, String query) {

        try {
            Dao<T, Object> dao = getDao(clazz);
            return dao.queryRaw(query);
        }catch(SQLException ex){
            Log.e("DB error ", ex.toString());
        }
        return null;
    }

    public String getCurrentTimeStamp(){
        Long tsLong = System.currentTimeMillis()/1000;
        return tsLong.toString();
    }
}