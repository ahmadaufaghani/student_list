package com.example.firebase_example

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

class Adapter(val mCtx : Context, val layoutResId: Int, val list: List<Users> )
    : ArrayAdapter<Users>(mCtx,layoutResId,list){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textNIS = view.findViewById<TextView>(R.id.textNIS)
        val textNama = view.findViewById<TextView>(R.id.textNama)
        val btnUpdate = view.findViewById<ImageButton>(R.id.btnUpdate)
        val btnDelete = view.findViewById<ImageButton>(R.id.btnDelete)


        val user = list[position]

        textNIS.text = user.nis
        textNama.text = user.nama

        btnUpdate.setOnClickListener {
            showUpdateDialog(user)
        }

        btnDelete.setOnClickListener {
            Deleteinfo(user)
        }

        return view
    }

    private fun Deleteinfo(user: Users) {
        val progressDialog = ProgressDialog(context,
            R.style.Theme_MaterialComponents_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Deleting...")
        progressDialog.show()
        val mydatabase = FirebaseDatabase.getInstance().getReference("USERS")
        mydatabase.child(user.id).removeValue()
        Toast.makeText(mCtx,"Deleted!!", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }

    private fun showUpdateDialog(user: Users) {
        val builder = AlertDialog.Builder(mCtx)

        builder.setTitle("Update")

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.update, null)

        val textNIS = view.findViewById<EditText>(R.id.inputNIS)
        val textNama = view.findViewById<EditText>(R.id.inputNama)

        textNIS.setText(user.nis)
        textNama.setText(user.nama)

        builder.setView(view)

        builder.setPositiveButton("Update") { dialog, which ->

            val dbUsers = FirebaseDatabase.getInstance().getReference("USERS")

            val nis = textNIS.text.toString().trim()

            val nama = textNama.text.toString().trim()

            if (nis.isEmpty()){
                textNIS.error = "Tolong masukkan nis"
                textNIS.requestFocus()
                return@setPositiveButton
            }

            if (nama.isEmpty()){
                textNama.error = "Tolong masukkan nama"
                textNama.requestFocus()
                return@setPositiveButton
            }

            val user = Users(user.id,nis,nama)

            dbUsers.child(user.id).setValue(user).addOnCompleteListener {
                Toast.makeText(mCtx,"Updated",Toast.LENGTH_SHORT).show()
            }

        }

        builder.setNegativeButton("No") { dialog, which ->

        }

        val alert = builder.create()
        alert.show()

    }

}