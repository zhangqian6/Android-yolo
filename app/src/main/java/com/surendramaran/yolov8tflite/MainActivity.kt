package com.surendramaran.yolov8tflite

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp.database.Database
import com.google.android.material.textfield.TextInputLayout


class MainActivity : AppCompatActivity() {
    //定义数据库和其他变量
    private lateinit var initDatabase: Database
    private lateinit var user: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var loginbutton: Button
    private lateinit var register: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        //创建数据库实例
        initDatabase = Database(this)
        user = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tilEmail)
        password = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tilPassword)
        loginbutton = findViewById<Button>(R.id.btnLogin)
        register = findViewById<TextView>(R.id.tvRegister)


        //登录按钮绑定事件
        loginbutton.setOnClickListener{
            val inputuser = user.editText?.text.toString()
            val inputpassword = password.editText?.text.toString()


            //查询数据库中是否有该数据
            val state = quary_data(inputuser,inputpassword)
            //数据库中有该数据   页面跳转
            if(state){
                val intent = Intent(this, DataAnalysis::class.java)
                startActivity(intent)
            }
        }

        //注册按钮绑定事件
        register.setOnClickListener{
            val intent = Intent(this,Activity_Register::class.java)
            startActivity(intent)
        }

        //删除一个数据
        /*val userIdToDelete = 2
        val rowsDeleted = initDatabase.deleteUserById(userIdToDelete)

        if(rowsDeleted >0){
            //删除成功
        }*/
    }



    //查询数据
    public fun quary_data(inputuser: String, inputpassword:String): Boolean {
        // 查询数据
        val cursor = initDatabase.readableDatabase.query(
            Database.TABLE_NAME,
            arrayOf(Database.COLUMN_ID, Database.COLUMN_NAME, Database.COLUMN_DEPARTMENT),
            null, null, null, null, null
        )
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_NAME))
            val department = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_DEPARTMENT))
            // 处理查询结果
            //println("Employee: $id, $name, $department")
            if(name == inputuser && department==inputpassword){
                Toast.makeText(this,"恭喜你，登录成功", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        cursor.close()
        Toast.makeText(this,"请先注册", Toast.LENGTH_SHORT).show()
        return false
    }

}