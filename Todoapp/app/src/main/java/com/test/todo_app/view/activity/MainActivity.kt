package com.test.todo_app.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.test.todo_app.R
import com.test.todo_app.view.view_model.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment?
        navHostFragment?.navController
        ViewModelProvider(this)[TasksViewModel::class.java]
    }
}