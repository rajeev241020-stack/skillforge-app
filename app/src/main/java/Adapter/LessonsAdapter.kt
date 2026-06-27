package Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import Api.Lesson
import com.intern.skillforgeapp.databinding.LessonItemBinding

class LessonsAdapter(
    private var lessons: List<Lesson> = emptyList(),
    private val onItemClick: (Lesson, Int) -> Unit
) : RecyclerView.Adapter<LessonsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LessonItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LessonItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = lessons[position]
        with(holder.binding) {
            tvLessonTitle.text = lesson.title
            tvLessonDuration.text = "${lesson.durationMinutes} min"

            if (lesson.isFree) {
                tvFreeBadge.visibility = View.VISIBLE
                ivPlayIcon.setImageResource(com.intern.skillforgeapp.R.drawable.play_button_arrowhead_svgrepo_com)
                ivPlayIcon.imageTintList = android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#2dd4bf")
                )
                layoutIcon.setBackgroundResource(com.intern.skillforgeapp.R.drawable.shape_courses)
                tvLessonTitle.setTextColor(android.graphics.Color.BLACK)
                tvLessonDuration.setTextColor(android.graphics.Color.parseColor("#A1A3AE"))
            } else {
                tvFreeBadge.visibility = View.GONE
                ivPlayIcon.setImageResource(android.R.drawable.ic_lock_lock)
                ivPlayIcon.imageTintList = android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#A1A3AE")
                )
                layoutIcon.setBackgroundResource(com.intern.skillforgeapp.R.drawable.notification_shape)
                tvLessonTitle.setTextColor(android.graphics.Color.parseColor("#A1A3AE"))
                tvLessonDuration.setTextColor(android.graphics.Color.parseColor("#A1A3AE"))
            }

            root.setOnClickListener {
                if (position != RecyclerView.NO_POSITION && lesson.isFree) {
                    onItemClick(lesson, position)
                }
            }
        }
    }

    override fun getItemCount() = lessons.size
}