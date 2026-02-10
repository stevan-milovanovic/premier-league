package com.smobile.premierleague.model

enum class PlayerPosition(val title: String?, val order: Int) {
    GOALKEEPER("Goalkeeper", 0),
    DEFENDER("Defender", 1),
    MIDFIELDER("Midfielder", 2),
    ATTACKER("Attacker", 3),
    UNKNOWN(null, 4);

    companion object {
        fun mapToOrder(title: String?) = when (title) {
            GOALKEEPER.title -> GOALKEEPER.order
            DEFENDER.title -> DEFENDER.order
            MIDFIELDER.title -> MIDFIELDER.order
            ATTACKER.title -> ATTACKER.order
            else -> UNKNOWN.order
        }
    }
}