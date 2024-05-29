lint: kotlin_lint android_lint

kotlin_lint:
	./gradlew lintKotlin

android_lint:
	./gradlew lint

format:
    ./gradlew formatKotlin
