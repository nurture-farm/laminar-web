module code.nurture.farm/ExampleService

go 1.14

replace code.nurture.farm/libraries/go/go-common => code.nurture.farm/libraries/go/go-common.git v0.0.2



require (
    
    code.nurture.farm/libraries/go/go-common v0.0.2
	contrib.go.opencensus.io/integrations/ocsql v0.1.7
	github.com/facebook/ent v0.5.1
	github.com/go-sql-driver/mysql v1.5.1-0.20200311113236-681ffa848bae
	github.com/golang/protobuf v1.4.3
	github.com/grpc-ecosystem/go-grpc-prometheus v1.2.0
	github.com/prometheus/client_golang v1.6.0
	github.com/spf13/cast v1.3.0
	github.com/spf13/viper v1.7.0
	go.uber.org/zap v1.15.0
	google.golang.org/grpc v1.34.0
	google.golang.org/protobuf v1.25.0
	
)
