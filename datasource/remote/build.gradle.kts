plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  androidTarget()
  jvm()
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.common)
        implementation(projects.model)
        implementation(libs.ktor.client)
        implementation(libs.kotlin.serialization)
      }
    }
  }
}

android {
  namespace = "io.github.castrokingjames.datasource.remote"
  compileSdk = 34
  defaultConfig {
    minSdk = 24
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
