package io.madrona.njord.layers

import io.madrona.njord.model.*

/**
 * Geometry Primitives: Point, Area
 *
 * Object: Crane
 *
 * Acronym: CRANES
 *
 * Code: 35
 */
class Cranes : Layerable() {

    private val lineColor = Color.CHBLK
    private val areaColor = Color.CHBRN

    override fun preTileEncode(feature: ChartFeature) {
        feature.pointSymbol(Sprite.CRANES01)
        feature.lineColor(lineColor)
        feature.areaColor(areaColor)
    }

    override fun layers(options: LayerableOptions) = sequenceOf(
        pointLayerFromSymbol(),
        areaLayerWithFillColor(areaColor),
        areaLayerWithPointSymbol(),
        lineLayerWithColor(color = lineColor)
    )
}
