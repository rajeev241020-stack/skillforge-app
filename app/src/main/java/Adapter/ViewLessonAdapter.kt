package Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import Api.Lesson
import com.intern.skillforgeapp.R
import com.intern.skillforgeapp.databinding.VideoLessonItemBinding


class VideoLessonsAdapter(
    private var lessons: List<Lesson> = emptyList(),
    private var currentIndex: Int = 0
) : RecyclerView.Adapter<VideoLessonsAdapter.ViewHolder>() {

    private var onItemClick: ((Lesson, Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Lesson, Int) -> Unit) {
        onItemClick = listener
    }

    fun setCurrentLesson(index: Int) {
        val old = currentIndex
        currentIndex = index
        notifyItemChanged(old)
        notifyItemChanged(index)
    }

    inner class ViewHolder(val binding: VideoLessonItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VideoLessonItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = lessons[position]
        val isPlaying = position == currentIndex
        val isFree = lesson.isFree

        with(holder.binding) {
            tvLessonTitle.text = lesson.title
            tvLessonSub.text = if (isPlaying) "Now playing · ${lesson.durationMinutes} min"
            else "${lesson.durationMinutes} min"

            if (isPlaying) {
                root.setBackgroundResource(R.drawable.lesson_active_bg)
                tvLessonTitle.setTextColor(ContextCompat.getColor(root.context, R.color.teal_200))
                tvLessonSub.setTextColor(ContextCompat.getColor(root.context, R.color.teal_200))
                layoutIcon.setBackgroundResource(R.drawable.lesson_active_bg)
                ivLessonIcon.setImageResource(R.drawable.ic_pause)
                tvFreeBadge.visibility = View.GONE
            } else if (isFree) {
                root.setBackgroundResource(R.drawable.edit_drawable_shape)
                tvLessonTitle.setTextColor(ContextCompat.getColor(root.context, R.color.black))
                tvLessonSub.setTextColor(ContextCompat.getColor(root.context, R.color.purple_200))
                layoutIcon.setBackgroundResource(R.drawable.shape_courses)
                ivLessonIcon.setImageResource(R.drawable.play_button_arrowhead_svgrepo_com)
                tvFreeBadge.visibility = View.VISIBLE
            } else {

                root.setBackgroundResource(R.drawable.edit_drawable_shape)
                tvLessonTitle.setTextColor(ContextCompat.getColor(root.context, R.color.purple_200))
                tvLessonSub.setTextColor(ContextCompat.getColor(root.context, R.color.black))
                layoutIcon.setBackgroundResource(R.drawable.notification_shape)
                ivLessonIcon.setImageResource(android.R.drawable.ic_lock_lock)
                tvFreeBadge.visibility = View.GONE
            }

            root.setOnClickListener {
                if (isPlaying || isFree) {
                    onItemClick?.invoke(lesson, position)
                }
            }
        }
    }

    override fun getItemCount() = lessons.size
}