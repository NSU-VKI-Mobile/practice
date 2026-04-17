package ci.nsu.mobile.auth.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import ci.nsu.mobile.auth.data.dto.PersonDto
import ci.nsu.mobile.auth.data.dto.RegisterRequest
import ci.nsu.mobile.auth.utils.QRManager
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(viewModel.isUserLoggedIn) {
        if (viewModel.isUserLoggedIn) {
            navController.navigate("main_flow") { popUpTo("login") { inclusive = true } }
        }
    }
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.errorMessage = null
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Вход в систему") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Логин") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Пароль") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.isLoading) CircularProgressIndicator()
            else {
                Button(onClick = { viewModel.login(login, password) }, modifier = Modifier.fillMaxWidth()) { Text("Войти") }
                OutlinedButton(onClick = { navController.navigate("qr_scanner") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    Text("Авторизация через QR-код")
                }
                TextButton(onClick = { navController.navigate("register") }) { Text("Нет аккаунта? Зарегистрироваться") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var firstName by remember { mutableStateOf("") }; var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }; var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }; var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }; var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedGroupId by remember { mutableStateOf<Int?>(null) }
    var selectedGroupName by remember { mutableStateOf("Выберите группу") }

    LaunchedEffect(Unit) { viewModel.loadGroups() }
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show(); viewModel.errorMessage = null }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Регистрация") }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(scrollState)) {
            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = middleName, onValueChange = { middleName = it }, label = { Text("Отчество") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = birthDate, onValueChange = { birthDate = it }, label = { Text("Дата рождения (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Пол (MALE/FEMALE)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(value = selectedGroupName, onValueChange = {}, readOnly = true, label = { Text("Группа") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    viewModel.groupsList.forEach { group ->
                        DropdownMenuItem(text = { Text(group.name) }, onClick = { selectedGroupId = group.id; selectedGroupName = group.name; expanded = false })
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Логин") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Пароль") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Телефон") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            else {
                Button(onClick = {
                    if (selectedGroupId == null || login.isBlank() || password.isBlank() || firstName.isBlank()) {
                        Toast.makeText(context, "Заполните основные поля", Toast.LENGTH_SHORT).show(); return@Button
                    }
                    val request = RegisterRequest(login, password, email, phone, roleId = 1, authAllowed = true, person = PersonDto(firstName, lastName, middleName.ifBlank { null }, birthDate, gender, selectedGroupId!!))
                    viewModel.register(request) { Toast.makeText(context, "Успешно!", Toast.LENGTH_SHORT).show(); navController.popBackStack() }
                }, modifier = Modifier.fillMaxWidth()) { Text("Зарегистрироваться") }
            }
        }
    }
}

@Composable
fun UsersScreen(viewModel: AuthViewModel) {
    val context = LocalContext.current
    var showQRDialog by remember { mutableStateOf(false) }
    var generatedQR by remember { mutableStateOf<Bitmap?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            generatedQR?.let {
                val success = QRManager.saveImageToGallery(context, it)
                Toast.makeText(context, if(success) "Сохранено в галерею" else "Ошибка сохранения", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) { viewModel.loadUsers() }

    Box(modifier = Modifier.fillMaxSize()) {
        if (viewModel.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        else {
            Column(modifier = Modifier.fillMaxSize()) {
                if (viewModel.sessionLogin.isNotBlank()) {
                    Button(
                        onClick = {
                            val data = "${viewModel.sessionLogin}:${viewModel.sessionPassword}"
                            generatedQR = QRManager.generateQRCode(data)
                            showQRDialog = true
                        },
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) { Text("Создать QR-код авторизации") }
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(viewModel.usersList) { user ->
                        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Логин: ${user.login}", style = MaterialTheme.typography.titleMedium)
                                Text("Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
                                user.phoneNumber?.let { Text("Телефон: $it", style = MaterialTheme.typography.bodyMedium) }
                            }
                        }
                    }
                }
            }
        }

        if (showQRDialog && generatedQR != null) {
            AlertDialog(
                onDismissRequest = { showQRDialog = false },
                title = { Text("Ваш QR-код") },
                text = {
                    Image(bitmap = generatedQR!!.asImageBitmap(), contentDescription = "QR Code", modifier = Modifier.fillMaxWidth().aspectRatio(1f))
                },
                confirmButton = {
                    Button(onClick = {
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        } else {
                            val success = QRManager.saveImageToGallery(context, generatedQR!!)
                            Toast.makeText(context, if(success) "Сохранено" else "Ошибка", Toast.LENGTH_SHORT).show()
                        }
                        showQRDialog = false
                    }) { Text("Сохранить в галерею") }
                },
                dismissButton = {
                    TextButton(onClick = { showQRDialog = false }) { Text("Закрыть") }
                }
            )
        }
    }
}

@Composable
fun QRScannerScreen(
    onCodeScanned: (String) -> Unit,
    onTimeout: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasCameraPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) onBack()
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (hasCameraPermission) {
        var timeLeft by remember { mutableIntStateOf(30) }
        var isScanned by remember { mutableStateOf(false) }

        LaunchedEffect(timeLeft) {
            if (timeLeft > 0 && !isScanned) {
                delay(1000)
                timeLeft--
            } else if (timeLeft == 0 && !isScanned) {
                onTimeout()
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val imageAnalysis = ImageAnalysis.Builder()
                            .setTargetResolution(Size(1280, 720))
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()

                        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                            if (isScanned) {
                                imageProxy.close()
                                return@setAnalyzer
                            }
                            val buffer = imageProxy.planes[0].buffer
                            val data = ByteArray(buffer.remaining())
                            buffer.get(data)
                            val source = PlanarYUVLuminanceSource(
                                data, imageProxy.width, imageProxy.height,
                                0, 0, imageProxy.width, imageProxy.height, false
                            )
                            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
                            try {
                                val result = MultiFormatReader().decode(binaryBitmap)
                                isScanned = true
                                onCodeScanned(result.text)
                            } catch (e: Exception) {
                                // QR не найден в текущем кадре
                            } finally {
                                imageProxy.close()
                            }
                        }

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) { e.printStackTrace() }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            // Рамка сканера (затемнение фона + прозрачное окно в центре)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val rectSize = canvasWidth * 0.7f
                val rectLeft = (canvasWidth - rectSize) / 2
                val rectTop = (canvasHeight - rectSize) / 2

                drawRect(Color.Black.copy(alpha = 0.5f))
                drawRect(
                    Color.Transparent,
                    topLeft = Offset(rectLeft, rectTop),
                    size = androidx.compose.ui.geometry.Size(rectSize, rectSize),
                    blendMode = BlendMode.Clear
                )
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(top = 48.dp, bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Осталось: $timeLeft с", color = Color.White, style = MaterialTheme.typography.headlineMedium)
                Text("Наведите камеру на QR-код авторизации", color = Color.White, modifier = Modifier.padding(16.dp))
                Button(onClick = onBack) { Text("Отмена") }
            }
        }
    }
}