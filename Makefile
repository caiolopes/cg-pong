all:
	mvn package

run:
	java -cp .;target/*;target/lib/* -Djava.library.path=target/natives;target/pong-1.0-SNAPSHOT.jar usp.cg.game.Main -XstartOnFirstThread