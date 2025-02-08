package com.example.playmaker_list_out_of_if

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.Toolbar

class MainActivityB : AppCompatActivity() {

    private lateinit var settingsLauncher: ActivityResultLauncher<Intent>
    private lateinit var mainLayout: LinearLayout
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        mainLayout = findViewById(R.id.activity_main)
//        mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red));

        settingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupButtons()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun setupButtons() {
        val buttonIds = listOf(R.id.Button_Big1, R.id.Button_Big2, R.id.Button_Big3)

        for (buttonId in buttonIds) {
            findViewById<View>(buttonId).setOnClickListener {
                when (buttonId) {
                    R.id.Button_Big1 -> launchActivity(SearchActivityB::class.java)
                    R.id.Button_Big2 -> launchActivity(MediaLibraryActivityB::class.java)
                    R.id.Button_Big3 -> launchActivity(SettingsActivityB::class.java)
                }
            }
        }
    }

    private fun <T> launchActivity(activityClass: Class<T>) {
        val intent = Intent(this, activityClass)
        settingsLauncher.launch(intent)
    }
}