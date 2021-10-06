package com.example.positive_alarm

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.util.*


class AlarmActivity : AppCompatActivity() {
    private var countLabel: TextView? = null
    private var questionLabel: TextView? = null
    private var answerBtn1: Button? = null
    private var answerBtn2: Button? = null
    private var answerBtn3: Button? = null
    private var answerBtn4: Button? = null
    private var prevLabel: TextView? = null
    private var prevLabel2: TextView? = null
    private var rightAnswer: String? = null
    private var rightAnswerCount = 0
    private var quizCount = 1
    var quizArray = ArrayList<ArrayList<String?>>()
    // 問題(ネガティブな言葉),正解の回答,間違いの回答1,2,3 の順で問題を格納
    var quizData = arrayOf(arrayOf("ぐずぐずしている", "着実", "凛としている", "社交的", "気が回る"),
            arrayOf("だらしない", "おおらか", "アイデアマン", "清楚", "冷静"),
            arrayOf("マイペース", "信念がある", "まじめ", "素朴", "用心深い"),
            arrayOf("無理をしている", "一生懸命", "楽天的", "清楚", "ユーモアがある"),
            arrayOf("面倒くさがり", "細かいことにこだわらない", "冷静", "自立した", "丁寧"),
            arrayOf("流されやすい", "素直", "穏やか", "熱意がある", "信念のある"),
            arrayOf("ルーズな", "こだわらない", "冷静", "丁寧", "自立している"),
            arrayOf("よく考えない", "行動的", "信念がある", "くじけない", "落ち着いている"),
            arrayOf("けじめがない", "温和", "親切", "控え目", "慎重"),
            arrayOf("責任感がない", "無邪気", "冷静", "清楚", "凛としている"))
    private var _player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        // 画面の部品を指定
        countLabel = findViewById(R.id.countLabel)
        questionLabel = findViewById(R.id.questionLabel)
        answerBtn1 = findViewById(R.id.answerBtn1)
        answerBtn2 = findViewById(R.id.answerBtn2)
        answerBtn3 = findViewById(R.id.answerBtn3)
        answerBtn4 = findViewById(R.id.answerBtn4)
        prevLabel = findViewById(R.id.prevLabel)
        prevLabel2 = findViewById(R.id.prevLabel2)

        prevLabel!!.text = ("  ")
        prevLabel2!!.text = ("  ")

        // quizDataからクイズ出題用のquizArrayを作成する
        for (quizDatum in quizData) {

            // 新しいArrayListを準備
            val tmpArray = ArrayList<String?>()

            // クイズデータを追加
            tmpArray.add(quizDatum[0]) // ネガティブ
            tmpArray.add(quizDatum[1]) // 正解
            tmpArray.add(quizDatum[2]) // 選択肢１
            tmpArray.add(quizDatum[3]) // 選択肢２
            tmpArray.add(quizDatum[4]) // 選択肢３

            // tmpArrayをquizArrayに追加する
            quizArray.add(tmpArray)
        }

        //ここにアラームが鳴る処理を描く
        _player = MediaPlayer()
        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.goodmorning}"
        val mediaFileUri = Uri.parse(mediaFileUriStr)
        try {
            _player?.setDataSource(applicationContext, mediaFileUri)
            _player?.setOnPreparedListener(PlayerPreparedListener())
            _player?.setOnCompletionListener(PlayerCompletaionListener())
            _player?.prepareAsync()
        }
        catch (ex: IllegalArgumentException){
            Log.e("MediaSample", "メディアプレイヤー準備時の例外発生", ex)
        }
        catch (ex: IOException){
            Log.e("MediaSample", "メディアプレイヤー準備時の例外発生", ex)
        }

        showNextQuiz()
    }

    fun showNextQuiz() {
        // クイズカウントラベルを更新
        countLabel!!.text = getString(R.string.count_label, quizCount)

        // ランダムな数字を取得
        val random = Random()
        val randomNum = random.nextInt(quizArray.size)

        // randomNumを使って、quizArrayからクイズを一つ取り出す
        val quiz = quizArray[randomNum]

        // 問題文（ネガティブな言葉）を表示
        questionLabel!!.text = quiz[0]

        // 正解をrightAnswerにセット
        rightAnswer = quiz[1]

        // クイズ配列から問題文（ネガティブな言葉）を削除
        quiz.removeAt(0)

        // 正解と選択肢３つをシャッフル
        Collections.shuffle(quiz)

        // 解答ボタンに正解と選択肢３つを表示
        answerBtn1!!.text = quiz[0]
        answerBtn2!!.text = quiz[1]
        answerBtn3!!.text = quiz[2]
        answerBtn4!!.text = quiz[3]

        // このクイズをquizArrayから削除
        quizArray.removeAt(randomNum)
    }

    fun checkAnswer(view: View) {

        // どの解答ボタンが押されたか
        val answerBtn = findViewById<Button>(view.id)
        val btnText = answerBtn.text.toString()
        val alertTitle: String
        if (btnText == rightAnswer) {
            alertTitle = "正解!"
            rightAnswerCount++
            quizCount++
        } else {
            alertTitle = "不正解..."
            quizCount = 1
        }

        // 結果を表示
        prevLabel!!.text = getString(R.string.prev_label, alertTitle)
        prevLabel2!!.text = getString(R.string.prev_label2, rightAnswer)
        if (quizCount == QUIZ_COUNT) {
            // 結果画面へ移動
            //アラームを止める
            stop()
            finish()
        } else {
            showNextQuiz()
        }

    }

    companion object {
        private const val QUIZ_COUNT = 4
    }


    private inner class PlayerPreparedListener : MediaPlayer.OnPreparedListener{
        override fun onPrepared(mp: MediaPlayer) {
            AlarmStart()
        }
    }

    private  inner class PlayerCompletaionListener : MediaPlayer.OnCompletionListener{
        override fun onCompletion(mp: MediaPlayer) {

        }
    }

    fun AlarmStart(){
        //フィールドのプレーヤーがnullじゃなかったら
        _player?.let {
            //プレーヤーが再生中でないなら再生開始
            if(!it.isPlaying) {
                //ループ設定する
                it.isLooping = true
                //再生開始
                it.start()
            }
        }
    }

    fun stop(){
        //フィールドのプレーヤーがnullじゃなかったら
        _player?.let {
            //プレーヤーが再生中の場合
            if(it.isPlaying) {
                //再生停止
                it.stop()
            }
            //プレーヤーを開放
            it.release()
            //プレーヤー用フィールドをnull
            _player = null
        }
    }

}