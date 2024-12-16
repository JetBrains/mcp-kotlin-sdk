package org.jetbrains.kotlinx.mcp.client

import org.jetbrains.kotlinx.mcp.Implementation
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import org.jetbrains.kotlinx.mcp.LIB_VERSION
import org.jetbrains.kotlinx.mcp.shared.IMPLEMENTATION_NAME
import kotlin.time.Duration

/**
 * Returns a new SSE transport for the Model Context Protocol using the provided HttpClient.
 *
 * @param urlString Optional URL of the MCP server.
 * @param reconnectionTime Optional duration to wait before attempting to reconnect.
 * @param  requestBuilder Optional lambda to configure the HTTP request.
 * @return A [SSEClientTransport] configured for MCP communication.
 */
public fun HttpClient.mcpSseTransport(
    urlString: String? = null,
    reconnectionTime: Duration? = null,
    requestBuilder: HttpRequestBuilder.() -> Unit = {},
): SSEClientTransport = SSEClientTransport(this, urlString, reconnectionTime, requestBuilder)

/**
 * Creates and connects an MCP client over SSE using the provided HttpClient.
 *
 * @param urlString Optional URL of the MCP server.
 * @param reconnectionTime Optional duration to wait before attempting to reconnect.
 * @param requestBuilder Optional lambda to configure the HTTP request.
 * @return A connected [Client] ready for MCP communication.
 */
public suspend fun HttpClient.mcpSse(
    urlString: String? = null,
    reconnectionTime: Duration? = null,
    requestBuilder: HttpRequestBuilder.() -> Unit = {},
): Client {
    val transport = mcpSseTransport(urlString, reconnectionTime, requestBuilder)
    val client = Client(
        Implementation(
            name = IMPLEMENTATION_NAME,
            version = LIB_VERSION,
        )
    )
    client.connect(transport)
    return client
}
