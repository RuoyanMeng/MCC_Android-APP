package com.example.exercise03_listview

import java.io.Serializable


class Model {
    lateinit var photo:String
    lateinit var author:String

    constructor(photo: String,author:String) {
        this.photo = photo
        this.author = author
    }

    constructor()
}