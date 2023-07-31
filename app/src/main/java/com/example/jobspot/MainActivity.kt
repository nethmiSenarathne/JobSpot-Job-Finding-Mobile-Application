package com.example.jobspot

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.EditTextPreference
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.jobspot.R
import android.widget.Spinner
import com.google.firebase.database.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var user_id_job: EditText
    private lateinit var User_Name_job: EditText
    private lateinit var email_job: EditText
    private lateinit var age_job: EditText
    private lateinit var gender_job: EditText

    private lateinit var updateButton1: Button
    private lateinit var updateButton2: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        user_id_job = findViewById(R.id.user_id_job)
        User_Name_job = findViewById(R.id.User_Name_job)
        email_job = findViewById(R.id.email_job)
        age_job = findViewById(R.id.age_job)
        gender_job = findViewById(R.id.gender_job)

        updateButton1 = findViewById(R.id.updateButton)
        updateButton2 = findViewById(R.id.updateButton2)

        dbRef = FirebaseDatabase.getInstance().getReference("Job")

        /*updateButton2.setOnClickListener {
            val intent = Intent(this, fetching::class.java)
            startActivity(intent)
        }*/

        updateButton1.setOnClickListener {
            saveJobData()
        }
    }

    private fun saveJobData() {
        val UserIdJob = user_id_job.text.toString()
        val UserNameJob = User_Name_job.text.toString()
        val EmailJob = email_job.text.toString()
        val AgeJob = age_job.text.toString()
        val GenderJob = gender_job.text.toString()

        if (UserIdJob.isEmpty()) {
            user_id_job.error = "Please Enter User Id"
        }

        if (UserNameJob.isEmpty()) {
            User_Name_job.error = "Please Enter User Nmae"
        }

        if (EmailJob.isEmpty()) {
            email_job.error = "Please Enter Email"
        } else if (EmailJob.toIntOrNull() == null) {
            email_job.error = "Please Enter a Valid Email"
            return
        }

        if (AgeJob.isEmpty()) {
            age_job.error = "Please Enter Job/Task Details"
        }

        if (GenderJob.isEmpty()) {
            gender_job.error = "Please Enter Job/Task Publish Date"
        }

        dbRef.orderByChild("jobCode").equalTo(UserIdJob).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    user_id_job.error = "User Id Already Exists"
                    return
                } else {
                    val jobId = dbRef.push().key!!

                    val job = jobUserModel(UserIdJob,UserNameJob,EmailJob,AgeJob,GenderJob)

                    dbRef.child(jobId).setValue(job)
                        .addOnCompleteListener {
                            Toast.makeText(this@MainActivity, "Data Added Successfully", Toast.LENGTH_LONG)
                                .show()

                            user_id_job.text.clear()
                            User_Name_job.text.clear()
                            email_job.text.clear()
                            age_job.text.clear()
                            gender_job.text.clear()

                        }.addOnFailureListener { err ->
                            Toast.makeText(this@MainActivity, "Error ${err.message}", Toast.LENGTH_LONG)
                                .show()
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error ${databaseError.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}