package io.madrona.njord

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.cio.websocket.*
import io.ktor.jackson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import io.madrona.njord.endpoints.*
import io.madrona.njord.ext.addHandlers
import java.time.Duration


class ChartServerApp {
    fun serve() {
        embeddedServer(Netty, port = Singletons.config.port, host = Singletons.config.host) {
            install(ContentNegotiation) {
                jackson {
                    Singletons.objectMapper
                }
            }
            install(WebSockets) {
                pingPeriod = Duration.ofSeconds(15)
                timeout = Duration.ofSeconds(15)
                maxFrameSize = Long.MAX_VALUE
                masking = false
            }
            addHandlers(
                // curl http://localhost:9000/v1/about | jq
                AboutHandler(),

                // curl http://localhost:9000/v1/tile_json | jq
                TileJsonHandler(),

                // curl http://localhost:9000/v1/style/day/meters | jq
                StyleHandler(),

                // curl -v "http://localhost:9000/v1/content/fonts/Roboto Bold/0-255.pbf"
                // curl http://localhost:9000/v1/content/sprites/rastersymbols-day.json | jq
                // curl http://localhost:9000/v1/content/sprites/rastersymbols-day.png
                // http://localhost:9000/v1/content/upload.html
                StaticContentHandler(),

                // curl -v --form file="@${HOME}/Charts/ENC_ROOT.zip" 'http://localhost:8080/v1/enc_save'
                EncSaveHandler(),

                ChartWebSocketHandler(),

                //curl -v -H "Content-Type: application/json" --request POST  --data '{"name": "foo", "scale": 0, "file_name": "foo.000", "updated": "1979", "issued": "1980", "zoom": 1, "dsid_props": {}, "chart_txt": {}}' http://localhost:9000/v1/chart
                //curl -v -X DELETE 'http://localhost:9000/v1/chart?id=1'
                //curl -v 'http://localhost:9000/v1/chart?id=1 | jq'
                ChartHandler()
            )
        }.start(wait = true)
    }
}

fun main() {
    ChartServerApp().serve()
}
