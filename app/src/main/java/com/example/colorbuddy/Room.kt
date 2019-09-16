package com.example.colorbuddy


class Room (val roomId: String, val name: String, val itemList:MutableList<Item>){
    constructor(): this("","", mutableListOf())
}