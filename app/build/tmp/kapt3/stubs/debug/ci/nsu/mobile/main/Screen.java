package ci.nsu.mobile.main;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b7\u0018\u00002\u00020\u0001:\u0004\u0007\b\t\nB\u000f\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u0082\u0001\u0004\u000b\f\r\u000e\u00a8\u0006\u000f"}, d2 = {"Lci/nsu/mobile/main/Screen;", "", "route", "", "(Ljava/lang/String;)V", "getRoute", "()Ljava/lang/String;", "LogIn", "Main", "Qrcode", "Registry", "Lci/nsu/mobile/main/Screen$LogIn;", "Lci/nsu/mobile/main/Screen$Main;", "Lci/nsu/mobile/main/Screen$Qrcode;", "Lci/nsu/mobile/main/Screen$Registry;", "app_debug"})
public abstract class Screen {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String route = null;
    
    private Screen(java.lang.String route) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRoute() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lci/nsu/mobile/main/Screen$LogIn;", "Lci/nsu/mobile/main/Screen;", "()V", "app_debug"})
    public static final class LogIn extends ci.nsu.mobile.main.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final ci.nsu.mobile.main.Screen.LogIn INSTANCE = null;
        
        private LogIn() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lci/nsu/mobile/main/Screen$Main;", "Lci/nsu/mobile/main/Screen;", "()V", "app_debug"})
    public static final class Main extends ci.nsu.mobile.main.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final ci.nsu.mobile.main.Screen.Main INSTANCE = null;
        
        private Main() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lci/nsu/mobile/main/Screen$Qrcode;", "Lci/nsu/mobile/main/Screen;", "()V", "app_debug"})
    public static final class Qrcode extends ci.nsu.mobile.main.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final ci.nsu.mobile.main.Screen.Qrcode INSTANCE = null;
        
        private Qrcode() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lci/nsu/mobile/main/Screen$Registry;", "Lci/nsu/mobile/main/Screen;", "()V", "app_debug"})
    public static final class Registry extends ci.nsu.mobile.main.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final ci.nsu.mobile.main.Screen.Registry INSTANCE = null;
        
        private Registry() {
        }
    }
}