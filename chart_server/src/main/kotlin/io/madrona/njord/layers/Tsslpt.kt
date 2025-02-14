package io.madrona.njord.layers

import io.madrona.njord.geo.symbols.floatValue
import io.madrona.njord.model.*

/**
 * Geometry Primitives: Area
 *
 * Object: Traffic Separation Scheme Lane part
 *
 * Acronym: TSSLPT
 *
 * Code: 148
 */
class Tsslpt : Layerable() {
    override fun preTileEncode(feature: ChartFeature) {
        feature.props.floatValue("ORIENT")?.let {
            feature.pointSymbol(Sprite.RCTLPT52)
        }
    }

    override fun layers(options: LayerableOptions) = sequenceOf(
        areaLayerWithPointSymbol(
            iconRotate = listOf("get", "ORIENT"),
            iconRotationAlignment = IconRotationAlignment.MAP,
        ),
    )
}
