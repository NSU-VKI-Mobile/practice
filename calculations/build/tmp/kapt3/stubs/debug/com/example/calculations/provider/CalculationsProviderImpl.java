package com.example.calculations.provider;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u001c\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\n2\u0006\u0010\r\u001a\u00020\bH\u0016J\u0010\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\fH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/example/calculations/provider/CalculationsProviderImpl;", "Lcom/example/domain/interfaces/CalculationsProvider;", "repository", "Lcom/example/calculations/data/repository/DepositRepository;", "(Lcom/example/calculations/data/repository/DepositRepository;)V", "deleteCalculation", "", "calculationId", "", "getCalculationsForUser", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/domain/model/DepositCalculation;", "userId", "saveCalculation", "calculation", "calculations_debug"})
public final class CalculationsProviderImpl implements com.example.domain.interfaces.CalculationsProvider {
    @org.jetbrains.annotations.NotNull()
    private final com.example.calculations.data.repository.DepositRepository repository = null;
    
    public CalculationsProviderImpl(@org.jetbrains.annotations.NotNull()
    com.example.calculations.data.repository.DepositRepository repository) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.example.domain.model.DepositCalculation>> getCalculationsForUser(long userId) {
        return null;
    }
    
    @java.lang.Override()
    public void saveCalculation(@org.jetbrains.annotations.NotNull()
    com.example.domain.model.DepositCalculation calculation) {
    }
    
    @java.lang.Override()
    public void deleteCalculation(long calculationId) {
    }
}