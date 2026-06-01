package com.example.calculations.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0012\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BQ\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\b0\u000b\u00a2\u0006\u0002\u0010\fJ\t\u0010\u001e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010 \u001a\u00020\u0003H\u00c6\u0003J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\"\u001a\u00020\bH\u00c6\u0003J\t\u0010#\u001a\u00020\bH\u00c6\u0003J\u000f\u0010$\u001a\b\u0012\u0004\u0012\u00020\b0\u000bH\u00c6\u0003JU\u0010%\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\b0\u000bH\u00c6\u0001J\u0013\u0010&\u001a\u00020\u00162\b\u0010\'\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010(\u001a\u00020)H\u00d6\u0001J\t\u0010*\u001a\u00020\u0003H\u00d6\u0001R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\b0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0011\u0010\u0015\u001a\u00020\u00168F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0017R\u0011\u0010\u0018\u001a\u00020\u00168F\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0017R\u0011\u0010\u0019\u001a\u00020\u00168F\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0017R\u0011\u0010\u001a\u001a\u00020\u00168F\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\u0017R\u0011\u0010\u001b\u001a\u00020\u00168F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0017R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0012\u00a8\u0006+"}, d2 = {"Lcom/example/calculations/vm/DepositsUiState;", "", "initialAmount", "", "periodMonths", "interestRate", "monthlyTopUp", "finalAmount", "", "interestEarned", "availableInterestRate", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;DDLjava/util/List;)V", "getAvailableInterestRate", "()Ljava/util/List;", "getFinalAmount", "()D", "getInitialAmount", "()Ljava/lang/String;", "getInterestEarned", "getInterestRate", "isAllCorrect", "", "()Z", "isInitialAmountValid", "isInterestRateValid", "isMonthlyTopUpValid", "isPeriodMonthsValid", "getMonthlyTopUp", "getPeriodMonths", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "calculations_debug"})
public final class DepositsUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String initialAmount = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String periodMonths = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String interestRate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String monthlyTopUp = null;
    private final double finalAmount = 0.0;
    private final double interestEarned = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Double> availableInterestRate = null;
    
    public DepositsUiState(@org.jetbrains.annotations.NotNull()
    java.lang.String initialAmount, @org.jetbrains.annotations.NotNull()
    java.lang.String periodMonths, @org.jetbrains.annotations.NotNull()
    java.lang.String interestRate, @org.jetbrains.annotations.NotNull()
    java.lang.String monthlyTopUp, double finalAmount, double interestEarned, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Double> availableInterestRate) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getInitialAmount() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPeriodMonths() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getInterestRate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMonthlyTopUp() {
        return null;
    }
    
    public final double getFinalAmount() {
        return 0.0;
    }
    
    public final double getInterestEarned() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Double> getAvailableInterestRate() {
        return null;
    }
    
    public final boolean isInitialAmountValid() {
        return false;
    }
    
    public final boolean isPeriodMonthsValid() {
        return false;
    }
    
    public final boolean isInterestRateValid() {
        return false;
    }
    
    public final boolean isMonthlyTopUpValid() {
        return false;
    }
    
    public final boolean isAllCorrect() {
        return false;
    }
    
    public DepositsUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    public final double component5() {
        return 0.0;
    }
    
    public final double component6() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Double> component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.calculations.vm.DepositsUiState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String initialAmount, @org.jetbrains.annotations.NotNull()
    java.lang.String periodMonths, @org.jetbrains.annotations.NotNull()
    java.lang.String interestRate, @org.jetbrains.annotations.NotNull()
    java.lang.String monthlyTopUp, double finalAmount, double interestEarned, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Double> availableInterestRate) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}