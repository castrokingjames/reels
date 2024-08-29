plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
  androidTarget()
  jvm()
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.common)
        implementation(projects.model)
        implementation(libs.kotlin.coroutines)
      }
    }
  }
}

android {
  namespace = "io.github.castrokingjames.domain"
  compileSdk = 34
  defaultConfig {
    minSdk = 24
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
