package com.example.calculations.data.dbo;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import com.example.calculations.data.dba.DepositDao;
import com.example.calculations.data.dba.DepositDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile DepositDao _depositDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(1, "25c0ce91d2b650ee37dea3d42e88a5a3", "14e9294106cdbaa6a7ed58391a7f7b2f") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `deposits` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `initialAmount` REAL NOT NULL, `periodMonths` INTEGER NOT NULL, `interestRate` REAL NOT NULL, `monthlyTopUp` REAL, `finalAmount` REAL NOT NULL, `interestEarned` REAL NOT NULL, `calculationDate` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '25c0ce91d2b650ee37dea3d42e88a5a3')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `deposits`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsDeposits = new HashMap<String, TableInfo.Column>(9);
        _columnsDeposits.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeposits.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeposits.put("initialAmount", new TableInfo.Column("initialAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeposits.put("periodMonths", new TableInfo.Column("periodMonths", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeposits.put("interestRate", new TableInfo.Column("interestRate", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeposits.put("monthlyTopUp", new TableInfo.Column("monthlyTopUp", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeposits.put("finalAmount", new TableInfo.Column("finalAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeposits.put("interestEarned", new TableInfo.Column("interestEarned", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeposits.put("calculationDate", new TableInfo.Column("calculationDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysDeposits = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesDeposits = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDeposits = new TableInfo("deposits", _columnsDeposits, _foreignKeysDeposits, _indicesDeposits);
        final TableInfo _existingDeposits = TableInfo.read(connection, "deposits");
        if (!_infoDeposits.equals(_existingDeposits)) {
          return new RoomOpenDelegate.ValidationResult(false, "deposits(com.example.calculations.data.dba.Deposit).\n"
                  + " Expected:\n" + _infoDeposits + "\n"
                  + " Found:\n" + _existingDeposits);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "deposits");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "deposits");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(DepositDao.class, DepositDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public DepositDao depositDao() {
    if (_depositDao != null) {
      return _depositDao;
    } else {
      synchronized(this) {
        if(_depositDao == null) {
          _depositDao = new DepositDao_Impl(this);
        }
        return _depositDao;
      }
    }
  }
}
