plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.compose)
  alias(libs.plugins.compose.compiler)
}

compose.resources {
  publicResClass = true
  packageOfResClass = "io.github.castrokingjames.reels"
  generateResClass = auto
}

kotlin {
  androidTarget()
  jvm()
  sourceSets {
    commonMain {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.components.resources)
      }
    }
    jvmMain {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.components.resources)
      }
    }
  }
}

android {
  namespace = "io.github.castrokingjames.ui"
  compileSdk = 34
  defaultConfig {
    minSdk = 24
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
