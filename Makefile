package:clean
	@mvn package

clean:
	@mvn clean

run:package
	@java -jar order-center-cp/cp-oc-main/target/cp-ddd-framework-demo.jar

run-plugin:package
	@java -jar order-center-cp/cp-oc-main/target/cp-ddd-framework-demo.jar 9090 plugin

plugin-loading:clean
	@mvn package -Pplugin

depgraph:
	@mvn install
	@mvn com.github.ferstl:depgraph-maven-plugin:aggregate -DcreateImage=true -DreduceEdges=false -Dscope=compile "-Dincludes=*:*"
	@open target/dependency-graph.png
