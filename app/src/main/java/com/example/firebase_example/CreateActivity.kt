package com.example.firebase_example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_main.*

class CreateActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        val actionBar = supportActionBar
        actionBar!!.title = "Tambah"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        ref = FirebaseDatabase.getInstance().getReference("USERS")
        btnSave.setOnClickListener {
            saveData()
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveData() {
        val inputNIS = nis.text.toString()
        val inputNama = nama.text.toString()

        val userId = ref.push().key.toString()
        val user = Users(userId,inputNIS,inputNama)

        ref.child(userId).setValue(user).addOnCompleteListener {
            Toast.makeText(this, "Siswa berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            nis.setText(" ")
            nama.setText(" ")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}