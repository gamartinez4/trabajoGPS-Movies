package com.example.borrar.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class FilmsModel:RealmObject() {
    @PrimaryKey var id:Int? = null
    var title = ""
    var imgUrl = ""
    var description = ""
    var realeseDate = ""
}