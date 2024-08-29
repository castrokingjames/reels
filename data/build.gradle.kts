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
        implementation(projects.timber)
        implementation(projects.model)
        implementation(projects.domain)
        implementation(projects.datasource.remote)
        implementation(projects.datasource.local)
        implementation(libs.kotlin.coroutines)
        implementation(libs.kotlin.datetime)
      }
    }
  }
}

android {
  namespace = "io.github.castrokingjames.data"
  compileSdk = 34
  defaultConfig {
    minSdk = 24
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
