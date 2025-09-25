.PHONY: all build test run clean

all: build test

build:
	./gradlew build

test:
	./gradlew test

run:
	./gradlew bootRun

clean:
	./gradlew clean
