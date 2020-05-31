package com.smobile.premierleague.model

enum class PlayerPosition(val title: String?, val order: Int) {
    GOALKEEPER("Goalkeeper", 1),
    DEFENDER("Defender", 2),
    MIDFIELDER("Midfielder", 3),
    ATTACKER("Attacker", 4),
    UNKNOWN(null, 5);

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