package Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import Api.Course
import com.bumptech.glide.Glide
import com.intern.skillforgeapp.databinding.PopularCoursesLayoutBinding

class PopularCoursesAdapter(
    private var courses: List<Course> = emptyList()
) : RecyclerView.Adapter<PopularCoursesAdapter.ViewHolder>() {

    private var onItemClick: ((Course) -> Unit)? = null

    fun setOnItemClickListener(listener: (Course) -> Unit) {
        onItemClick = listener
    }

    fun updateList(newCourses: List<Course>) {
        courses = newCourses
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: PopularCoursesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            with(binding) {

                courseBeginner.text = course.level.uppercase()
                popularCourseName.text = course.title
                popularCourseTecher.text = course.instructor.name
                ratingCourse.text = course.rating.toString()
                courseTime.text = "${course.durationHours}h"

                Glide.with(root.context)
                    .load(course.thumbnailUrl)
                    .centerCrop()
                    .into(popularCourseImage)

                root.setOnClickListener {
                    onItemClick?.invoke(course)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PopularCoursesLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    override fun getItemCount() = courses.size
}