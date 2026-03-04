package com.example.bodydivinity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bodydivinity.ui.theme.BodyDivinityTheme
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import com.example.bodydivinity.AppDatabase
import com.example.bodydivinity.Food
import com.example.bodydivinity.FoodViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Entity(tableName = "foods")
data class Food(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int
)

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "food_database"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = FoodViewModelFactory(db.foodDao())

        setContent {
            val viewModel: FoodViewModel = viewModel(factory = factory)
            CaloriesScreen(viewModel)
        }

    }
}

private fun RoomDatabase.Builder<AppDatabase>.addCallback(callback: Any) {}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaloriesScreen(viewModel: FoodViewModel) {
    val foods by viewModel.foods.collectAsState()
    var calories by remember { mutableStateOf(0) }
    var totalProtein by remember { mutableStateOf(0) }
    var totalCarbs by remember { mutableStateOf(0) }
    var totalFat by remember { mutableStateOf(0) }
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            /*if (isExpanded) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .fillMaxWidth()
                        .height(700.dp)
                        .clickable{}
                        .width(2.dp)

                ){}
            }*/

            FloatingActionButton(
                onClick = {
                    showSheet = true
                },
                containerColor = MaterialTheme.colorScheme.primary, // Color del tema
                shape = RoundedCornerShape(16.dp) // Aquí aplicas lo que aprendimos antes
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        },
        floatingActionButtonPosition = FabPosition.End // Posición por defecto a la derecha
    ) { innerPadding ->
        // El contenido de tu pantalla va aquí
        Column(modifier = Modifier.padding(innerPadding)) {
            // Tu Row con fondo redondeado puede ir aquí
        }
    }
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text("Seleccionar comida", fontSize = 20.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.addFoodByName("Carne de res (100g)")
                    }
                ) {
                    Text("Agregar Carne")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    // aquí luego abrirás formulario personalizado
                }) {
                    Text("Crear nueva comida")
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🔥 Calorías grandes
        Text(
            text = calories.toString(),
            fontSize = 48.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "kcal")

        Spacer(modifier = Modifier.height(32.dp))

        // 🧱 Fila de macros
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = totalProtein.toString())
                Text(text = "Protein")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = totalCarbs.toString())
                Text(text = "Carbs")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = totalFat.toString())
                Text(text = "Fats")
            }
        }
        Spacer(modifier = Modifier.height(32.dp))



        Button(
            onClick = {

            }

        ) {
            Text(text = "+ Agregar calorías")
        }
        Spacer(modifier = Modifier.height(24.dp))

        foods.forEach { food ->
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {}
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Nombre
                Text(
                    text = "${food.name}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                // Calorías
                Text(
                    text = "${food.calories} kcal",
                    fontSize = 14.sp
                )

                // Macros
                Text(
                    text = "Fats ${food.fat}g",
                    fontSize = 14.sp
                )

                Text(
                    text = "Carbs ${food.carbs}g",
                    fontSize = 14.sp
                )
                Button(
                    onClick = {

                    }

                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BodyDivinityTheme {
        Greeting("Android")
    }
}
