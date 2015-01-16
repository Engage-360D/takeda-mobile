package ru.com.cardiomagnil.ca_api.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;

import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.ca_model.token.Ca_Token;
import ru.com.cardiomagnil.ca_model.region.Ca_Region;
import ru.com.cardiomagnil.ca_model.base.BaseModel;
import ru.com.cardiomagnil.ca_model.region.Ca_RegionDao;
import ru.com.cardiomagnil.ca_model.role.Ca_Role;
import ru.com.cardiomagnil.ca_model.role.Ca_RoleDao;
import ru.com.cardiomagnil.ca_model.role.Ca_UserRoleDao;
import ru.com.cardiomagnil.ca_model.role.Ca_UserRole;
import ru.com.cardiomagnil.ca_model.token.Ca_TokenDao;
import ru.com.cardiomagnil.ca_model.user.Ca_User;
import ru.com.cardiomagnil.ca_model.user.Ca_UserDao;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    //имя файла базы данных который будет храниться в /data/data/APPNAME/DATABASE_NAME
    private static final String DATABASE_NAME = "cardiomagnil.sqlite";

    //с каждым увеличением версии, при нахождении в устройстве БД с предыдущей версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 1;

    //ссылки на DAO соответсвующие сущностям, хранимым в БД
    //используется для сущностей, которые не имеют своего DAO-класса
    private HashMap<Class, RuntimeExceptionDao<BaseModel, Integer>> allRuntimeExceptionDao = new HashMap<Class, RuntimeExceptionDao<BaseModel, Integer>>();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Ca_Region.class);
            TableUtils.createTable(connectionSource, Ca_User.class);
            TableUtils.createTable(connectionSource, Ca_Role.class);
            TableUtils.createTable(connectionSource, Ca_UserRole.class);
            TableUtils.createTable(connectionSource, Ca_Token.class);
        } catch (/*SQLException*/Exception e) {
            Log.e(CardiomagnilApplication.getInstance().getTag(), "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            //Так делают ленивые, гораздо предпочтительнее не удаляя БД аккуратно вносить изменения
            TableUtils.dropTable(connectionSource, Ca_Region.class, true);
            TableUtils.dropTable(connectionSource, Ca_User.class, true);
            TableUtils.dropTable(connectionSource, Ca_Role.class, true);
            TableUtils.dropTable(connectionSource, Ca_UserRole.class, true);
            TableUtils.dropTable(connectionSource, Ca_Token.class, true);

            onCreate(db, connectionSource);
        } catch (/*SQLException*/Exception e) {
            Log.e(CardiomagnilApplication.getInstance().getTag(), "error upgrading db " + DATABASE_NAME + "from ver " + oldVer);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public <T> RuntimeExceptionDao<T, Integer> getRuntimeDataDao(Class<T> clazz) {
        RuntimeExceptionDao runtimeDao = allRuntimeExceptionDao.get(clazz);

        if (runtimeDao == null) {
            runtimeDao = getRuntimeExceptionDao(clazz);
            allRuntimeExceptionDao.put(clazz, runtimeDao);
        }
        return runtimeDao;
    }

    /**
     * Close any open connections.
     */
    @Override
    public void close() {
        super.close();
        allRuntimeExceptionDao.clear();
    }

    // =================================================================================================
    // DAO-singletons
    // =================================================================================================
    private Ca_RegionDao mRegionDao;
    private Ca_UserDao mUserDao;
    private Ca_RoleDao mRoleDao;
    private Ca_UserRoleDao mUserRoleDao;
    private Ca_TokenDao mTokenDao;

    public Ca_RegionDao getRegionDao() {
        try {
            if (mRegionDao == null) {
                mRegionDao = new Ca_RegionDao(getConnectionSource(), Ca_Region.class);
            }
            return mRegionDao;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create DAO", e);
        }
    }

    public Ca_UserDao getUserDao() {
        try {
            if (mUserDao == null) {
                mUserDao = new Ca_UserDao(getConnectionSource(), Ca_User.class);
            }
            return mUserDao;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create DAO", e);
        }
    }

    public Ca_RoleDao getRoleDao() {
        try {
            if (mRoleDao == null) {
                mRoleDao = new Ca_RoleDao(getConnectionSource(), Ca_Role.class);
            }
            return mRoleDao;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create DAO", e);
        }
    }

    public Ca_UserRoleDao getUserRoleDao() {
        try {
            if (mUserRoleDao == null) {
                mUserRoleDao = new Ca_UserRoleDao(getConnectionSource(), Ca_UserRole.class);
            }
            return mUserRoleDao;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create DAO", e);
        }
    }

    public Ca_TokenDao getTokenDao() {
        try {
            if (mTokenDao == null) {
                mTokenDao = new Ca_TokenDao(getConnectionSource(), Ca_Token.class);
            }
            return mTokenDao;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create DAO", e);
        }
    }

}
