all:
	mvn package
run:
	java -cp .:target/*:target/lib/* -Djava.library.path=target/natives:target/pong-1.0-SNAPSHOT.jar -XstartOnFirstThread usp.cg.game.Main