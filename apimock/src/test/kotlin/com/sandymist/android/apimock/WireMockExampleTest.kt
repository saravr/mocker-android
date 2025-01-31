//package com.scribd.android.mocker.apimock
//
//import com.github.kittinunf.fuel.Fuel
//import com.github.tomakehurst.wiremock.client.WireMock.*
//import com.github.tomakehurst.wiremock.WireMockServer
//import com.github.tomakehurst.wiremock.core.WireMockConfiguration
//import junit.framework.TestCase.assertEquals
//import okhttp3.ResponseBody.Companion.toResponseBody
//
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//
//class WireMockExampleTest {
//    private lateinit var wireMockServer: WireMockServer
//
//    @Before
//    fun setup() {
//        wireMockServer = WireMockServer(WireMockConfiguration.options().port(8080))
//        wireMockServer.start()
//
//        wireMockServer.stubFor(get(urlEqualTo("/api/test"))
//            .willReturn(aResponse()
//                .withHeader("Content-Type", "application/json")
//                .withBody("{ \"message\": \"Hello, WireMock!\" }")))
//    }
//
//    @After
//    fun teardown() {
//        wireMockServer.stop()
//    }
//
//    @Test
//    fun testApiCall() {
//        val url = "http://localhost:8080/api/test"
//        val result = Fuel.get(url).responseString()
//        val response = result.second.data.toResponseBody().string()
//
//        assertEquals("{ \"message\": \"Hello, WireMock!\" }", response)
//    }
//}
