package myself.words

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import myself.words.ui.theme.WordsTheme // 如果你的主题名是 Theme，请把这里和下方都改为 Theme

// 配色常量
val AppBgColor = Color(0xFFF5F6FA)
val CardBgColor = Color.White
val TextMainColor = Color(0xFF2C3E50)
val TextSubColor = Color(0xFF7F8C8D)
val ThemeBlue = Color(0xFF4A90E2)
val ThemeGreen = Color(0xFF2ECC71)
val ThemeRed = Color(0xFFE74C3C)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordsTheme {
                val navController = rememberNavController()
                val viewModel: WordViewModel = viewModel()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController) }
                    composable("study") { StudyScreen(navController, viewModel) }
                    composable("memorized") { MemorizedScreen(navController, viewModel) }
                    composable("all_words") { AllWordsScreen(navController, viewModel) }
                }
            }
        }
    }
}

// ---------------- 首页 ----------------
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBgColor)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 顶部标题区域
        Text(
            text = "Words",
            fontSize = 42.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ThemeBlue,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "每天进步一点点",
            fontSize = 16.sp,
            color = TextSubColor,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // 菜单按钮
        HomeMenuCard("记背功能", "继续你的学习进度", Icons.AutoMirrored.Filled.MenuBook, ThemeBlue) {
            navController.navigate("study")
        }
        Spacer(modifier = Modifier.height(16.dp))
        HomeMenuCard("已记住", "查看已掌握的单词", Icons.Default.CheckCircle, ThemeGreen) {
            navController.navigate("memorized")
        }
        Spacer(modifier = Modifier.height(16.dp))
        HomeMenuCard("全部单词", "浏览A-Z排序词典", Icons.Default.List, Color(0xFF9B59B6)) {
            navController.navigate("all_words")
        }

        // 版权信息：使用 Spacer 将文字挤压到屏幕最底部
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "图标版权：\n字母表 字母 w图标 by Pixelbazaar on Icon-Icons.com",
            fontSize = 11.sp,
            color = Color(0xFFB0BEC5), // 浅灰色
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun HomeMenuCard(title: String, subtitle: String, icon: ImageVector, iconColor: Color, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(12.dp)).background(iconColor.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextMainColor)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, fontSize = 14.sp, color = TextSubColor)
            }
        }
    }
}

// ---------------- 单词卡片 ----------------
@Composable
fun WordCard(word: Word) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(word.word, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextMainColor)
                if (word.partOfSpeech.isNotBlank()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(shape = RoundedCornerShape(6.dp), color = ThemeBlue.copy(alpha = 0.1f)) {
                        Text(word.partOfSpeech, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = ThemeBlue, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(word.chineseMeaning, fontSize = 15.sp, color = TextSubColor, lineHeight = 22.sp)
        }
    }
}

// ---------------- 记背页面 (带进度统计) ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(navController: NavController, viewModel: WordViewModel) {
    val studyWords by viewModel.studyWords.collectAsState()
    val allWords by viewModel.allWords.collectAsState()
    val memorizedWords by viewModel.memorizedWords.collectAsState()

    val totalCount = allWords.size
    val memorizedCount = memorizedWords.size
    val percentage = if (totalCount > 0) (memorizedCount * 100 / totalCount) else 0

    Scaffold(
        containerColor = AppBgColor,
        topBar = {
            TopAppBar(
                title = { Text("记背单词", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack("home", inclusive = false) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    // 右上角显示进度
                    Text(
                        text = "$memorizedCount/$totalCount ($percentage%)",
                        fontSize = 14.sp,
                        color = ThemeBlue,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardBgColor)
            )
        }
    ) { padding ->
        if (studyWords.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("太棒了！所有单词已记住", fontSize = 18.sp, color = TextSubColor)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(vertical = 12.dp)) {
                items(studyWords, key = { it.word }) { word ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.EndToStart) { viewModel.markAsMemorized(word.word); true } else false
                        }
                    )
                    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clip(RoundedCornerShape(12.dp))) {
                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                Box(modifier = Modifier.fillMaxSize().background(ThemeGreen).padding(horizontal = 24.dp), contentAlignment = Alignment.CenterEnd) {
                                    Icon(Icons.Default.Check, contentDescription = "已记住", tint = Color.White)
                                }
                            }
                        ) {
                            WordCard(word)
                        }
                    }
                }
            }
        }
    }
}

// ---------------- 已记住页面 (带进度统计) ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemorizedScreen(navController: NavController, viewModel: WordViewModel) {
    val memorizedList by viewModel.memorizedList.collectAsState()
    val allWords by viewModel.allWords.collectAsState()
    val memorizedWords by viewModel.memorizedWords.collectAsState()

    val totalCount = allWords.size
    val memorizedCount = memorizedWords.size
    val percentage = if (totalCount > 0) (memorizedCount * 100 / totalCount) else 0

    Scaffold(
        containerColor = AppBgColor,
        topBar = {
            TopAppBar(
                title = { Text("已掌握", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack("home", inclusive = false) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    // 右上角显示进度
                    Text(
                        text = "$memorizedCount/$totalCount ($percentage%)",
                        fontSize = 14.sp,
                        color = ThemeGreen,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardBgColor)
            )
        }
    ) { padding ->
        if (memorizedList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("暂无已记住的单词", fontSize = 18.sp, color = TextSubColor)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(vertical = 12.dp)) {
                items(memorizedList, key = { it.word }) { word ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.EndToStart) { viewModel.markAsForgotten(word.word); true } else false
                        }
                    )
                    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clip(RoundedCornerShape(12.dp))) {
                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                Box(modifier = Modifier.fillMaxSize().background(ThemeRed).padding(horizontal = 24.dp), contentAlignment = Alignment.CenterEnd) {
                                    Icon(Icons.Default.Delete, contentDescription = "重新记忆", tint = Color.White)
                                }
                            }
                        ) {
                            WordCard(word)
                        }
                    }
                }
            }
        }
    }
}

// ---------------- 全部单词页面 (带总数统计) ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllWordsScreen(navController: NavController, viewModel: WordViewModel) {
    val allWords by viewModel.allWords.collectAsState()
    val totalCount = allWords.size
    var searchQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val filteredWords = remember(searchQuery, allWords) {
        if (searchQuery.isBlank()) allWords
        else allWords.filter { it.word.contains(searchQuery, ignoreCase = true) || it.chineseMeaning.contains(searchQuery, ignoreCase = true) }
    }

    val letterIndicesMap = remember(filteredWords) {
        val map = mutableMapOf<Char, Int>()
        filteredWords.forEachIndexed { index, word ->
            val firstChar = word.word.firstOrNull()?.uppercaseChar() ?: '#'
            if (!map.containsKey(firstChar)) map[firstChar] = index
        }
        map
    }

    val presentLetters = remember(letterIndicesMap) { ('A'..'Z').filter { letterIndicesMap.containsKey(it) }.toList() }
    var sidebarHeight by remember { mutableStateOf(0f) }

    Scaffold(
        containerColor = AppBgColor,
        topBar = {
            TopAppBar(
                title = { Text("全部单词 ($totalCount 词)", fontWeight = FontWeight.Bold) }, // 标题显示总词数
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack("home", inclusive = false) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardBgColor)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("搜索英文单词或中文释义...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "搜索") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Clear, contentDescription = "清空") }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardBgColor, unfocusedContainerColor = CardBgColor,
                    focusedBorderColor = ThemeBlue, unfocusedBorderColor = Color.Transparent
                )
            )

            if (filteredWords.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("未找到相关单词", fontSize = 16.sp, color = TextSubColor) }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize().padding(end = 24.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(filteredWords, key = { it.word }) { word -> WordCard(word) }
                    }

                    if (presentLetters.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .width(24.dp)
                                .fillMaxHeight(0.8f)
                                .padding(end = 4.dp)
                                .onGloballyPositioned { coordinates -> sidebarHeight = coordinates.size.height.toFloat() }
                                .pointerInput(filteredWords) {
                                    detectVerticalDragGestures { change, _ ->
                                        val y = change.position.y
                                        val itemHeight = sidebarHeight / presentLetters.size
                                        val index = (y / itemHeight).toInt().coerceIn(0, presentLetters.size - 1)
                                        val letter = presentLetters[index]
                                        val targetIndex = letterIndicesMap[letter]
                                        if (targetIndex != null) coroutineScope.launch { listState.scrollToItem(targetIndex) }
                                    }
                                },
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            presentLetters.forEach { letter ->
                                Text(
                                    text = letter.toString(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ThemeBlue,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .clickable {
                                            letterIndicesMap[letter]?.let { index -> coroutineScope.launch { listState.scrollToItem(index) } }
                                        },
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}