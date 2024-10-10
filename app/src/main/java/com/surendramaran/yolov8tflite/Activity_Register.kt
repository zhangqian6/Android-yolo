package com.surendramaran.yolov8tflite

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp.database.Database


class Activity_Register : AppCompatActivity() {
    private lateinit var initDatabase: Database
    private lateinit var usernameEditText1 : EditText
    private lateinit var emailEditText : EditText
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText1 : EditText

    // 注册按钮
    private lateinit var registerButton : Button
    // 返回按钮
    private lateinit var backButton1 : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        initDatabase = Database(this)
        usernameEditText1 = findViewById<EditText>(R.id.usernameEditText)
        emailEditText = findViewById<EditText>(R.id.emailEditText)
        phoneEditText = findViewById<EditText>(R.id.phoneEditText)
        passwordEditText1 = findViewById<EditText>(R.id.passwordEditText)

        // 注册按钮和返回按钮
        registerButton = findViewById<Button>(R.id.registerButton)
        backButton1 = findViewById<Button>(R.id.backButton)


        //注册按钮事件
        registerButton.setOnClickListener(){
            val user = usernameEditText1.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val password = passwordEditText1.text.toString()

            // 插入数据
            if (user.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty()) {
                val id = initDatabase.insertUser(user, email, phone, password)
                if (id != -1L) {
                    Toast.makeText(this, "User inserted with ID: $id", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error inserting user", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a valid name and age", Toast.LENGTH_SHORT).show()
            }
        }

        //返回按钮事件
        backButton1.setOnClickListener(){
            finish()// 关闭当前Activity并返回到前一个Activity
        }

    }
}