#!/usr/bin/env bash

../gradlew clean localJar && npx @modelcontextprotocol/inspector java -jar ../build/libs/kotlinx-mcp-sdk.jar --server
