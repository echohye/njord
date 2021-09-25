package io.madrona.njord.endpoints

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.madrona.njord.db.ChartDao
import io.madrona.njord.db.GeoJsonDao
import io.madrona.njord.ext.KtorHandler
import io.madrona.njord.ext.letTwo
import io.madrona.njord.model.FeatureInsert
import mil.nga.sf.geojson.GeoJsonObject

class GeoJsonHandler(
    private val geoJsonDao: GeoJsonDao = GeoJsonDao(),
    private val chartDao: ChartDao = ChartDao(),
) : KtorHandler {
    override val route = "/v1/geojson"

    override suspend fun handlePost(call: ApplicationCall) {
        val geo = call.receive<GeoJsonObject>()
        letTwo(
            call.request.queryParameters["chart_id"]?.toLongOrNull(),
            call.request.queryParameters["layer_name"]
        ) { id, name ->
            chartDao.findAsync(id).await()?.let { chart ->
                FeatureInsert(
                    name,
                    chart,
                    geo
                )
            }
        }?.let { fi ->
            geoJsonDao.insertAsync(fi).await()?.let { it to fi }
        }?.let {
            if (it.first > 0) {
                call.respond(HttpStatusCode.OK, it)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        } ?: call.respond(HttpStatusCode.BadRequest)
    }
}
