package com.example.calculations.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bJ\u001a\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\u001d0\u00132\u0006\u0010\u001f\u001a\u00020\u001bJ\u000e\u0010 \u001a\u00020\u00172\u0006\u0010\u001f\u001a\u00020\u001bJ\u000e\u0010!\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020\u0019J\u000e\u0010#\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020\u0019J\u000e\u0010$\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020\u0019J\u000e\u0010%\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020\u0019R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\t0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006&"}, d2 = {"Lcom/example/calculations/vm/DepositsViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "depositRepository", "Lcom/example/calculations/data/repository/DepositRepository;", "(Landroid/app/Application;Lcom/example/calculations/data/repository/DepositRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/calculations/vm/DepositsUiState;", "allInterestRates", "", "", "", "getAllInterestRates", "()Ljava/util/Map;", "sdf", "Ljava/text/SimpleDateFormat;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "calcFinalAmountAndEarned", "", "formatTime", "", "timestamp", "", "getDepositUser", "", "Lcom/example/calculations/data/dba/Deposit;", "userId", "saveDeposit", "setInitialAmount", "newValue", "setInterestRate", "setMonthlyTopUp", "setPeriodMonths", "calculations_debug"})
public final class DepositsViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.calculations.data.repository.DepositRepository depositRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final java.text.SimpleDateFormat sdf = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.Integer, java.lang.Double> allInterestRates = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.calculations.vm.DepositsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.calculations.vm.DepositsUiState> uiState = null;
    
    public DepositsViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application, @org.jetbrains.annotations.NotNull()
    com.example.calculations.data.repository.DepositRepository depositRepository) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.Integer, java.lang.Double> getAllInterestRates() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.calculations.vm.DepositsUiState> getUiState() {
        return null;
    }
    
    public final void setInitialAmount(@org.jetbrains.annotations.NotNull()
    java.lang.String newValue) {
    }
    
    public final void setPeriodMonths(@org.jetbrains.annotations.NotNull()
    java.lang.String newValue) {
    }
    
    public final void setInterestRate(@org.jetbrains.annotations.NotNull()
    java.lang.String newValue) {
    }
    
    public final void setMonthlyTopUp(@org.jetbrains.annotations.NotNull()
    java.lang.String newValue) {
    }
    
    public final void calcFinalAmountAndEarned() {
    }
    
    public final void saveDeposit(long userId) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.calculations.data.dba.Deposit>> getDepositUser(long userId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatTime(long timestamp) {
        return null;
    }
}