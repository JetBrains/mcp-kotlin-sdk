# MCP Kotlin Server Sample

A sample implementation of an MCP (Model Communication Protocol) server in Kotlin that demonstrates different server configurations and transport methods.

## Features

- Multiple server operation modes:
    - Standard I/O server
    - SSE (Server-Sent Events) server with plain configuration
    - SSE server using Ktor plugin
- Built-in capabilities for:
    - Prompts management
    - Resources handling
    - Tools integration

## Getting Started

### Running the Server

To run the server in SSE mode on the port 3001, run:

```bash
./gradlew run
```

### Connecting to the Server

For SSE servers (both plain and Ktor plugin versions):
1. Start the server
2. Use the MCP inspector to connect to `http://localhost:<port>/sse`

## Server Capabilities

- **Prompts**: Supports prompt management with list change notifications
- **Resources**: Includes subscription support and list change notifications
- **Tools**: Supports tool management with list change notifications

## Implementation Details

The server is implemented using:
- Ktor for HTTP server functionality
- Kotlin coroutines for asynchronous operations
- SSE for real-time communication
- Standard I/O for command-line interface
