package com.jmquinones.recipesapp.ui.fragments.settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.jmquinones.recipesapp.databinding.FragmentSettingsBinding
import com.jmquinones.recipesapp.models.SettingsModel
import com.jmquinones.recipesapp.ui.RecipesViewModel
import com.jmquinones.recipesapp.ui.dataStore
import com.jmquinones.recipesapp.utils.Constants.Companion.DARK_MODE_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val recipesViewModel by viewModels<RecipesViewModel>()
    private var firstTime: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        loadSettings()
        initListeners()
    }

    private fun loadSettings() {
        lifecycleScope.launch(Dispatchers.IO) {
            getSavedSettings().filter { firstTime }.collect { settings ->
                activity?.runOnUiThread{
                    binding.switchDarkMode.isChecked = settings.darkMode
                }
                firstTime = false
            }
        }
    }

    private fun initListeners() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, flag ->
            changeTheme(flag)
            lifecycleScope.launch(Dispatchers.IO) {
                saveTheme(flag)
            }
        }
    }

    private fun changeTheme(flag: Boolean) {
        if (flag) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        //delegate.applyDayNight()
    }

    private suspend fun saveTheme(newFlag: Boolean) {
        val darkThemeKey = booleanPreferencesKey(DARK_MODE_KEY)
        requireContext().dataStore.edit { settings ->
            settings[darkThemeKey] = newFlag
        }
    }

    private fun getSavedSettings(): Flow<SettingsModel> {
        return requireContext().dataStore.data.map { preferences ->
            SettingsModel(
                darkMode = preferences[booleanPreferencesKey(DARK_MODE_KEY)] ?: false,

                )
        }

    }

}