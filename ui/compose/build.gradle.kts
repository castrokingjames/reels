plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.compose)
  alias(libs.plugins.compose.compiler)
}

kotlin {
  androidTarget()
  jvm()
  sourceSets {
    commonMain {
      dependencies {
        api(compose.material)
        api(compose.material3)
        api(compose.materialIconsExtended)
        api(compose.runtime)
        api(compose.foundation)
        implementation(projects.timber)
      }
    }
    androidMain {
      dependencies {
        api(libs.compose.activity)
        api(libs.androidx.window)
        api(libs.exoplayer)
        api(libs.exoplayer.dash)
        api(libs.exoplayer.hls)
        api(libs.exoplayer.ui)
      }
    }
  }
}

android {
  namespace = "io.github.castrokingjames.ui.compose"
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
