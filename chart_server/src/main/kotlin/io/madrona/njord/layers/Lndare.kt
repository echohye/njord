package io.madrona.njord.layers

import io.madrona.njord.model.*

class Lndare : Layerable {
    override val key = "LNDARE"

    override fun layers(options: LayerableOptions): Sequence<Layer> {
        return sequenceOf(
                Layer(
                        id = "${key}_fill",
                        type = LayerType.FILL,
                        sourceLayer = key,
                        filter = listOf(
                                Filters.all,
                                Filters.eqTypePolyGon
                        ),
                        paint = Paint(
                                fillColor = options.color.from("LANDA")
                        )
                )
        )
    }
}