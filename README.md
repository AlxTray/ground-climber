# Ground Climber

## What is this game?
This is both a level-based, and procedurally-generated, platformer where the player is ball that is continually bouncing therefore only being manoeuvrable left and right. You, the player, must bounce the ball to the end of the level, or as far as you can in the procedurally-generated endless mode.
Programmed in Java using LibGDX as the primary graphics library and internal Box2D bindings for the game physics.

## Manual running/building
LibGDX offers backends for Desktop, Android, IOS, and Web. However, only Desktop is currently supported. So to simply run **Ground Climber** from source use the Gradle wrapper (gradlew for Unix and gradlew.bat for Windows), and pass `desktop:run` into the script.

To instead create a .JAR then pass `desktop:dist` to the wrapper rather than `desktop:run`. The built .JAR will then be in the `desktop/build/libs` directory, which can be simply executed with `java -jar desktop-<version>.jar`. Please note this will require the JVM installed on your machine to work.

## Releases
Releases will contain the source code as a downloadable archive, that can be extracted locally.
