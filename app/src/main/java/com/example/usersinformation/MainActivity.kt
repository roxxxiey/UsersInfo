package com.example.usersinformation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usersinformation.dataBase.AppDatabase
import com.example.usersinformation.dataBase.UserDao
import com.example.usersinformation.dataBase.UserEntity
import com.example.usersinformation.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: RecycleViewAdapter
    private val apiService = RandomApiService.create()
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = binding.usersList
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = RecycleViewAdapter(mutableListOf()) { user ->
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
        }


        binding.addNewUsers.setOnClickListener {
            loadUsersFromApi()
        }

        binding.deleteData.setOnClickListener {
            lifecycleScope.launch {
                try {
                    userDao.clearAll()
                    Toast.makeText(this@MainActivity, "Все данные удалены", Toast.LENGTH_SHORT).show()

                    val newList = userDao.getAll().map { it.toApiUser() }
                    adapter.updateUsers(newList)

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Ошибка при удалении данных: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadUsersFromApi() {
        lifecycleScope.launch {
            try {
                Log.d("MainActivity", "Загрузка пользователей...")
                val response = apiService.getUsers()

                if (response.isSuccessful) {
                    val apiUsers = response.body()?.results ?: emptyList()

                    if (apiUsers.isEmpty()) {
                        Log.e("MainActivity", "Получен пустой список пользователей")
                        Toast.makeText(this@MainActivity, "Получен пустой список пользователей", Toast.LENGTH_LONG).show()
                    }

                    withContext(Dispatchers.IO) {
                        userDao.insertAll(apiUsers.map { UserEntity.fromApiUser(it) })
                    }

                    val allUsers = withContext(Dispatchers.IO) {
                        userDao.getAll().map { it.toApiUser() }
                    }
                    adapter.updateUsers(allUsers)

                } else {
                    Log.e("MainActivity", "Ошибка API: ${response.code()}")
                    Toast.makeText(this@MainActivity, "Ошибка API: код ${response.code()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Ошибка при загрузке: ${e.message}", e)
                Toast.makeText(this@MainActivity, "Ошибка загрузки: ${e.localizedMessage ?: "Неизвестная ошибка"}", Toast.LENGTH_LONG).show()
            }
        }
    }

}
