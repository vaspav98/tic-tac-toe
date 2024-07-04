refresh-and-start-all:
	rm -r front2/src &
	rm -r front3/src &
	rm -r front4/src &

	gradle front:clean

	cp -r front/src front2/src
	cp -r front/src front3/src
	cp -r front/src front4/src

	gradle server:bootRun &

	gradle front:run &
	gradle front2:run &
	gradle front3:run &
	gradle front4:run

kill-process:
	@sudo lsof -t -i:8080 | xargs kill -9

prepare-jars-before-pushing:
	gradle front:clean
	gradle front:build
	cp front/build/libs/front-1.0-SNAPSHOT.jar server/src/main/resources/static/tic-tac-toe.jar

	gradle server:clean
	gradle server:build
	cp server/build/libs/server-1.0-SNAPSHOT.jar server/

refresh-all:
		rm -r front2/src &
		rm -r front3/src &
		rm -r front4/src &
		gradle front:clean
		cp -r front/src front2/src
		cp -r front/src front3/src
		cp -r front/src front4/src
