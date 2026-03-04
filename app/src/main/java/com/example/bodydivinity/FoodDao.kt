
import com.example.bodydivinity.Food
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Insert
    suspend fun insertFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)

    @Query("SELECT * FROM foods ORDER BY id DESC")
    fun getAllFoods(): Flow<List<Food>>

    @Query("SELECT * FROM foods")
    suspend fun getAllFoodsOnce(): List<Food>

    @Query("SELECT * FROM foods WHERE name = :name LIMIT 1")
    suspend fun getFoodByName(name: String): Food?
}