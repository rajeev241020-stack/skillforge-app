package com.intern.skillforgeapp

import Adapter.VideoLessonsAdapter
import Api.Course
import Api.Lesson
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.intern.skillforgeapp.databinding.ActivityLessonBinding

class VideoPlayerActivity : AppCompatActivity() {

    private val binding: ActivityLessonBinding by lazy {
        ActivityLessonBinding.inflate(layoutInflater)
    }

    private lateinit var player: ExoPlayer
    private lateinit var lessonsAdapter: VideoLessonsAdapter
    private var currentLessonIndex = 0
    private var course: Course? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        course = intent.getSerializableExtra("course") as? Course
        currentLessonIndex = intent.getIntExtra("lesson_index", 0)

        setupPlayer()
        course?.let { setupUI(it) }

        binding.btnBack.setOnClickListener { finish() }
        setupTabs()
    }


    private fun setupTabs() {
        selectTab(0)
        binding.tabLessons.setOnClickListener { selectTab(0) }
        binding.tabNotes.setOnClickListener { selectTab(1) }
        binding.tabResources.setOnClickListener { selectTab(2) }
    }

    private fun selectTab(index: Int) {
        val gray = Color.parseColor("#A1A3AE")
        val black = ContextCompat.getColor(this, R.color.black)

        binding.tabLessons.setTextColor(gray)
        binding.tabNotes.setTextColor(gray)
        binding.tabResources.setTextColor(gray)
        binding.tabLessons.setBackgroundResource(0)
        binding.tabNotes.setBackgroundResource(0)
        binding.tabResources.setBackgroundResource(0)

        when (index) {
            0 -> {
                binding.tabLessons.setTextColor(black)
                binding.tabLessons.setBackgroundResource(R.drawable.tab_underline_active)
                binding.recyclerLessons.visibility = View.VISIBLE
            }
            1 -> {
                binding.tabNotes.setTextColor(black)
                binding.tabNotes.setBackgroundResource(R.drawable.tab_underline_active)
                binding.recyclerLessons.visibility = View.GONE
            }
            2 -> {
                binding.tabResources.setTextColor(black)
                binding.tabResources.setBackgroundResource(R.drawable.tab_underline_active)
                binding.recyclerLessons.visibility = View.GONE
            }
        }
    }


    private fun setupPlayer() {
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player

        binding.btnPlayPause.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                binding.btnPlayPause.setImageResource(R.drawable.play_button_svgrepo_com)
            } else {
                player.play()
                binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
            }
        }

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) startSeekBarUpdate()
            }
        })

        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser && player.duration > 0) {
                    player.seekTo(player.duration * progress / 100)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun startSeekBarUpdate() {
        binding.seekbar.post(object : Runnable {
            override fun run() {
                if (::player.isInitialized && player.isPlaying) {
                    val duration = player.duration
                    val position = player.currentPosition
                    if (duration > 0) {
                        binding.seekbar.progress = (position * 100 / duration).toInt()
                        binding.tvCurrentTime.text = formatTime(position)
                        binding.tvTotalTime.text = formatTime(duration)
                    }
                    binding.seekbar.postDelayed(this, 500)
                }
            }
        })
    }

    private fun formatTime(ms: Long): String {
        val total = ms / 1000
        return String.format("%02d:%02d", total / 60, total % 60)
    }

    private fun playLesson(lesson: Lesson) {
        val fakeUrls = mapOf(
            "les_k1" to "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4",
            "les_k2" to "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3",
            "les_k3" to "https://storage.googleapis.com/exoplayer-test-media-1/mkv/android-screens-lavf-56.36.100-aac-avc-main-1280x720.mkv",
            "les_c1" to "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4",
            "les_c2" to "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4",
            "les_c3" to "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4",
            "les_r1" to "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4",
            "les_r2" to "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4",
            "les_r3" to "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
        )

        val url = if (lesson.videoUrl.contains("example.com")) {
            fakeUrls[lesson.id] ?: "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
        } else {
            lesson.videoUrl
        }

        player.stop()
        player.clearMediaItems()
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        player.play()
        binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
    }

    private fun setupUI(course: Course) {
        val lessons = course.lessons
        val currentLesson = lessons[currentLessonIndex]

        playLesson(currentLesson)
        bindLessonInfo(course, currentLesson, currentLessonIndex)

        lessonsAdapter = VideoLessonsAdapter(lessons, currentLessonIndex)
        binding.recyclerLessons.apply {
            layoutManager = LinearLayoutManager(this@VideoPlayerActivity)
            adapter = lessonsAdapter
        }

        lessonsAdapter.setOnItemClickListener { lesson, index ->
            currentLessonIndex = index
            lessonsAdapter.setCurrentLesson(index)
            bindLessonInfo(course, lesson, index)
            playLesson(lesson)
        }
    }

    private fun bindLessonInfo(course: Course, lesson: Lesson, index: Int) {
        with(binding) {
            tvLessonLabel.text = "LESSON ${index + 1} · ${course.title.uppercase()}"
            tvLessonTitle.text = lesson.title
            tvLessonDesc.text = lesson.content
            tvVideoTitle.text = course.title
        }
    }


    override fun onPause() {
        super.onPause()
        if (::player.isInitialized) player.pause()
    }

    override fun onResume() {
        super.onResume()
        if (::player.isInitialized) player.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::player.isInitialized) player.release()
    }
}