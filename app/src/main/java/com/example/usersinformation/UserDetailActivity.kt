package com.example.usersinformation

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.usersinformation.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val user = intent.getParcelableExtra<ApiUser>("USER")
        if (user != null){

            setTextIfValid(user.gender, binding.detailGender, "gender")
            setTextIfValid(user.name?.getFullName(), binding.detailName, "name")
            setTextIfValid(user.location?.getLocation(), binding.detailLocation, "location")
            setTextIfValid(user.email, binding.detailEmail, "email")
            setTextIfValid(user.login?.getLogin(), binding.detailLogin, "login")
            setTextIfValid(user.dob.date, binding.detailDOB, "date of birthday")
            setTextIfValid(user.registered?.getRegistred(), binding.detailRegistred, "registered")
            setTextIfValid(user.phone, binding.detailTelephone, "phone")
            setTextIfValid(user.cell, binding.detailCell, "cell")
            setTextIfValid(user.id?.getId(), binding.detailId, "id")
            setTextIfValid(user.nat, binding.detailNationality, "nationality")

            Glide.with(this)
                .load(user.picture.large)
                .circleCrop()
                .into(binding.detailAvatar)

            binding.detailEmail.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:${user.email}")
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }else{
                    Toast.makeText(this@UserDetailActivity,"Почтовый клиент не найден", Toast.LENGTH_LONG).show()
                    Log.d("IntentCheck", "Email resolved: ${intent.resolveActivity(packageManager)}")
                }
            }

            binding.detailTelephone.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${user.phone}")
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }else{
                    Toast.makeText(this@UserDetailActivity,"Ошибка при переходе в набор номера", Toast.LENGTH_LONG).show()
                    Log.d("IntentCheck", "Telephone resolved: ${intent.resolveActivity(packageManager)}")
                }
            }

            val lat = user.location.coordinates.latitude
            val lon = user.location.coordinates.longitude
            val geoUri = Uri.parse("geo:$lat,$lon?q=$lat,$lon(${Uri.encode("User Location")})")

            binding.detailLocation.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW,geoUri)
                if (intent.resolveActivity(packageManager)!=null){
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this@UserDetailActivity,"Клиент для карт не найден", Toast.LENGTH_LONG).show()
                    Log.d("IntentCheck", "Geo intent resolved: ${intent.resolveActivity(packageManager)}")
                    Log.d("IntentCheck", "Geo intent alternative version run")
                    val gmmIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lon")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    if (mapIntent.resolveActivity(packageManager) != null) {
                        startActivity(mapIntent)
                    } else {
                        Toast.makeText(this, "Google Maps не установлены", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            finish()
        }
    }

    private fun showMissingIdWarning(value: String) {
        Toast.makeText(this, "У пользователя отсутствует: $value", Toast.LENGTH_LONG).show()
    }

    private fun setTextIfValid(value: String?, textView: TextView, fieldName: String) {
        if (value.isNullOrBlank()) {
            showMissingIdWarning(fieldName)
        } else {
            textView.text = value
            textView.setTextColor(Color.BLACK)
        }
    }
}