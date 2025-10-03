package ru.gorbulevsv.composeukazania

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class Storage(private val context: Context) {
   companion object {
      private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")


      private val FONT = stringPreferencesKey("font")
      private val FONT_SIZE = intPreferencesKey("fontSize")
      private val LINE_HEIGHT = intPreferencesKey("lineHeight")
      private val PADDING = intPreferencesKey("padding")
      private val IS_BOTTOM_PANEL_SHOW = booleanPreferencesKey("isBottomPanelShow")
   }

   var fontGet: Flow<String> = context.dataStore.data.map {
      it[FONT] ?: "PT Sans"
   }

   suspend fun fontSet(value: String) {
      context.dataStore.edit {
         it[FONT] = value
      }
   }

   var fontSizeGet: Flow<Int> = context.dataStore.data.map {
      it[FONT_SIZE] ?: 20
   }

   suspend fun fontSizeSet(value: Int) {
      context.dataStore.edit {
         it[FONT_SIZE] = value
      }
   }

   var lineHeightGet: Flow<Int> = context.dataStore.data.map {
      it[LINE_HEIGHT] ?: 27
   }

   suspend fun lineHeightSet(value: Int) {
      context.dataStore.edit {
         it[LINE_HEIGHT] = value
      }
   }

   var paddingGet: Flow<Int> = context.dataStore.data.map {
      it[PADDING] ?: 14
   }

   suspend fun paddingSet(value: Int) {
      context.dataStore.edit {
         it[PADDING] = value
      }
   }

   var isBottomPanelShowGet: Flow<Boolean> = context.dataStore.data.map {
      it[IS_BOTTOM_PANEL_SHOW] ?: false
   }

   suspend fun isBottomPanelShowSet(value: Boolean) {
      context.dataStore.edit {
         it[IS_BOTTOM_PANEL_SHOW] = value
      }
   }
}
