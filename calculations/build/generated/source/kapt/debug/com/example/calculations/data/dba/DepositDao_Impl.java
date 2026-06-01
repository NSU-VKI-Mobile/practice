package com.example.calculations.data.dba;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.coroutines.FlowUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Double;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class DepositDao_Impl implements DepositDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Deposit> __insertAdapterOfDeposit;

  public DepositDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfDeposit = new EntityInsertAdapter<Deposit>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `deposits` (`id`,`userId`,`initialAmount`,`periodMonths`,`interestRate`,`monthlyTopUp`,`finalAmount`,`interestEarned`,`calculationDate`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, @NonNull final Deposit entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindDouble(3, entity.getInitialAmount());
        statement.bindLong(4, entity.getPeriodMonths());
        statement.bindDouble(5, entity.getInterestRate());
        if (entity.getMonthlyTopUp() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getMonthlyTopUp());
        }
        statement.bindDouble(7, entity.getFinalAmount());
        statement.bindDouble(8, entity.getInterestEarned());
        statement.bindLong(9, entity.getCalculationDate());
      }
    };
  }

  @Override
  public void addDeposit(final Deposit deposit) {
    if (deposit == null) throw new NullPointerException();
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfDeposit.insert(_connection, deposit);
      return null;
    });
  }

  @Override
  public LiveData<List<Deposit>> getDeposits() {
    final String _sql = "SELECT * FROM deposits";
    return __db.getInvalidationTracker().createLiveData(new String[] {"deposits"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "userId");
        final int _columnIndexOfInitialAmount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "initialAmount");
        final int _columnIndexOfPeriodMonths = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "periodMonths");
        final int _columnIndexOfInterestRate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "interestRate");
        final int _columnIndexOfMonthlyTopUp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "monthlyTopUp");
        final int _columnIndexOfFinalAmount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "finalAmount");
        final int _columnIndexOfInterestEarned = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "interestEarned");
        final int _columnIndexOfCalculationDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "calculationDate");
        final List<Deposit> _result = new ArrayList<Deposit>();
        while (_stmt.step()) {
          final Deposit _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final long _tmpUserId;
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId);
          final double _tmpInitialAmount;
          _tmpInitialAmount = _stmt.getDouble(_columnIndexOfInitialAmount);
          final int _tmpPeriodMonths;
          _tmpPeriodMonths = (int) (_stmt.getLong(_columnIndexOfPeriodMonths));
          final double _tmpInterestRate;
          _tmpInterestRate = _stmt.getDouble(_columnIndexOfInterestRate);
          final Double _tmpMonthlyTopUp;
          if (_stmt.isNull(_columnIndexOfMonthlyTopUp)) {
            _tmpMonthlyTopUp = null;
          } else {
            _tmpMonthlyTopUp = _stmt.getDouble(_columnIndexOfMonthlyTopUp);
          }
          final double _tmpFinalAmount;
          _tmpFinalAmount = _stmt.getDouble(_columnIndexOfFinalAmount);
          final double _tmpInterestEarned;
          _tmpInterestEarned = _stmt.getDouble(_columnIndexOfInterestEarned);
          final long _tmpCalculationDate;
          _tmpCalculationDate = _stmt.getLong(_columnIndexOfCalculationDate);
          _item = new Deposit(_tmpId,_tmpUserId,_tmpInitialAmount,_tmpPeriodMonths,_tmpInterestRate,_tmpMonthlyTopUp,_tmpFinalAmount,_tmpInterestEarned,_tmpCalculationDate);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Flow<List<Deposit>> getDepositsUser(final long userId) {
    final String _sql = "SELECT * FROM deposits WHERE userId == ?";
    return FlowUtil.createFlow(__db, false, new String[] {"deposits"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, userId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "userId");
        final int _columnIndexOfInitialAmount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "initialAmount");
        final int _columnIndexOfPeriodMonths = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "periodMonths");
        final int _columnIndexOfInterestRate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "interestRate");
        final int _columnIndexOfMonthlyTopUp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "monthlyTopUp");
        final int _columnIndexOfFinalAmount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "finalAmount");
        final int _columnIndexOfInterestEarned = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "interestEarned");
        final int _columnIndexOfCalculationDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "calculationDate");
        final List<Deposit> _result = new ArrayList<Deposit>();
        while (_stmt.step()) {
          final Deposit _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final long _tmpUserId;
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId);
          final double _tmpInitialAmount;
          _tmpInitialAmount = _stmt.getDouble(_columnIndexOfInitialAmount);
          final int _tmpPeriodMonths;
          _tmpPeriodMonths = (int) (_stmt.getLong(_columnIndexOfPeriodMonths));
          final double _tmpInterestRate;
          _tmpInterestRate = _stmt.getDouble(_columnIndexOfInterestRate);
          final Double _tmpMonthlyTopUp;
          if (_stmt.isNull(_columnIndexOfMonthlyTopUp)) {
            _tmpMonthlyTopUp = null;
          } else {
            _tmpMonthlyTopUp = _stmt.getDouble(_columnIndexOfMonthlyTopUp);
          }
          final double _tmpFinalAmount;
          _tmpFinalAmount = _stmt.getDouble(_columnIndexOfFinalAmount);
          final double _tmpInterestEarned;
          _tmpInterestEarned = _stmt.getDouble(_columnIndexOfInterestEarned);
          final long _tmpCalculationDate;
          _tmpCalculationDate = _stmt.getLong(_columnIndexOfCalculationDate);
          _item = new Deposit(_tmpId,_tmpUserId,_tmpInitialAmount,_tmpPeriodMonths,_tmpInterestRate,_tmpMonthlyTopUp,_tmpFinalAmount,_tmpInterestEarned,_tmpCalculationDate);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Object deleteById(final long calculationId, final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM deposits WHERE id = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, calculationId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
