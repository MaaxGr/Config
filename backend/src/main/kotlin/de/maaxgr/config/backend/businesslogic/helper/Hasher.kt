package de.maaxgr.config.backend.businesslogic.helper

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

interface Hasher {
    fun hash(string: String): String
    fun checkAgainstHash(clearText: String, hashed: String): Boolean
}

class HasherImpl: Hasher {

    private val encoder = BCryptPasswordEncoder()

    override fun hash(string: String): String {
        return encoder.encode(string)
    }

    override fun checkAgainstHash(clearText: String, hashed: String): Boolean {
        return encoder.matches(clearText, hashed)
    }

}
