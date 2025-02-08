package com.example.playmaker_list_out_of_if

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivityB : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var switchControl: SwitchMaterial
    private lateinit var settingsLayout: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private var isDarkTheme: Boolean = false
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false)

        AppCompatDelegate.setDefaultNightMode(if (isDarkTheme)
            AppCompatDelegate.MODE_NIGHT_YES else
            AppCompatDelegate.MODE_NIGHT_NO)

        enableEdgeToEdge()
        setContentView(R.layout.activity_settings2)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        initViews()
        setupClickListeners()
        handleWindowInsets()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun <T : View> find(id: Int): T {
        return findViewById(id) as T
    }

    private fun initViews() {
        switchControl = find(R.id.switch_control)
        backButton = find(R.id.backArrow)
        settingsLayout = find(R.id.activity_settings)

        switchControl.isChecked = isDarkTheme
    }

    private fun setupClickListeners() {
        switchControl.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("isDarkTheme", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(if (isChecked)
                AppCompatDelegate.MODE_NIGHT_YES else
                AppCompatDelegate.MODE_NIGHT_NO)
            isDarkTheme = isChecked
        }

        setupViewClickListener<ImageView>(R.id.backArrow) { if (!isDarkTheme) finish() }
        setupViewClickListener<TextView>(R.id.share) { shareApp() }
        setupViewClickListener<TextView>(R.id.group) { writeToSupport() }
        setupViewClickListener<TextView>(R.id.agreement) { openAgreement() }
        setupViewClickListener<TextView>(R.id.title) { if (isDarkTheme) finish() }
    }

    private fun <T : View> setupViewClickListener(viewId: Int, action: () -> Unit) {
        find<T>(viewId).setOnClickListener { action() }
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(settingsLayout) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun shareApp() {
        val shareMessage = getString(R.string.share_message)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }

    private fun writeToSupport() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:${getString(R.string.support_email)}?subject=${getString(R.string.support_subject)}&body=${getString(R.string.support_body)}")
        }
        startActivity(Intent.createChooser(emailIntent, null))

        if (emailIntent.resolveActivity(packageManager) != null) {
            startActivity(emailIntent)
        } else {
            Toast.makeText(this, "Нет приложения для отправки электронной почты", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openAgreement() {
        val agreementUrl = getString(R.string.agreement_url)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(agreementUrl))
        startActivity(browserIntent)
    }

}