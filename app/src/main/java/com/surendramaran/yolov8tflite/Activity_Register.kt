package com.surendramaran.yolov8tflite

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp.database.Database
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit



class Activity_Register : AppCompatActivity() {
    private lateinit var initDatabase: Database
    private lateinit var usernameEditText1 : EditText
    private lateinit var passwordEditText1 : EditText
    private lateinit var registerButton : Button
    private lateinit var backButton1 : Button
    private lateinit var sendCodeButton : Button
    private lateinit var auth: FirebaseAuth
    private lateinit var phoneEditText:EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        initDatabase = Database(this)
        usernameEditText1 = findViewById<EditText>(R.id.usernameEditText)
        passwordEditText1 = findViewById<EditText>(R.id.passwordEditText)
        registerButton = findViewById<Button>(R.id.registerButton)
        backButton1 = findViewById<Button>(R.id.backButton)

        phoneEditText = findViewById<EditText>(R.id.phoneEditText)
        sendCodeButton = findViewById<Button>(R.id.sendCodeButton)

        //注册按钮事件
        registerButton.setOnClickListener(){
            val user = usernameEditText1.text.toString()
            val password = passwordEditText1.text.toString()
            // 插入数据
            if (user.isNotEmpty() && password.isNotEmpty()) {
                val id = initDatabase.insertUser(user, password)
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

        //发送验证码事件
        sendCodeButton.setOnClickListener(){
            //初始化验证码
            //

            val phone = phoneEditText.text.toString()
            //向该手机号发送验证码
            if(phone.isNotEmpty()){
                sendVerificationCode(phone)
            } else {
                Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun sendVerificationCode(phoneNumber: String){
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)       // 手机号码
            .setTimeout(60L, TimeUnit.SECONDS) // 超时时间
            .setActivity(this)                 // 活动实例
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // 自动验证成功时调用
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // 验证失败时调用
                    Log.e("Verification Failed", e.message ?: "Error")
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    // 验证码发送成功时调用
                    Log.d("Code Sent", "Verification ID: $verificationId")
                    // 保存verificationId和resendToken以供以后使用
                }
            })          // 回调
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 验证成功，登录或注册
                    Toast.makeText(this@Activity_Register, "验证成功: ", Toast.LENGTH_LONG).show()
                    val user = task.result?.user
                } else {
                    // 验证失败
                    Toast.makeText(this@Activity_Register, "验证失败: ", Toast.LENGTH_LONG).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // 验证码错误
                    }
                }
            }
    }

    private fun verifyCode(code: String, verificationId: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }
}