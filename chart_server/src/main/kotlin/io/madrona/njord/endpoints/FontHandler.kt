package io.madrona.njord.endpoints

import com.willkamp.vial.api.EndPointHandler
import com.willkamp.vial.api.Request
import io.madrona.njord.resourceBytes
import io.netty.handler.codec.http.HttpResponseStatus

private const val fontStack = "fontstack"
private const val range = "range"

class FontHandler : EndPointHandler {
    override val route = "/v1/font/:$fontStack/:$range"

    override fun handle(request: Request) {
        request.pathParam(fontStack)?.let { fs ->
            request.pathParam(range)?.let { rng ->
                resourceBytes("/fonts/$fs/$rng")?.let {
                    request.respondWith { builder ->
                        builder.setBodyData("application/x-protobuf", it)
                    }
                }
            }
        } ?: run {
            request.respondWith { builder ->
                builder.setBodyText("invalid request")
                        .setStatus(HttpResponseStatus.BAD_REQUEST)
            }
        }
    }


}