package com.example.calculations.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\nJ\u000e\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u0012J\u001a\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u00142\u0006\u0010\u0015\u001a\u00020\u0012R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0016"}, d2 = {"Lcom/example/calculations/data/repository/DepositRepository;", "", "depositDao", "Lcom/example/calculations/data/dba/DepositDao;", "(Lcom/example/calculations/data/dba/DepositDao;)V", "coroutineScope", "Lkotlinx/coroutines/CoroutineScope;", "depositList", "Landroidx/lifecycle/LiveData;", "", "Lcom/example/calculations/data/dba/Deposit;", "getDepositList", "()Landroidx/lifecycle/LiveData;", "addDeposit", "", "deposit", "deleteDeposit", "depositId", "", "getDepositUser", "Lkotlinx/coroutines/flow/Flow;", "userId", "calculations_debug"})
public final class DepositRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.calculations.data.dba.DepositDao depositDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope coroutineScope = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.example.calculations.data.dba.Deposit>> depositList = null;
    
    public DepositRepository(@org.jetbrains.annotations.NotNull()
    com.example.calculations.data.dba.DepositDao depositDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.calculations.data.dba.Deposit>> getDepositList() {
        return null;
    }
    
    public final void addDeposit(@org.jetbrains.annotations.NotNull()
    com.example.calculations.data.dba.Deposit deposit) {
    }
    
    public final void deleteDeposit(long depositId) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.calculations.data.dba.Deposit>> getDepositUser(long userId) {
        return null;
    }
}