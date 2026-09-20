package myself.words

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("word_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _allWords = MutableStateFlow<List<Word>>(emptyList())
    val allWords: StateFlow<List<Word>> = _allWords.asStateFlow()

    private val _memorizedWords = MutableStateFlow<Set<String>>(emptySet())
    val memorizedWords: StateFlow<Set<String>> = _memorizedWords.asStateFlow()

    // 记背页面：从未记住的单词中，按JSON原顺序取前10个
    val studyWords: StateFlow<List<Word>> = combine(_allWords, _memorizedWords) { all, memorized ->
        all.filter { it.word !in memorized }.take(10)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 已记住页面
    val memorizedList: StateFlow<List<Word>> = combine(_allWords, _memorizedWords) { all, memorized ->
        all.filter { it.word in memorized }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // 1. 读取本地记忆状态
            val savedSet = prefs.getStringSet("memorized_set", emptySet()) ?: emptySet()
            _memorizedWords.value = savedSet

            // 2. 读取 assets 里的 json
            val jsonString = withContext(Dispatchers.IO) {
                try {
                    getApplication<Application>().assets.open("result.json")
                        .bufferedReader().use { it.readText() }
                } catch (e: Exception) {
                    e.printStackTrace()
                    "[]"
                }
            }

            // 3. 解析并按照英文词典顺序（忽略大小写）排序
            val type = object : TypeToken<List<Word>>() {}.type
            val words: List<Word> = gson.fromJson(jsonString, type) ?: emptyList()
            _allWords.value = words.sortedBy { it.word.lowercase() }
        }
    }

    // 标记为已记住 (左滑触发)
    fun markAsMemorized(word: String) {
        val newSet = _memorizedWords.value.toMutableSet().apply { add(word) }
        _memorizedWords.value = newSet
        saveToPrefs(newSet)
    }

    // 标记为重新记忆 (左滑触发)
    fun markAsForgotten(word: String) {
        val newSet = _memorizedWords.value.toMutableSet().apply { remove(word) }
        _memorizedWords.value = newSet
        saveToPrefs(newSet)
    }

    private fun saveToPrefs(set: Set<String>) {
        prefs.edit().putStringSet("memorized_set", set).apply()
    }
}