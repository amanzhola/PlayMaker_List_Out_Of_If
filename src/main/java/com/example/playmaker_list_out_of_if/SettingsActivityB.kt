package com.example.playmaker_list_out_of_if

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup
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
import androidx.core.view.children
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlin.random.Random

class SettingsActivityB : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var switchControl: SwitchMaterial
    private lateinit var settingsLayout: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private var isDarkTheme: Boolean = false
    private lateinit var toolbar: Toolbar
    private val colors = arrayOf(
        Color.RED,
        Color.GREEN,
        Color.BLUE,
        Color.WHITE,
        Color.YELLOW,
        Color.CYAN,
        Color.MAGENTA,
        Color.BLACK
    )

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
        setupViewClickListener<TextView>(R.id.phone) { makePhoneCall()}
        setupViewClickListener<TextView>(R.id.sms) { sendSms() }
        setupViewClickListener<TextView>(R.id.textColor) { changeColor(true) }
        setupViewClickListener<TextView>(R.id.backgroundColor) { changeColor(false) }
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

    private fun makePhoneCall() {
        val phoneNumber = "tel:+7000000"
        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse(phoneNumber)
        }
        startActivity(Intent.createChooser(callIntent, null))

        if (callIntent.resolveActivity(packageManager) != null) {
            startActivity(callIntent)
        } else {
            Toast.makeText(this, "Нет приложения для совершения телефонных звонков", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendSms() {
        val smsNumber = "+70000000"
        val smsMessage = "Тестовое сообщение"
        val smsUri = Uri.parse("smsto:$smsNumber")
        val smsIntent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
            putExtra("sms_body", smsMessage)
        }
        startActivity(Intent.createChooser(smsIntent, null))

        if (smsIntent.resolveActivity(packageManager) != null) {
            startActivity(smsIntent)
        } else {
            Toast.makeText(this, "Нет приложения для отправки SMS", Toast.LENGTH_SHORT).show()
        }
    }

    private fun changeColor(isTextColor: Boolean){
        val randomColor = colors[Random.nextInt(colors.size)]
        if (isTextColor) {
            settingsLayout.changeTextColor(randomColor)
            backButton.imageTintList = ColorStateList.valueOf(randomColor)
        } else {
            settingsLayout.setBackgroundColor(randomColor)
        }
    }

    private fun ViewGroup.changeTextColor(textColor:Int){
        children.forEach { view: View ->
            if (view is TextView ){
                view.run { setTextColor(textColor) }
            } else if (view is ViewGroup) {
                view.run { changeTextColor(textColor) }
            }
        }
    }

}