: TO include consul.exe in PATH as below:
: set PATH=%PATH%;D:\Jacky\Projects\bin\consul-1.15.2

consul agent -dev -config-file .\config\server.json
: consul agent -config-file .\config\server.json -data-dir=./data/server