package com.hua.note.config

/**
 * Created by Iaovy on 2020/3/17 11:07
 *@email Cymbidium@outlook.com
 */
class MessageEvent constructor(message: String, position: String) {
    var message: String = ""
    var position: String = ""

    init {
        this.message = message
        this.position = position
    }

}