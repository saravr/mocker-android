package com.scribd.android.mocker.apimock

import com.github.tomakehurst.wiremock.common.FileSource
import com.github.tomakehurst.wiremock.extension.Parameters
import com.github.tomakehurst.wiremock.extension.ResponseTransformer
import com.github.tomakehurst.wiremock.http.Request
import com.github.tomakehurst.wiremock.http.Response

class CustomResponseTransformer : ResponseTransformer() {
    override fun getName(): String {
        return "partial-body-transformer"
    }

    override fun transform(
        request: Request?,
        response: Response?,
        files: FileSource?,
        parameters: Parameters?
    ): Response {
        val originalBody = response?.bodyAsString ?: run {
            return Response.Builder()
                .status(404)
                .body("No response found".toByteArray())
                .build()
        }

        val modifiedBody = originalBody.replace("originalValue", "newValue")

        return Response.Builder.like(response)
            .but()
            .body(modifiedBody)
            .build()
    }

    override fun applyGlobally(): Boolean {
        return false // Only apply to the specific stub
    }
}
