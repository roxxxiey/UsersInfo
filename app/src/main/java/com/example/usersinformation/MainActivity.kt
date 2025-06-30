package com.example.usersinformation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.usersinformation.dataBase.AppDatabase
import com.example.usersinformation.dataBase.UserDao
import com.example.usersinformation.dataBase.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: RecycleViewAdapter
    private val apiService = RandomApiService.create()
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val recyclerView = findViewById<RecyclerView>(R.id.usersList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = RecycleViewAdapter(mutableListOf()) { user ->
            // Запуск Activity с детальной информацией
            val intent = Intent(this, UserDetailActivity::class.java).apply {
                putExtra("USER", user)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        db = AppDatabase.getInstance(this)
        userDao = db.userDao()

        lifecycleScope.launch {
            val savedUsers = withContext(Dispatchers.IO) {
                userDao.getAll().map { it.toApiUser() }
            }

            if (savedUsers.isNotEmpty()) {
                adapter.updateUsers(savedUsers)
            }

            loadUsersFromApi()
        }
    }

    private fun loadUsersFromApi() {
        lifecycleScope.launch {
            try {
                Log.d("MainActivity", "Загрузка пользователей...")
                val response = apiService.getUsers()

                if (response.isSuccessful) {
                    val apiUsers = response.body()?.results ?: emptyList()

                    withContext(Dispatchers.IO) {
                        userDao.clearAll()
                        userDao.insertAll(apiUsers.map { UserEntity.fromApiUser(it) })
                    }
                    adapter.updateUsers(apiUsers)
                } else {
                    Log.e("MainActivity", "Ошибка API: ${response.code()}")
                    showTestData()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Ошибка при загрузке: ${e.message}", e)
                showTestData()
            }
        }
    }

    private fun showTestData() {
        val users = listOf(
            ApiUser(
                gender = "male",
                name = ApiUser.Name("Mr", "Иван", "Иванов"),
                location = ApiUser.Location(
                    street = ApiUser.Street(123, "Main St"),
                    city = "Moscow",
                    state = "Moscow",
                    country = "Russia",
                    postcode = "123456",
                    coordinates = ApiUser.Coordinates("55.75", "37.61"),
                    timezone = ApiUser.Timezone("+3", "Moscow")
                ),
                email = "ivan@example.com",
                login = ApiUser.Login("uuid", "ivanov", "password", "salt", "md5", "sha1", "sha256"),
                dob = ApiUser.Dob("1990-01-01", 33),
                registered = ApiUser.Registered("2020-01-01", 3),
                phone = "+7 (123) 456-7890",
                cell = "+7 (987) 654-3210",
                id = ApiUser.Id("passport", "123456789"),
                picture = ApiUser.Picture(
                    "https://randomuser.me/api/portraits/men/1.jpg",
                    "https://randomuser.me/api/portraits/men/1.jpg",
                    "https://randomuser.me/api/portraits/men/1.jpg"
                ),
                nat = "RU"
            ),
            ApiUser(
                gender = "male",
                name = ApiUser.Name("Mr", "Иван", "Иванов"),
                location = ApiUser.Location(
                    street = ApiUser.Street(123, "Main St"),
                    city = "Moscow",
                    state = "Moscow",
                    country = "Russia",
                    postcode = "123456",
                    coordinates = ApiUser.Coordinates("55.75", "37.61"),
                    timezone = ApiUser.Timezone("+3", "Moscow")
                ),
                email = "ivan@example.com",
                login = ApiUser.Login("uuid", "ivanov", "password", "salt", "md5", "sha1", "sha256"),
                dob = ApiUser.Dob("1990-01-01", 33),
                registered = ApiUser.Registered("2020-01-01", 3),
                phone = "+7 (123) 456-7890",
                cell = "+7 (987) 654-3210",
                id = ApiUser.Id("passport", "123456789"),
                picture = ApiUser.Picture(
                    "https://randomuser.me/api/portraits/men/1.jpg",
                    "https://randomuser.me/api/portraits/men/1.jpg",
                    "https://randomuser.me/api/portraits/men/1.jpg"
                ),
                nat = "RU"
            )
        ).toMutableList()
        adapter.updateUsers(users)
    }
}
