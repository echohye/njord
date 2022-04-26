package io.madrona.njord.components

import io.madrona.njord.IconInfo
import io.madrona.njord.Theme
import io.madrona.njord.pathToFullUrl
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import react.*
import react.dom.*
import styled.css
import styled.styledImg

typealias IconData = Map<String, IconInfo>

fun spriteJsonUrl(theme: Theme) = "/v1/content/sprites/${theme.name.lowercase()}_sprites.json".pathToFullUrl()

fun spritePngUrl(theme: Theme) = "/v1/content/sprites/${theme.name.lowercase()}_sprites.png".pathToFullUrl()

suspend fun fetchThemeIcons(theme: Theme): IconData {
    val response = window
        .fetch(spriteJsonUrl(theme))
        .await()
        .text()
        .await()
    return Json.decodeFromString(response)
}

val ControlSprites = fc<Props> {
    var themeData: Map<Theme, IconData>? by useState(null)
    val theme = Theme.Day
    useEffectOnce {
        mainScope.launch {
            themeData = mapOf(theme to fetchThemeIcons(theme))
        }
    }

    div {
        h2 {
            +"Chart Symbol Sprites - ${theme.name}"
        }
        div {
            +"Sprite sheet: "
            a(href = spritePngUrl(theme), target = "_blank") {
                +spritePngUrl(theme)
            }
        }
        div {
            +"Sprite json: "
            a(href = spriteJsonUrl(theme), target = "_blank") {
                +spriteJsonUrl(theme)
            }
        }
        themeData?.let {
            div(classes = "col") {
                div(classes = "container") {
                    themeData?.get(theme)?.asIterable()?.chunked(3)?.forEach {
                        div(classes = "row") {
                            it.forEach {
                                val imgData = it.value
                                div(classes = "col-sm") {
                                    br { }
                                    +it.key
                                    a(href= "/v1/icon/${theme}/${it.key}.png") {
                                        styledImg {
                                            css {
                                                width = imgData.width.px
                                                height = imgData.height.px
                                                background = "url('${spritePngUrl(theme)}');"
                                                backgroundPosition = "-${imgData.x}px -${imgData.y}px"
                                                display = Display.inlineBlock
                                                borderWidth = 4.px
                                            }
                                        }
                                    }
                                    br { }
                                }
                            }
                        }
                    }
                }
            }
        } ?: Loading {}
    }
}