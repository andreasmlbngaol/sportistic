package com.jawapbo.sportistic

import com.jawapbo.sportistic.auth.authRoute
import com.jawapbo.sportistic.core.config.DatabaseFactory
import com.jawapbo.sportistic.core.config.EnvConfig
import com.jawapbo.sportistic.core.config.FirebaseConfig
import com.jawapbo.sportistic.core.config.JwtConfig
import com.jawapbo.sportistic.core.controller.metricRoute
import com.jawapbo.sportistic.core.data.AuthNames
import com.jawapbo.sportistic.core.plugins.*
import com.jawapbo.sportistic.core.utils.respondJson
import com.jawapbo.sportistic.customers.bookmarks.bookmarkRoute
import com.jawapbo.sportistic.customers.merchants.merchantRoute
import com.jawapbo.sportistic.merchants.staffs.staffRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kotlinx.serialization.ExperimentalSerializationApi

fun main(args: Array<String>): Unit = EngineMain.main(args)

@OptIn(ExperimentalSerializationApi::class)
fun Application.module() {
    initKtor(environment.config)
    val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    corsPlugin()
    contentNegotiationPlugin()
    authenticationPlugin()
    statusPagesPlugin()
    callLoggingPlugin()
    micrometerMetricsPlugin(appMicrometerRegistry)

    routing {
        route("/") {
            get {
                call.respondRedirect("/api")
            }
        }

        route("/api") {
            get { call.respondJson(HttpStatusCode.OK, "Allo Woldeu") }
            metricRoute(appMicrometerRegistry)
            authRoute()


            // Protected customer routes
            route("/customers") {
               authenticate(AuthNames.CUSTOMER_JWT_AUTH) {
                   merchantRoute()
                   bookmarkRoute()
                }
            }

            // Protected merchant routes
            route("/merchants") {
                authenticate(AuthNames.MERCHANT_JWT_AUTH) {
                    staffRoute()
                }
            }
        }
    }
}

fun initKtor(
    config: ApplicationConfig
) {
    FirebaseConfig.init()
    EnvConfig.init()
    JwtConfig.init(config)
    DatabaseFactory.init(config)
}