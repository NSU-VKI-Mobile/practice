package ci.nsu.mobile.main.di;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001b\u0010\t\u001a\u00020\n8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000f\u001a\u00020\u00108FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0013\u0010\u000e\u001a\u0004\b\u0011\u0010\u0012R\u001b\u0010\u0014\u001a\u00020\u00158FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0018\u0010\u000e\u001a\u0004\b\u0016\u0010\u0017R\u001b\u0010\u0019\u001a\u00020\u001a8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001d\u0010\u000e\u001a\u0004\b\u001b\u0010\u001c\u00a8\u0006\u001f"}, d2 = {"Lci/nsu/mobile/main/di/ServiceLocator;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "appContext", "Landroid/app/Application;", "getAppContext", "()Landroid/app/Application;", "authRepository", "Lcom/example/auth/data/repository/AuthRepository;", "getAuthRepository", "()Lcom/example/auth/data/repository/AuthRepository;", "authRepository$delegate", "Lkotlin/Lazy;", "database", "Lcom/example/calculations/data/dbo/AppDatabase;", "getDatabase", "()Lcom/example/calculations/data/dbo/AppDatabase;", "database$delegate", "depositRepository", "Lcom/example/calculations/data/repository/DepositRepository;", "getDepositRepository", "()Lcom/example/calculations/data/repository/DepositRepository;", "depositRepository$delegate", "viewModelFactory", "Lci/nsu/mobile/main/vm/ViewModelFactory;", "getViewModelFactory", "()Lci/nsu/mobile/main/vm/ViewModelFactory;", "viewModelFactory$delegate", "Companion", "app_debug"})
public final class ServiceLocator {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.app.Application appContext = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy database$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy authRepository$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy depositRepository$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModelFactory$delegate = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile ci.nsu.mobile.main.di.ServiceLocator INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final ci.nsu.mobile.main.di.ServiceLocator.Companion Companion = null;
    
    public ServiceLocator(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.app.Application getAppContext() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.calculations.data.dbo.AppDatabase getDatabase() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.auth.data.repository.AuthRepository getAuthRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.calculations.data.repository.DepositRepository getDepositRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final ci.nsu.mobile.main.vm.ViewModelFactory getViewModelFactory() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lci/nsu/mobile/main/di/ServiceLocator$Companion;", "", "()V", "INSTANCE", "Lci/nsu/mobile/main/di/ServiceLocator;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final ci.nsu.mobile.main.di.ServiceLocator getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}