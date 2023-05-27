#!/bin/sh

# Using apt/yum to install consul, or just download and include it in PATH

consul agent -dev -config-file ./config/client.json
# consul agent -config-file ./config/client.json -data-dir=./data/client