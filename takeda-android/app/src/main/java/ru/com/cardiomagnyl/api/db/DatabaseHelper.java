package ru.com.cardiomagnyl.api.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;

import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.region.Region;
import ru.com.cardiomagnyl.model.region.RegionDao;
import ru.com.cardiomagnyl.model.role.Role;
import ru.com.cardiomagnyl.model.role.RoleDao;
import ru.com.cardiomagnyl.model.role.UserRole;
import ru.com.cardiomagnyl.model.role.UserRoleDao;
import ru.com.cardiomagnyl.model.test.TestPage;
import ru.com.cardiomagnyl.model.test.PageDao;
import ru.com.cardiomagnyl.model.test.TestResultHolder;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.token.TokenDao;
import ru.com.cardiomagnyl.model.user.User;
import ru.com.cardiomagnyl.model.user.UserDao;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    //имя файла базы данных который будет храниться в /data/data/APPNAME/DATABASE_NAME
    private static final String DATABASE_NAME = "cardiomagnyl.sqlite";

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
            TableUtils.createTable(connectionSource, Region.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Role.class);
            TableUtils.createTable(connectionSource, UserRole.class);
            TableUtils.createTable(connectionSource, Token.class);
            TableUtils.createTable(connectionSource, TestResultHolder.class);
            TableUtils.createTable(connectionSource, TestPage.class);
            TableUtils.createTable(connectionSource, Pill.class);
        } catch (/*SQLException*/Exception e) {
            Log.e(CardiomagnylApplication.getInstance().getTag(), "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            //Так делают ленивые, гораздо предпочтительнее не удаляя БД аккуратно вносить изменения
            TableUtils.dropTable(connectionSource, Region.class, true);
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Role.class, true);
            TableUtils.dropTable(connectionSource, UserRole.class, true);
            TableUtils.dropTable(connectionSource, Token.class, true);
            TableUtils.dropTable(connectionSource, TestResultHolder.class, true);
            TableUtils.dropTable(connectionSource, TestPage.class, true);
            TableUtils.dropTable(connectionSource, Pill.class, true);

            onCreate(db, connectionSource);
        } catch (/*SQLException*/Exception e) {
            Log.e(CardiomagnylApplication.getInstance().getTag(), "error upgrading db " + DATABASE_NAME + "from ver " + oldVer);
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
    private RegionDao mRegionDao;
    private UserDao mUserDao;
    private RoleDao mRoleDao;
    private UserRoleDao mUserRoleDao;
    private TokenDao mTokenDao;
    private PageDao mMpageDao;

    public RegionDao getRegionDao() {
        try {
            if (mRegionDao == null) {
                mRegionDao = new RegionDao(getConnectionSource(), Region.class);
            }
            return mRegionDao;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create DAO", e);
        }
    }

    public UserDao getUserDao() {
        try {
            if (mUserDao == null) {
                mUserDao = new UserDao(getConnectionSource(), User.class);
            }
            return mUserDao;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create DAO", e);
        }
    }

    public RoleDao getRoleDao() {
        try {
            if (mRoleDao == null) {
                mRoleDao = new RoleDao(getConnectionSource(), Role.class);
            }
            return mRoleDao;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create DAO", e);
        }
    }

    public UserRoleDao getUserRoleDao() {
        try {
            if (mUserRoleDao == null) {
                mUserRoleDao = new UserRoleDao(getConnectionSource(), UserRole.class);
            }
            return mUserRoleDao;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create DAO", e);
        }
    }

    public TokenDao getTokenDao() {
        try {
            if (mTokenDao == null) {
                mTokenDao = new TokenDao(getConnectionSource(), Token.class);
            }
            return mTokenDao;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create DAO", e);
        }
    }

    public PageDao getPageDao() {
        try {
            if (mMpageDao == null) {
                mMpageDao = new PageDao(getConnectionSource(), TestPage.class);
            }
            return mMpageDao;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create DAO", e);
        }
    }

}
