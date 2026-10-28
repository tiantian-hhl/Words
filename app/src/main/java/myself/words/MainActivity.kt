package myself.words

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
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
                    composable("home") { HomeScreen(navController, viewModel) }
                    composable("study") { StudyScreen(navController, viewModel) }
                    composable("memorized") { MemorizedScreen(navController, viewModel) }
                    composable("all_words") { AllWordsScreen(navController, viewModel) }
                    composable("cet6") { Cet6Screen(navController, viewModel) }
                    composable("cet6_quiz") { Cet6QuizScreen(navController, viewModel) }
                }
            }
        }
    }
}

// ---------------- 首页 ----------------
@Composable
fun HomeScreen(navController: NavController, viewModel: WordViewModel) {
    val allWords by viewModel.allWords.collectAsState()
    val memorizedWords by viewModel.memorizedWords.collectAsState()
    val progress = if (allWords.isEmpty()) 0f else memorizedWords.size.toFloat() / allWords.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBgColor)
            .statusBarsPadding()
            .verticalScroll(androidx.compose.foundation.rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(48.dp).clip(RoundedCornerShape(15.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF155EEF), Color(0xFF004EEB)))),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.MenuBook, null, tint = Color.White, modifier = Modifier.size(27.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Words", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = TextMainColor)
                Text("让每一次积累都看得见", fontSize = 13.sp, color = TextSubColor)
            }
        }

        Spacer(Modifier.height(22.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column(
                Modifier.background(Brush.linearGradient(listOf(Color(0xFF0B4A8B), Color(0xFF1570EF))))
                    .padding(22.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("学习总进度", color = Color(0xFFD1E9FF), fontSize = 13.sp)
                        Text("${memorizedWords.size} / ${allWords.size}", color = Color.White,
                            fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Box(Modifier.size(54.dp).clip(CircleShape).background(Color.White.copy(alpha = .14f)),
                        contentAlignment = Alignment.Center) {
                        Text("${(progress * 100).toInt()}%", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = { progress }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                    color = Color(0xFFFDB022), trackColor = Color.White.copy(alpha = .18f)
                )
                Spacer(Modifier.height(10.dp))
                Text("今天再掌握 10 个单词，离目标更近一步", color = Color(0xFFEFF8FF), fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(22.dp))
        Text("学习专区", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextMainColor)
        Spacer(Modifier.height(12.dp))
        Card(
            onClick = { navController.navigate("cet6") },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFAEB)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFEDF89))
        ) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(52.dp).clip(RoundedCornerShape(15.dp)).background(Color(0xFFFDB022)),
                    contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.School, null, tint = Color.White, modifier = Modifier.size(29.dp))
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text("CET-6 学习专区", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A2E0E))
                    Text("核心词汇 · 专项记忆 · 四选一测验", fontSize = 13.sp, color = Color(0xFF93370D))
                }
                Icon(Icons.Default.ArrowForward, null, tint = Color(0xFFB54708))
            }
        }

        Spacer(Modifier.height(22.dp))
        Text("快捷学习", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextMainColor)
        Spacer(Modifier.height(12.dp))
        HomeMenuCard("继续记背", "从待掌握词汇继续", Icons.AutoMirrored.Filled.MenuBook, ThemeBlue) {
            navController.navigate("study")
        }
        Spacer(modifier = Modifier.height(12.dp))
        HomeMenuCard("已记住", "查看已掌握的单词", Icons.Default.CheckCircle, ThemeGreen) {
            navController.navigate("memorized")
        }
        Spacer(modifier = Modifier.height(12.dp))
        HomeMenuCard("词汇词典", "搜索并浏览 A-Z 词库", Icons.Default.List, Color(0xFF7F56D9)) {
            navController.navigate("all_words")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("离线词库 · 专注学习 · 数据仅保存在本机", fontSize = 11.sp,
            color = Color(0xFF98A2B3), modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun HomeMenuCard(title: String, subtitle: String, icon: ImageVector, iconColor: Color, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(92.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEAECF0))
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

// ---------------- CET-6 专区 ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cet6Screen(navController: NavController, viewModel: WordViewModel) {
    val words by viewModel.cet6Words.collectAsState()
    val memorized by viewModel.memorizedWords.collectAsState()
    var filter by rememberSaveable { mutableIntStateOf(0) }
    val filtered = remember(words, memorized, filter) {
        when (filter) {
            1 -> words.filter { it.word !in memorized }
            2 -> words.filter { it.word in memorized }
            else -> words
        }
    }
    val mastered = words.count { it.word in memorized }
    val progress = if (words.isEmpty()) 0f else mastered.toFloat() / words.size

    Scaffold(
        containerColor = AppBgColor,
        topBar = {
            TopAppBar(
                title = { Text("CET-6 学习专区", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardBgColor)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        Modifier.background(Brush.linearGradient(listOf(Color(0xFF7A2E0E), Color(0xFFF79009))))
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(48.dp).clip(RoundedCornerShape(14.dp))
                                .background(Color.White.copy(alpha = .16f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.EmojiEvents, null, tint = Color.White)
                            }
                            Spacer(Modifier.width(13.dp))
                            Column(Modifier.weight(1f)) {
                                Text("六级核心突破", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text("精选高频与学术词汇", color = Color(0xFFFEF0C7), fontSize = 13.sp)
                            }
                            Text("$mastered/${words.size}", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(16.dp))
                        LinearProgressIndicator(
                            progress = { progress }, modifier = Modifier.fillMaxWidth().height(7.dp).clip(CircleShape),
                            color = Color(0xFFFFE7A3), trackColor = Color.White.copy(alpha = .2f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate("cet6_quiz") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF93370D)),
                            shape = RoundedCornerShape(13.dp), enabled = words.size >= 4
                        ) {
                            Icon(Icons.Default.LocalFireDepartment, null)
                            Spacer(Modifier.width(8.dp))
                            Text("开始 10 题专项测验", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("全部 ${words.size}", "待掌握 ${words.size - mastered}", "已掌握 $mastered").forEachIndexed { index, label ->
                        FilterChip(
                            selected = filter == index,
                            onClick = { filter = index },
                            label = { Text(label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFD1E9FF),
                                selectedLabelColor = Color(0xFF175CD3)
                            )
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
            }
            items(filtered, key = { it.word }) { word ->
                Cet6WordCard(word, word.word in memorized) {
                    if (word.word in memorized) viewModel.markAsForgotten(word.word)
                    else viewModel.markAsMemorized(word.word)
                }
            }
            if (filtered.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                        Text("这里暂时没有单词", color = TextSubColor)
                    }
                }
            }
        }
    }
}

@Composable
private fun Cet6WordCard(word: Word, mastered: Boolean, onToggle: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEAECF0))
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(word.word, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextMainColor)
                    Spacer(Modifier.width(8.dp))
                    Text(word.partOfSpeech, fontSize = 11.sp, color = ThemeBlue,
                        modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(Color(0xFFEFF8FF))
                            .padding(horizontal = 7.dp, vertical = 3.dp))
                }
                Spacer(Modifier.height(7.dp))
                Text(word.chineseMeaning, fontSize = 14.sp, color = TextSubColor, lineHeight = 21.sp)
            }
            IconButton(
                onClick = onToggle,
                modifier = Modifier.clip(CircleShape).background(if (mastered) Color(0xFFD1FADF) else Color(0xFFF2F4F7))
            ) {
                Icon(Icons.Default.Check, contentDescription = if (mastered) "取消掌握" else "标记掌握",
                    tint = if (mastered) Color(0xFF039855) else Color(0xFF98A2B3))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cet6QuizScreen(navController: NavController, viewModel: WordViewModel) {
    val sourceWords by viewModel.cet6Words.collectAsState()
    var round by rememberSaveable { mutableIntStateOf(0) }
    val quizWords = remember(sourceWords, round) { sourceWords.shuffled().take(10) }
    var index by rememberSaveable(round) { mutableIntStateOf(0) }
    var score by rememberSaveable(round) { mutableIntStateOf(0) }
    var selected by rememberSaveable(round, index) { mutableStateOf<String?>(null) }
    val finished = quizWords.isNotEmpty() && index >= quizWords.size

    Scaffold(
        containerColor = AppBgColor,
        topBar = {
            TopAppBar(
                title = { Text("CET-6 专项测验", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardBgColor)
            )
        }
    ) { padding ->
        if (quizWords.size < 4) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (finished) {
            Column(
                Modifier.fillMaxSize().padding(padding).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(Modifier.size(92.dp).clip(CircleShape).background(Color(0xFFFFFAEB)),
                    contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.EmojiEvents, null, tint = Color(0xFFF79009), modifier = Modifier.size(48.dp))
                }
                Spacer(Modifier.height(22.dp))
                Text("本轮完成", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = TextMainColor)
                Text("答对 $score / ${quizWords.size} 题", fontSize = 18.sp, color = TextSubColor,
                    modifier = Modifier.padding(top = 8.dp, bottom = 26.dp))
                Button(onClick = { round++; index = 0; score = 0; selected = null },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                    Icon(Icons.Default.Refresh, null)
                    Spacer(Modifier.width(8.dp))
                    Text("再练一组")
                }
                TextButton(onClick = { navController.popBackStack() }) { Text("返回 CET-6 专区") }
            }
        } else {
            val current = quizWords[index]
            val options = remember(quizWords, index) {
                (quizWords.filter { it.word != current.word }.shuffled().take(3).map { it.chineseMeaning }
                        + current.chineseMeaning).shuffled()
            }
            Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("第 ${index + 1} / ${quizWords.size} 题", color = TextSubColor, fontWeight = FontWeight.Medium)
                    Text("得分 $score", color = ThemeBlue, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { (index + 1f) / quizWords.size },
                    modifier = Modifier.fillMaxWidth().height(7.dp).clip(CircleShape)
                )
                Spacer(Modifier.height(28.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF8FF))
                ) {
                    Column(Modifier.fillMaxWidth().padding(vertical = 34.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(current.word, fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF175CD3))
                        Text(current.partOfSpeech, color = Color(0xFF528BCE), modifier = Modifier.padding(top = 6.dp))
                    }
                }
                Spacer(Modifier.height(22.dp))
                Text("选择正确的中文释义", fontWeight = FontWeight.Bold, color = TextMainColor)
                Spacer(Modifier.height(10.dp))
                options.forEachIndexed { optionIndex, option ->
                    val isCorrect = option == current.chineseMeaning
                    val isSelected = selected == option
                    val background = when {
                        selected != null && isCorrect -> Color(0xFFD1FADF)
                        isSelected -> Color(0xFFFEE4E2)
                        else -> Color.White
                    }
                    val border = when {
                        selected != null && isCorrect -> Color(0xFF12B76A)
                        isSelected -> Color(0xFFF04438)
                        else -> Color(0xFFEAECF0)
                    }
                    Surface(
                        onClick = {
                            if (selected == null) {
                                selected = option
                                if (isCorrect) score++
                            }
                        },
                        enabled = selected == null,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                        shape = RoundedCornerShape(14.dp), color = background,
                        border = androidx.compose.foundation.BorderStroke(1.dp, border)
                    ) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(28.dp).clip(CircleShape).background(border.copy(alpha = .12f)),
                                contentAlignment = Alignment.Center) {
                                Text(('A' + optionIndex).toString(), color = border, fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.width(12.dp))
                            Text(option, color = TextMainColor, modifier = Modifier.weight(1f))
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                if (selected != null) {
                    Button(onClick = { index++ }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                        Text(if (index == quizWords.lastIndex) "查看成绩" else "下一题")
                        Spacer(Modifier.width(6.dp))
                        Icon(Icons.Default.ArrowForward, null)
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
