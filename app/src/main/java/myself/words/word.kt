package myself.words

import com.google.gson.annotations.SerializedName

data class Word(
    @SerializedName("word") val word: String,
    @SerializedName("part_of_speech") val partOfSpeech: String,
    @SerializedName("chinese_meaning") val chineseMeaning: String
)