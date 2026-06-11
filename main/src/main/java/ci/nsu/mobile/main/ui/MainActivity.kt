class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(applicationContext)
        setContent {
            AppTheme { NavGraph() }
        }
    }
}