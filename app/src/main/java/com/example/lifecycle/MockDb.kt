package com.example.lifecycle


class MockDb {

    companion object {
        var isLoggedIn = false

        private val username = "user"
        private val password = "pass"

        fun checkLoggIn(username: String, password: String): Boolean {
            return if (username == this.username && password == this.password) {
                isLoggedIn = true
                true
            } else {
                false
            }
        }
    }
}
