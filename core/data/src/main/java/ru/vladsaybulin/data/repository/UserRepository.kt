package ru.vladsaybulin.data.repository

import javax.inject.Inject

class UserRepository @Inject constructor() {

    suspend fun getMyId(): Long? = 1042639L

}