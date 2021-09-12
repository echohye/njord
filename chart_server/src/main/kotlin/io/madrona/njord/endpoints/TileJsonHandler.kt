package io.madrona.njord.endpoints

import io.ktor.application.*
import io.ktor.response.*
import io.madrona.njord.ChartsConfig
import io.madrona.njord.ext.KtorHandler
import io.madrona.njord.model.TileJson

class TileJsonHandler(
        private val config: ChartsConfig,
) : KtorHandler {
    override val route = "/v1/tile_json"

    override suspend fun handle(call: ApplicationCall) {
        call.respond(
            TileJson(
                tiles = listOf(
                    "${config.externalBaseUrl}/v1/tile/{z}/{x}/{y}"
                )
            )
        )
    }
}
