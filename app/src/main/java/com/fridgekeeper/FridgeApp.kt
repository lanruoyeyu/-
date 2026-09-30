package com.fridgekeeper

import android.app.Application
import com.fridgekeeper.data.local.AppDatabase
import com.fridgekeeper.data.repository.DigitalRepository
import com.fridgekeeper.data.repository.FridgeRepository
import com.fridgekeeper.data.repository.PetRepository
import com.fridgekeeper.data.repository.StatsRepository

class FridgeApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val repository: FridgeRepository by lazy { FridgeRepository.getRepository(database) }
    val digitalRepository: DigitalRepository by lazy { DigitalRepository(database.digitalItemDao()) }
    val petRepository: PetRepository by lazy {
        PetRepository(database.petCategoryDao(), database.petItemDao())
    }
    val statsRepository: StatsRepository by lazy {
        StatsRepository(
            database.itemDao(),
            database.digitalItemDao(),
            database.petCategoryDao(),
            database.petItemDao(),
            database.mapMarkerDao()
        )
    }
}
