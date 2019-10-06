package com.example.exercise03

class Model{
    lateinit var photo:String
    lateinit var author:String

    constructor(photo: String,author:String) {
        this.photo = photo
        this.author = author
    }

    constructor()
}