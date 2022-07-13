package io.madrona.njord

import io.madrona.njord.components.App
import kotlinx.browser.document
import kotlinx.css.*
import react.dom.client.createRoot
import react.dom.html.ReactHTML.div
import react.dom.render
import react.router.dom.BrowserRouter
import styled.injectGlobal

fun main() {
    kotlinext.js.require("bootstrap/dist/css/bootstrap.min.css")
    kotlinext.js.require("bootstrap/dist/js/bootstrap.min.js")
    kotlinext.js.require("jquery/dist/jquery.js")

    //https://ktor.io/docs/css-dsl.html#use_css
    injectGlobal(
        CssBuilder(allowClasses = false).apply {
            body {
                height = 100.vh
                margin(0.px)
            }
        }
    )

//    createRoot(document.getElementById("root")!!).apply {
//        render(BrowserRouter {
//            App {}
//        })
//
//    }
//    createRoot(document.getElementById("root")!!).also {
//        it.render(
//
//        )
//    }
    render(document.getElementById("root")!!) {
        BrowserRouter {
            App {}
        }
    }
}
