package com.example.bodydivinity

import FoodDao
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FoodViewModel(private val dao: FoodDao) : ViewModel() {

    val foods: StateFlow<List<Food>> =
        dao.getAllFoods()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    private val _selectedFoods = MutableStateFlow<List<Food>>(emptyList())
    val selectedFoods: StateFlow<List<Food>> = _selectedFoods

    init {
        seedDatabase()
    }

    fun addFoodByName(name: String) {
        viewModelScope.launch {
            val food = dao.getFoodByName(name)
            food?.let {
                _selectedFoods.value = _selectedFoods.value + it
            }
        }
    }

    private fun seedDatabase() {
        viewModelScope.launch {
            if (dao.getAllFoodsOnce().isEmpty()) {

                dao.insertFood(
                    Food(
                        name = "Arroz blanco (100g)",
                        calories = 130,
                        protein = 2,
                        carbs = 28,
                        fat = 0
                    )
                )

                dao.insertFood(
                    Food(
                        name = "Huevo (1 unidad)",
                        calories = 70,
                        protein = 6,
                        carbs = 1,
                        fat = 5
                    )
                )

                dao.insertFood(
                    Food(
                        name = "Carne de res (100g)",
                        calories = 250,
                        protein = 26,
                        carbs = 0,
                        fat = 17
                    )
                )
            }
        }
    }

    fun addFood(food: Food) {
        viewModelScope.launch {
            dao.insertFood(food)
        }
    }

    fun deleteFood(food: Food) {
        viewModelScope.launch {
            dao.deleteFood(food)
        }
    }
}
