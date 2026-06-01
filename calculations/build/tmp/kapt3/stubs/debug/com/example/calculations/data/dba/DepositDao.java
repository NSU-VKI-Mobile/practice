package com.example.calculations.data.dba;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0016\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u000bH\'J\u001c\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u000e2\u0006\u0010\u000f\u001a\u00020\bH\'\u00a8\u0006\u0010"}, d2 = {"Lcom/example/calculations/data/dba/DepositDao;", "", "addDeposit", "", "deposit", "Lcom/example/calculations/data/dba/Deposit;", "deleteById", "calculationId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDeposits", "Landroidx/lifecycle/LiveData;", "", "getDepositsUser", "Lkotlinx/coroutines/flow/Flow;", "userId", "calculations_debug"})
@androidx.room.Dao()
public abstract interface DepositDao {
    
    @androidx.room.Query(value = "SELECT * FROM deposits")
    @org.jetbrains.annotations.NotNull()
    public abstract androidx.lifecycle.LiveData<java.util.List<com.example.calculations.data.dba.Deposit>> getDeposits();
    
    @androidx.room.Query(value = "SELECT * FROM deposits WHERE userId == :userId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.calculations.data.dba.Deposit>> getDepositsUser(long userId);
    
    @androidx.room.Query(value = "DELETE FROM deposits WHERE id = :calculationId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteById(long calculationId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert()
    public abstract void addDeposit(@org.jetbrains.annotations.NotNull()
    com.example.calculations.data.dba.Deposit deposit);
}