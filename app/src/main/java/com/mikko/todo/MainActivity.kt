package com.mikko.todo

import android.content.Context
import android.os.Bundle
import android.util.Log

import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu

import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeActivity
import com.dolatkia.animatedThemeManager.ThemeManager
import com.mikko.todo.darkmode.DarkTheme
import com.mikko.todo.darkmode.LightTheme
import com.mikko.todo.darkmode.MyAppTheme
import com.mikko.todo.databinding.ActivityMainBinding


class MainActivity :  ThemeActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var isDark = false
    override fun getStartTheme(): AppTheme = LightTheme()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.darkMode.setOnClickListener {
            val theme = if (isDark) LightTheme() else DarkTheme()
            ThemeManager.instance.changeTheme(theme, it)
            isDark = !isDark
            //Snackbar.make(it, "dark mode button clicked", Snackbar.LENGTH_LONG).show()
        }
        /*binding.darkMode.setOnLongClickListener {
            ThemeManager.instance.changeTheme(DarkTheme(), it)
            Snackbar.make(it, "dark mode button clicked", Snackbar.LENGTH_LONG).show()
            return@setOnLongClickListener true
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }*/

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun syncTheme(appTheme: AppTheme) {
        val myAppTheme = appTheme as MyAppTheme
        val prefs = binding.root.context.getSharedPreferences("app_theme", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("apptheme",appTheme.id())
            .apply()
        Log.d("SAVE", "SUCCESS")
        // set background color
        if (appTheme.id() != 0){
            binding.darkMode.setImageResource(R.drawable.baseline_dark_mode_24_dark)
        } else {
            binding.darkMode.setImageResource(R.drawable.baseline_dark_mode_24_light)
        }
        binding.root.setBackgroundColor(myAppTheme.firstActivityBackgroundColor(this))
        binding.toolbar.setBackgroundColor(myAppTheme.firstActivityBackgroundColor(this))
        binding.toolbar.setTitleTextColor(myAppTheme.firstActivityTextColor(this))


        //set text color
        //binding.text.setTextColor(myAppTheme.activityTextColor(this))

        // set icons color
        //binder.share.setColorFilter(myAppTheme.firstActivityIconColor(this))
        //binder.gift.setColorFilter(myAppTheme.firstActivityIconColor(this))
    }
}