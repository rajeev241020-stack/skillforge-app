package Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import Api.Category
import com.intern.skillforgeapp.databinding.CategoriesCousesLayoutBinding

class CategoriesAdapter(
    private var categories: List<Category> = emptyList()
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private var onItemClick: ((Category) -> Unit)? = null

    fun setOnItemClickListener(listener: (Category) -> Unit) {
        onItemClick = listener
    }

    fun updateList(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: CategoriesCousesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            with(binding) {
                coursesName.text = category.name
                coursesNumber.text = "${category.courseCount} courses"

                val color = Color.parseColor(category.iconColor)
                layoutColor.background.setTint(adjustAlpha(color, 0.15f))
                viewColor.background.setTint(color)

                root.setOnClickListener {
                    onItemClick?.invoke(category)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoriesCousesLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount() = categories.size
}

fun adjustAlpha(color: Int, factor: Float): Int {
    val alpha = (Color.alpha(color) * factor).toInt()
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)
    return Color.argb(alpha, red, green, blue)
}