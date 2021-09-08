package com.example.borrar

import com.example.borrar.proxi.RetrofitController
import com.example.borrar.proxi.RetrofitStrings
import org.koin.dsl.module


val modKoin = module{
    single { RetrofitStrings() }
    single { RetrofitController(get()) }
    single { DialogPersonalized() }
}