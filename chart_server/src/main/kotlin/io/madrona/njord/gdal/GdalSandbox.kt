/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package io.madrona.njord.gdal

import org.gdal.gdal.gdal.*
import org.gdal.ogr.Feature
import org.gdal.osr.osr
import java.util.*

//val wgs84wkt = """GEOGCS["WGS 84",DATUM["WGS_1984",SPHEROID["WGS 84",6378137,298.257223563,AUTHORITY["EPSG","7030"]],AUTHORITY["EPSG","6326"]],PRIMEM["Greenwich",0,AUTHORITY["EPSG","8901"]],UNIT["degree",0.0174532925199433,AUTHORITY["EPSG","9122"]],AXIS["Latitude",NORTH],AXIS["Longitude",EAST],AUTHORITY["EPSG","4326"]]"""

class App {

    init {
        AllRegister()
        SetConfigOption("OGR_S57_OPTIONS", "LNAM_REFS:ON,UPDATES:ON,SPLIT_MULTIPOINT:ON,PRESERVE_EMPTY_NUMBERS:ON,RETURN_LINKAGES:ON")
    }

    private val path = "${System.getenv("HOME")}/Charts/ENC/US_REGION15/US5WA22M/US5WA22M.000"

    val gdalVersion: String
        get() {
            osr.SRS_DN_WGS84
            return VersionInfo()
        }

    fun openS57() {
        val ds = OpenEx(path)
        println("proj4 spatial ref = ${ds.GetSpatialRef()?.ExportToPrettyWkt()}")
        println("projection = ${ds.GetProjectionRef()}")
        println("number of layers = ${ds.GetLayerCount()}")
        for (i in 0 until ds.GetLayerCount()) {
            val layer = ds.GetLayer(i)
//            println("layer name ${layer.GetName()}")

            val name = layer.GetName()
            if (name == "BOYSPP") {
                println("layer name = $name")
                println("$i extent = ${Arrays.toString(layer.GetExtent())}")

                var feature: Feature? = layer.GetNextFeature()
                while (feature != null) {
                    feature.printFields()
                    println("--------------------")
//                    val wkt = feature.GetGeometryRef()?.ExportToWkt()
//                    println("wkt = $wkt")
                    feature = layer.GetNextFeature()
                }
            }
            layer.delete()
        }
    }
}

fun main() {
    val app = App()
    println("gdal version info: ${app.gdalVersion}")
    app.openS57()
}
