package com.example.usersinformation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.usersinformation.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<ApiUser>("USER")
        if (user != null){

            binding.detailGender.text = user.gender
            binding.detailName.text = user.name.getFullName()
            binding.detailLocation.text = user.location.getLocation()
            binding.detailEmail.text = user.email
            binding.detailLogin.text = user.login.getLogin()
            binding.detailDOB.text = user.dob.date
            binding.detailRegistred.text = user.registered.getRegistred()
            binding.detailTelephone.text = user.phone
            binding.detailCell.text = user.cell
            binding.detailId.text = user.id.getId()
            Glide.with(this)
                .load(user.picture.medium)
                .circleCrop()
                .into(binding.detailAvatar)
            binding.detailNationality.text = user.nat


            binding.detailEmail.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:${user.email}")
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }

            binding.detailTelephone.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${user.phone}")
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }else{
            finish()
        }
    }
}