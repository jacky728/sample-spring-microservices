: TO include consul.exe in PATH as below:
: set PATH=%PATH%;D:\Jacky\Projects\bin\consul-1.15.2

consul agent -dev -config-file .\config\client.json
: consul agent -config-file .\config\client.json -data-dir=./data/client