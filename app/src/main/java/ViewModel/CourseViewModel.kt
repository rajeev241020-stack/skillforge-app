package ViewModel



import Api.Category
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import Api.Course
import Repository.CourseRepository
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch

class CourseViewModel(private val repository: CourseRepository) : ViewModel() {

    private val _popularCourses = MutableLiveData<List<Course>>()
    val popularCourses: LiveData<List<Course>> = _popularCourses

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchCourses() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val courses = repository.getPopularCourses()
                _popularCourses.postValue(courses)
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}



class CourseViewModelFactory(
    private val repository: CourseRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            return CourseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
