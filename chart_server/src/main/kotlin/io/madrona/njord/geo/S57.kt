package io.madrona.njord.geo

import com.fasterxml.jackson.databind.ObjectMapper
import io.madrona.njord.Singletons
import io.madrona.njord.geo.symbols.*
import io.madrona.njord.model.ChartInsert
import io.madrona.njord.util.ZFinder
import mil.nga.sf.geojson.FeatureCollection
import org.gdal.gdal.Dataset
import org.gdal.gdal.gdal
import org.gdal.ogr.Feature
import org.gdal.ogr.Geometry
import org.gdal.ogr.Layer
import org.gdal.osr.SpatialReference
import mil.nga.sf.geojson.FeatureConverter
import org.gdal.ogr.ogrConstants.*
import java.io.File
import java.lang.RuntimeException

class S57(
    val file: File,
    private val inLayers: Set<String>? = null,
    private val exLayers: Set<String>? = null,
    private val sr4326: SpatialReference = Singletons.wgs84SpatialRef,
    private val objectMapper: ObjectMapper = Singletons.objectMapper,
    private val zFinder: ZFinder = Singletons.zFinder,
) {
    private val dataSet: Dataset = gdal.OpenEx(file.absolutePath)

    val layerGeoJson: Map<String, FeatureCollection> by lazy {
        layerGeoJsonSequence().toMap()
    }

    fun findLayer(name: String): FeatureCollection? {
        return dataSet.GetLayer(name)?.featureCollection()
    }

    fun chartInsertInfo(): ChartInsert? {
        val chartTxt = file.parentFile.listFiles { _: File, name: String ->
            name.endsWith(".TXT", true)
        }?.map {
            it.name to it.readText()
        }?.toMap() ?: emptyMap()

        return findLayer("DSID")?.features?.firstOrNull()?.s57Props()?.let { props ->
            props.intValue("DSPM_CSCL")?.let { scale ->
                ChartInsert(
                    name = props.stringValue("DSID_DSNM") ?: "",
                    scale = scale,
                    fileName = file.name,
                    updated = props.stringValue("DSID_UADT") ?: "",
                    issued = props.stringValue("DSID_ISDT") ?: "",
                    zoom = zFinder.findZoom(scale),
                    dsidProps = props,
                    chartTxt = chartTxt
                )
            }
        }
    }

    fun layerGeoJsonSequence(): Sequence<Pair<String, FeatureCollection>> {
        return dataSet.layers().mapNotNull { layer ->
            val name = layer.GetName()
            if ((inLayers == null || inLayers.contains(name)) && exLayers?.contains(name) != true) {
                name to layer.featureCollection()
            } else {
                null
            }
        }
    }

    /**
     * export OGR_S57_OPTIONS="RETURN_PRIMITIVES=OFF,RETURN_LINKAGES=OFF,LNAM_REFS=ON:UPDATES:APPLY,SPLIT_MULTIPOINT:ON,RECODE_BY_DSSI:ON:ADD_SOUNDG_DEPTH=ON"
     * ogr2ogr -t_srs 'EPSG:4326' -f GeoJSON $(pwd)/ogr_SOUNDG.json $(pwd)/US5WA22M.000 SOUNDG
     * ogr2ogr -t_srs 'EPSG:4326' -f GeoJSON $(pwd)/ogr_BOYSPP.json $(pwd)/US3WA46M.000 BOYSPP
     * ogr2ogr -t_srs 'EPSG:4326' -f GeoJSON $(pwd)/ogr_DSID.json $(pwd)/US5WA22M.000 DSID
     */
    fun renderGeoJson(
        outDir: File = file.parentFile,
        msg: (String) -> Unit
    ) {
        layerGeoJsonSequence().forEach {
            val name = "${it.first}.json"
            msg(name)
            objectMapper.writeValue(File(outDir, name), it.second)
        }
    }

    private fun Feature.geoJsonFeature(): mil.nga.sf.geojson.Feature? {
        return GetGeometryRef()?.let { geom ->
            geom.geoJson()?.let { geoJson ->
                mil.nga.sf.geojson.Feature().also { feat ->
                    feat.geometry = FeatureConverter.toGeometry(geoJson)
                    feat.properties = fields().apply {
                        intValue("SCAMIN")?.takeIf { it > 0 }?.let {
                            put("MINZ", zFinder.findZoom(it))
                        }
                        intValue("SCAMAX")?.takeIf { it > 0 }?.let {
                            put("MAXZ", zFinder.findZoom(it))
                        }
                    }
                }
            }
        }?: run {
            fields().takeIf { it.isNotEmpty() }?.let { fields ->
                mil.nga.sf.geojson.Feature().also {
                    it.geometry = null
                    it.properties = fields
                }
            }
        }
    }

    private fun Feature.fields(): S57Prop {
        return (0 until GetFieldCount()).asSequence().map { id ->
            GetFieldDefnRef(id).let {
                val name = it.GetName()
                name to value(it.GetFieldType(), id)
            }
        }.toMap().toMutableMap()
    }

    private fun Feature.value(type: Int, id: Int): Any? {
        return when (type) {
            OFTInteger -> GetFieldAsInteger(id)
            OFTIntegerList -> GetFieldAsIntegerList(id)
            OFTReal -> GetFieldAsDouble(id)
            OFTRealList -> GetFieldAsDoubleList(id)
            OFTDate,
            OFTTime,
            OFTDateTime,
            OFTWideString,
            OFTString -> GetFieldAsString(id)
            OFTStringList,
            OFTWideStringList -> GetFieldAsStringList(id)
            OFTBinary -> GetFieldAsBinary(id)
            OFTInteger64 -> GetFieldAsInteger64(id)
            OFTInteger64List -> GetFieldAsStringList(id)
            else -> null
        }
    }

    private fun Layer.featureCollection(): FeatureCollection {
        return FeatureCollection().also { fc ->
            val name = GetName()
            fc.addFeatures(features().mapNotNull { feat ->
                feat.geoJsonFeature()?.apply {
                    when (name) {
                        "SOUNDG" -> addSounding()
                        "BOYSPP" -> addBoyShp()
                        "LIGHTS" -> {
                        } //todo:
                    }
                }
            }.toList())
        }
    }

    private fun Layer.features(): Sequence<Feature> {
        ResetReading()
        return (0 until GetFeatureCount()).asSequence().mapNotNull {
            GetNextFeature()
        }
    }

    private fun Dataset.layers(): Sequence<Layer> {
        ResetReading()
        return (0 until GetLayerCount()).asSequence().mapNotNull {
            GetLayer(it)
        }
    }


    private fun Geometry.wgs84() {
        if (GetSpatialReference()?.IsSameGeogCS(sr4326) != 1) {
            val result = TransformTo(sr4326)
            if (result != 0) {
                throw RuntimeException("failed to transform Geometry to wgs84")
            }
        }
    }

    private fun Geometry.geoJson(): String? {
        wgs84()
        return ExportToJson()
    }

    companion object {
        /**
         * https://gdal.org/drivers/vector/s57.html
         */
        private const val OGR_S57_OPTIONS_K = "OGR_S57_OPTIONS"

        /**
         * https://gdal.org/drivers/vector/s57.html#s-57-export
         */
        private const val OGR_S57_EXPORT_MIN = "RETURN_PRIMITIVES=OFF,RETURN_LINKAGES=OFF,LNAM_REFS=ON"

        private const val OGR_S57_OPTIONS_V =
            "${OGR_S57_EXPORT_MIN}:UPDATES:APPLY,SPLIT_MULTIPOINT:ON,RECODE_BY_DSSI:ON:ADD_SOUNDG_DEPTH=ON"

        init {
            gdal.AllRegister()
            gdal.SetConfigOption(OGR_S57_OPTIONS_K, OGR_S57_OPTIONS_V)
        }
    }
}
