package io.github.eth0net.relativity.network

import io.github.eth0net.relativity.Relativity

object Channels {
    internal val CONTROL = Relativity.id("control_channel")
    internal val SYNC = Relativity.id("sync_channel")
}
