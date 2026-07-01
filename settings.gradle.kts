pluginManagement {
  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "com.google.dagger.hilt.android") {
        useModule("com.google.dagger:hilt-android-gradle-plugin:${requested.version}")
      }
    }
  }
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "AI Studio Ultimate"

include(":app")

include(":core")
include(":common")
include(":designsystem")
include(":data")
include(":domain")
include(":ai-engine")
include(":providers")
include(":agents")
include(":plugins")
include(":features:chat")
include(":features:workspace")
include(":features:models")
include(":features:settings")