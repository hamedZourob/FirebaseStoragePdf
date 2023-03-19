package com.example.firebasestorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.storge.R
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class MainActivity2 : AppCompatActivity() {
    private val storage = FirebaseStorage.getInstance()
    private val filesList = mutableListOf<StorageReference>()
    private lateinit var progressBar: ProgressBar
    private lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        FirebaseApp.initializeApp(this)
        getAllFiles()
    }
    private fun getAllFiles() {
        progressBar  = findViewById(R.id.proBar)
        listView = findViewById(R.id.listview)

        val storageRef = storage.reference
        val listAllTask = storageRef.listAll()


        progressBar.visibility = View.VISIBLE

        listAllTask.addOnSuccessListener { result ->

            filesList.addAll(result.items)

            val adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                filesList.map { it.name })
            listView.adapter = adapter

            listView.setOnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position) as String
                val fileRef = storageRef.child(selectedItem)

                progressBar.visibility = View.VISIBLE

                 val file = File(getExternalFilesDir(null), selectedItem)
                fileRef.getFile(file).addOnSuccessListener {

                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "File  successfully", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {

                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "File  Failed", Toast.LENGTH_SHORT).show()
                }.addOnProgressListener { taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                    progressBar.progress = progress.toInt()
                }
            }

            progressBar.visibility = View.GONE
        }.addOnFailureListener {

        }
    }


}