# MCP Kotlin SDK

Kotlin implementation of the [Model Context Protocol](https://modelcontextprotocol.io) (MCP), providing both client and server capabilities for integrating with LLM surfaces.

## Overview

The Model Context Protocol allows applications to provide context for LLMs in a standardized way, separating the concerns of providing context from the actual LLM interaction. This Kotlin SDK implements the full MCP specification, making it easy to:

- Build MCP clients that can connect to any MCP server
- Create MCP servers that expose resources, prompts and tools
- Use standard transports like stdio, SSE, and WebSocket
- Handle all MCP protocol messages and lifecycle events

## Samples

- [kotlin-mcp-server](./samples/kotlin-mcp-server): shows how to set up Kotlin MCP server with different tools and other features.

## Installation

Add the JitPack repository to your build file:

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}
```

Add the dependency:

```kotlin
dependencies {
    implementation("com.github.JetBrains:mcp-kotlin-sdk:main-SNAPSHOT")
}
```

## Quick Start

### Creating a Client

```kotlin
import org.jetbrains.kotlinx.mcp.client.Client
import org.jetbrains.kotlinx.mcp.client.StdioClientTransport
import org.jetbrains.kotlinx.mcp.Implementation

val client = Client(
    clientInfo = Implementation(
        name = "example-client",
        version = "1.0.0"
    )
)

val transport = StdioClientTransport(
    inputStream = processInputStream,
    outputStream = processOutputStream
)

// Connect to server
client.connect(transport)

// List available resources
val resources = client.listResources()

// Read a specific resource
val resourceContent = client.readResource(
    ReadResourceRequest(uri = "file:///example.txt")
)
```

### Creating a Server

```kotlin
import org.jetbrains.kotlinx.mcp.server.Server
import org.jetbrains.kotlinx.mcp.server.ServerOptions
import org.jetbrains.kotlinx.mcp.server.StdioServerTransport
import org.jetbrains.kotlinx.mcp.ServerCapabilities

val server = Server(
    serverInfo = Implementation(
        name = "example-server",
        version = "1.0.0"
    ),
    options = ServerOptions(
        capabilities = ServerCapabilities(
            resources = ServerCapabilities.Resources(
                subscribe = true,
                listChanged = true
            )
        )
    )
)

// Add a resource
server.addResource(
    uri = "file:///example.txt",
    name = "Example Resource",
    description = "An example text file",
    mimeType = "text/plain"
) { request ->
    ReadResourceResult(
        contents = listOf(
            TextResourceContents(
                text = "This is the content of the example resource.",
                uri = request.uri,
                mimeType = "text/plain"
            )
        )
    )
}

// Start server with stdio transport
val transport = StdioServerTransport()
server.connect(transport)
```

### Using SSE Transport

```kotlin
import io.ktor.server.application.*
import org.jetbrains.kotlinx.mcp.server.MCP

fun Application.module() {
    MCP {
        Server(
            serverInfo = Implementation(
                name = "example-sse-server",
                version = "1.0.0"
            ),
            options = ServerOptions(
                capabilities = ServerCapabilities(
                    prompts = ServerCapabilities.Prompts(listChanged = null),
                    resources = ServerCapabilities.Resources(subscribe = null, listChanged = null)
                )
            )
        )
    }
}
```

## Contributing

Issues and pull requests are welcome on GitHub at https://github.com/JetBrains/mcp-kotlin-sdk.

## License

This project is licensed under the MIT License—see the [LICENSE](LICENSE) file for details.
