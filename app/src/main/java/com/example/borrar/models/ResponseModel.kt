package com.example.borrar.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class ResponseModel: RealmObject(){
    var tiempo: String = ""
    var distancia: String = ""
}