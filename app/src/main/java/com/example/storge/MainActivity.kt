package com.example.storge
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.firebasestorage.MainActivity2
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {

    private val storageRef = Firebase.storage.reference
    private lateinit var uploadButton: Button
    private lateinit var download_bt: Button

    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uploadButton = findViewById(R.id.btn_upload)
        download_bt = findViewById(R.id.btn_download)
        progressBar = findViewById(R.id.bar_progress)
        statusText = findViewById(R.id.tv_status)
        download_bt.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        uploadButton.setOnClickListener {
            selectFile()

        }
    }

    private fun selectFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/pdf"
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun uploadFile(fileUri: Uri) {
        val fileName = fileUri.lastPathSegment!!
        val fileRef = storageRef.child(fileName)

        val uploadTask = fileRef.putFile(fileUri)

        uploadTask.addOnSuccessListener {
            progressBar.visibility = View.GONE
            statusText.text = "Upload successful"
        }.addOnFailureListener {
            progressBar.visibility = View.GONE
            statusText.text = "Upload failed"
        }.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
            progressBar.progress = progress.toInt()
        }
    }

    companion object {
        const val REQUEST_CODE = 101
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { fileUri ->
                progressBar.visibility = View.VISIBLE
                statusText.text = "Uploading..."
                uploadFile(fileUri)
            }
        }
    }

}