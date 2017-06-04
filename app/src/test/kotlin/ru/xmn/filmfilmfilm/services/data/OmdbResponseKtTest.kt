package ru.xmn.filmfilmfilm.services.data

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by USER on 26.05.2017.
 */
class OmdbResponseKtTest {
    @Test
    fun toRealm() {
        val response = OmdbResponse(listOf(Rating("1", "2"), Rating("1", "2")), "2")
        val realm = response.toRealm()
        realm.toModel()
        Assert.assertEquals(response, realm.toModel())
    }
}