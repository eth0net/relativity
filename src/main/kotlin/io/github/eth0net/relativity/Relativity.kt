package io.github.eth0net.relativity

import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Relativity : ModInitializer {
    internal const val MOD_ID = "relativity"

    internal val log: Logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize(mod: ModContainer) {
        log.info("Relativity initializing...")



        log.info("Relativity initialized")
    }
}
