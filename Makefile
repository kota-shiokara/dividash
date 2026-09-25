lint:
	./gradlew ktlintCheck

format:
	./gradlew ktlintFormat

test:
	./gradlew checkDebugAarMetadata testDebugUnitTest
