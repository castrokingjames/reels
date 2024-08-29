plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
  androidTarget()
  jvm()
  js(IR) {
    browser()
    nodejs()
  }
}

android {
  namespace = "timber.log"
  compileSdk = 34
  defaultConfig {
    minSdk = 24
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
