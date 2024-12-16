#!/usr/bin/env bash

../gradlew clean localJar; java -jar ../build/libs/kotlinx-mcp-sdk.jar --demo
