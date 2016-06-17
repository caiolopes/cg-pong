all:
	mvn package
mac:
	java -cp .:target/*:target/lib/* -Djava.library.path=target/natives:target/pong-1.0-SNAPSHOT.jar -XstartOnFirstThread usp.cg.game.Main
linux:
	java -cp .:target/*:target/lib/* -Djava.library.path=target/natives:target/pong-1.0-SNAPSHOT.jar usp.cg.game.Main -XstartOnFirstThread
windows:
	java -cp .;target/*;target/lib/* -Djava.library.path=target/natives;target/pong-1.0-SNAPSHOT.jar usp.cg.game.Main -XstartOnFirstThread