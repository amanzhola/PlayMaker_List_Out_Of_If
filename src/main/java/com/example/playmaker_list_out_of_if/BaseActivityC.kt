package com.example.playmaker_list_out_of_if

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar

open class BaseActivityC : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyNightMode()
    }

    protected fun setupToolbar(toolbarId: Int) {
        toolbar = findViewById(toolbarId)
        setSupportActionBar(toolbar)
        toolbar.contentInsetStartWithNavigation = dipToPx(8)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun dipToPx(dp: Int): Int {
        val displayMetrics = resources.displayMetrics
        return dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.dropdown -> {
                showDropdown()
                true
            }
            R.id.filter_list -> {
                showFilterList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun applyNightMode() {
        val darkModeEnabled = getSharedPreferences("AppSettings", MODE_PRIVATE)
            .getBoolean("DarkMode", false)

        AppCompatDelegate.setDefaultNightMode(
            if (darkModeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun showDropdown() {
        val dropdownDialog = AlertDialog.Builder(this)
            .setTitle("Выберите элемент")
            .setItems(arrayOf("Элемент 1", "Элемент 2", "Элемент 3")) { _, which ->
                when (which) {
                    0 -> Toast.makeText(this, "Элемент 1 chosen", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(this, "Элемент 2 chosen", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this, "Элемент 3 chosen", Toast.LENGTH_SHORT).show()
                }
            }
            .create()
        dropdownDialog.show()
    }

    private fun showFilterList() {

        Toast.makeText(this, "Фильтр запущен", Toast.LENGTH_SHORT).show()

        val filterListDialog = AlertDialog.Builder(this)
            .setTitle("Фильтр")
            .setMessage("Вы хотите фильтровать данные?")
            .setPositiveButton("Да") { _, _ ->
                Toast.makeText(this, "Фильтр активирован", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Нет") { _, _ ->
                Toast.makeText(this, "Фильтр отменен", Toast.LENGTH_SHORT).show()
            }
            .create()
        filterListDialog.show()
    }
}