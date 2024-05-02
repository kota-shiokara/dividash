package jp.ikanoshiokara.dividash.util

// 秒数を引数で受け取り、mm:ss形式の文字列に変換して返す
fun Int.formatTimer(): String {
    val minutes = this / 60
    val remainingSeconds = this % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}