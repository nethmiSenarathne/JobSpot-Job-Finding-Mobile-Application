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

class insert : AppCompatActivity() {

    private lateinit var user_id_task: EditText
    private lateinit var user_name_task: EditText
    private lateinit var email_task: EditText
    private lateinit var age_task: EditText
    private lateinit var gender_task: EditText

    private lateinit var updateButton1: Button
    private lateinit var updateButton2: Button

    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)
        user_id_task = findViewById(R.id.user_id_task)
        user_name_task = findViewById(R.id.user_name_task)
        email_task = findViewById(R.id.email_task)
        age_task = findViewById(R.id.age_task)
        gender_task = findViewById(R.id.gender_task)

        updateButton1 = findViewById(R.id.updateButton1)
        updateButton2 = findViewById(R.id.updateButton2)

        dbRef = FirebaseDatabase.getInstance().getReference("Job")

        /*updateButton2.setOnClickListener {
            val intent = Intent(this, fetching::class.java)
            startActivity(intent)
        }*/

        updateButton1.setOnClickListener {
            saveJobUser()
        }
    }

    private fun saveJobUser() {
        val UserIdTask = user_id_task.text.toString()
        val UserNameTask = user_name_task.text.toString()
        val EmailTask = email_task.text.toString()
        val AgeTask = age_task.text.toString()
        val GenderTask = gender_task.text.toString()

        if (UserIdTask.isEmpty()) {
            user_id_task.error = "Please Enter User Id"
        }

        if (UserNameTask.isEmpty()) {
            user_name_task.error = "Please Enter User Nmae"
        }

        if (EmailTask.isEmpty()) {
            email_task.error = "Please Enter Email"
        } else if (EmailTask.toIntOrNull() == null) {
            email_task.error = "Please Enter a Valid Email"
            return
        }

        if (AgeTask.isEmpty()) {
            age_task.error = "Please Enter Job/Task Details"
        }

        if (GenderTask.isEmpty()) {
            gender_task.error = "Please Enter Job/Task Publish Date"
        }

        dbRef.orderByChild("jobCode").equalTo(UserIdTask).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    user_id_task.error = "Job/Task Code Already Exists"
                    return
                } else {
                    val jobId = dbRef.push().key!!

                    val job = jobUserModel(UserIdTask,UserNameTask,EmailTask,AgeTask,GenderTask)

                    dbRef.child(jobId).setValue(job)
                        .addOnCompleteListener {
                            Toast.makeText(this@insert, "Data Added Successfully", Toast.LENGTH_LONG)
                                .show()

                            user_id_task.text.clear()
                            user_name_task.text.clear()
                            email_task.text.clear()
                            age_task.text.clear()
                            gender_task.text.clear()

                        }.addOnFailureListener { err ->
                            Toast.makeText(this@insert, "Error ${err.message}", Toast.LENGTH_LONG)
                                .show()
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@insert, "Error ${databaseError.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

}