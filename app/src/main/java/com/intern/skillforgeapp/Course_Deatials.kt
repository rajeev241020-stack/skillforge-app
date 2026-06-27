//package com.intern.skillforgeapp
//
//import Adapter.LessonsAdapter
//import Api.Course
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.intern.skillforgeapp.databinding.ActivityCourseDeatialsBinding
//
//class Course_Deatials : AppCompatActivity() {
//    private val binding: ActivityCourseDeatialsBinding by lazy {
//        ActivityCourseDeatialsBinding.inflate(layoutInflater)
//    }
//    private lateinit var Adapter: LessonsAdapter
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(binding.root)
//
//
//        val course = intent.getSerializableExtra("course") as? Course
//        course?.let { bindCourse(it) }
//
//        binding.btnBack.setOnClickListener { finish() }
//    }
//
//    private fun bindCourse(course: Course) {
//        with(binding) {
//            tvHeaderTitle.text = course.title
//            tvTitle.text = course.title
//            tvSubtitle.text = course.subtitle
//            tvRating.text = course.rating.toString()
//            tvStudents.text = course.studentsEnrolled.toLocaleString()
//            tvDuration.text = "${course.durationHours}h"
//            tvLevel.text = course.level
//            tvDescription.text = course.description
//            tvInstructorName.text = course.instructor.name
//            tvInstructorTitle.text = course.instructor.title
//            sortName.text = course.instructor.name.initials()
//             btnFollow.setOnClickListener {
//                 btnFollow.text="Followed"
//                 btnFollow.setTextColor(ContextCompat.getColor(this@Course_Deatials,R.color.black))
//                 Toast.makeText(this@Course_Deatials,"Follow now", Toast.LENGTH_SHORT).show()
//             }
//            val totalMin = course.lessons.sumOf { it.durationMinutes }
//            tvLessonsInfo.text = "${course.lessons.size} lessons · $totalMin min"
//
//            recyclerLessons.apply {
//                layoutManager = LinearLayoutManager(this@Course_Deatials)
//                Adapter = LessonsAdapter(course.lessons, { lesson, index ->
//                    val intent = Intent(this@Course_Deatials, VideoPlayerActivity::class.java)
//                    intent.putExtra("course", course)
//                    intent.putExtra("lesson_index", index)
//                    startActivity(intent)
//                }
//                )
//                adapter = Adapter
//
//            }
//        }
//    }
//}
//
//fun String.initials(): String {
//    return split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
//}
//
//fun Int.toLocaleString(): String {
//    return String.format("%,d", this)
//}
package com.intern.skillforgeapp

import Adapter.LessonsAdapter
import Api.Course
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.intern.skillforgeapp.databinding.ActivityCourseDeatialsBinding

class Course_Deatials : AppCompatActivity() {

    private val binding: ActivityCourseDeatialsBinding by lazy {
        ActivityCourseDeatialsBinding.inflate(layoutInflater)
    }

    private lateinit var lessonsAdapter: LessonsAdapter
    private var course: Course? = null
    private var isBookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        course = intent.getSerializableExtra("course") as? Course
        course?.let { bindCourse(it) }

        binding.btnBack.setOnClickListener { finish() }

        binding.btnBookmark.setOnClickListener {
            isBookmarked = !isBookmarked
            binding.btnBookmark.setImageResource(
                if (isBookmarked) R.drawable.outline_bookmark_24
                else R.drawable.outline_bookmark_24
            )
            binding.btnBookmark.imageTintList =
                if (isBookmarked) android.content.res.ColorStateList.valueOf(Color.parseColor("#2dd4bf"))
                else android.content.res.ColorStateList.valueOf(Color.WHITE)
        }

        binding.btnEnroll.setOnClickListener {
            val intent = Intent(this, VideoPlayerActivity::class.java)
            intent.putExtra("course", course)
            intent.putExtra("lesson_index", 0)
            startActivity(intent)
        }

        binding.btnFollow.setOnClickListener {
            val isFollowing = binding.btnFollow.text == "Following"
            binding.btnFollow.text = if (isFollowing) "Follow" else "Following"
            binding.btnFollow.setTextColor(
                if (isFollowing) Color.parseColor("#2dd4bf")
                else Color.parseColor("#A1A3AE")
            )
        }
    }

    private fun bindCourse(course: Course) {
        with(binding) {

            // ── Header ──
            tvHeaderTitle.text = course.title
            tvTag.text = "// ${course.tags.firstOrNull()?.lowercase() ?: "course"}"

            // ── Tags chips ──
            tagsRow.removeAllViews()
            course.tags.forEach { tag ->
                val chip = TextView(this@Course_Deatials).apply {
                    text = tag
                    textSize = 12f
                    setTextColor(Color.parseColor("#2dd4bf"))
                    background = ContextCompat.getDrawable(
                        this@Course_Deatials, R.drawable.shape_courses
                    )
                    setPadding(24, 8, 24, 8)
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply { marginEnd = 8 }
                    layoutParams = params
                }
                tagsRow.addView(chip)
            }

            // ── Course info ──
            tvTitle.text = course.title
            tvSubtitle.text = course.subtitle
            tvRating.text = course.rating.toString()
            tvStudents.text = "${course.studentsEnrolled.toLocaleString()} students"
            tvDuration.text = "${course.durationHours}h"
            tvLevel.text = course.level
            tvLanguage.text = "🌐 ${course.language}"
            tvLastUpdated.text = "Updated ${course.lastUpdated}"
            tvDescription.text = course.description


            tvInstructorName.text = course.instructor.name
            tvInstructorTitle.text = course.instructor.title
            tvInstructorBio.text = course.instructor.bio
            sortName.text = course.instructor.name.initials()


            val totalMin = course.lessons.sumOf { it.durationMinutes }
            tvLessonsInfo.text = "${course.lessons.size} lessons · $totalMin min"


            lessonsAdapter = LessonsAdapter(course.lessons) { lesson, index ->
                val intent = Intent(this@Course_Deatials, VideoPlayerActivity::class.java)
                intent.putExtra("course", course)
                intent.putExtra("lesson_index", index)
                startActivity(intent)
            }

            recyclerLessons.apply {
                layoutManager = LinearLayoutManager(this@Course_Deatials)
                adapter = lessonsAdapter
            }
        }
    }
}

fun String.initials(): String {
    return split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
}

fun Int.toLocaleString(): String {
    return String.format("%,d", this)
}