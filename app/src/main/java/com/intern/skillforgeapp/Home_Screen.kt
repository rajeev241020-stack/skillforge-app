package com.intern.skillforgeapp

import Adapter.CategoriesAdapter

import Adapter.PopularCoursesAdapter
import Api.PopularCourseData
import Api.RetrofitInstance
import Model.CourseData
import Repository.CategoryRepository
import Repository.CourseRepository
import ViewModel.CategoryViewModel
import ViewModel.CategoryViewModelFactory
import ViewModel.CourseViewModel
import ViewModel.CourseViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.cursoradapter.widget.CursorAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.intern.skillforgeapp.databinding.ActivityHomeScreenBinding
import kotlinx.coroutines.launch
import kotlin.jvm.Throws

class Home_Screen : AppCompatActivity() {
    private val binding: ActivityHomeScreenBinding by lazy {
        ActivityHomeScreenBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: PopularCoursesAdapter

    private lateinit var Adapter: CategoriesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val repository = CourseRepository(RetrofitInstance.api)
        val factory = CourseViewModelFactory(repository)
        val viewModel = ViewModelProvider(this@Home_Screen, factory)[CourseViewModel::class.java]
        viewModel.isLoading.observe(this@Home_Screen) { loading ->
            binding.progrss.isVisible = loading
        }
        viewModel.error.observe(this@Home_Screen) { error ->
            error?.let { Toast.makeText(this@Home_Screen, it, Toast.LENGTH_SHORT).show() }
        }
        binding.recyclerPopularCourses.layoutManager = LinearLayoutManager(
            this@Home_Screen,
            LinearLayoutManager.VERTICAL, false
        )

        lifecycleScope.launch {
            viewModel.popularCourses.observe(this@Home_Screen) { courses ->
                adapter = PopularCoursesAdapter(courses)
                binding.recyclerPopularCourses.adapter = adapter
                adapter.updateList(courses)
                adapter.setOnItemClickListener { course ->
                    val intent = Intent(this@Home_Screen, Course_Deatials::class.java)
                    intent.putExtra("course", course)
                    startActivity(intent)
                }
            }
        }

        viewModel.fetchCourses()


        val repositorys = CategoryRepository(RetrofitInstance.api)
        val factorys = CategoryViewModelFactory(repositorys)
        val viewModels = ViewModelProvider(this@Home_Screen,factorys)[CategoryViewModel::class.java]

        Adapter = CategoriesAdapter()
        binding.recyclerCourses.apply {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
            isNestedScrollingEnabled = false
            this.adapter= Adapter
        }


        viewModels.categories
            .observe(this@Home_Screen) { categories ->
            Adapter.updateList(categories)
        }

        viewModel.isLoading.observe(this@Home_Screen) { loading ->
            binding.progrss.isVisible = loading
        }

        viewModel.error.observe(this@Home_Screen) { error ->
            error?.let { Toast.makeText(this@Home_Screen, it, Toast.LENGTH_SHORT).show() }
        }

        viewModels.fetchCategories()

    }
}