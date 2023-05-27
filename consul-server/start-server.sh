#!/bin/sh

# Using apt/yum to install consul, or just download and include it in PATH

consul agent -dev -config-file ./config/server.json
# consul agent -config-file ./config/server.json -data-dir=./data/server