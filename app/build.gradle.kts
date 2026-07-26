import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use(::load)
    }
}

android {
    namespace = "com.buildsol.cryptotracker"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("API_KEY") ?: ""}\"")
        applicationId = "com.buildsol.cryptotracker"
        minSdk = 27
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

// NEW — forces a single kotlinx-coroutines-core/android version across the
// whole dependency graph, overriding whatever Room/Koin/lifecycle/navigation
// would otherwise transitively pull in. This is the actual fix for the
// debounceInternal NullPointerException: without this, Gradle's default
// "highest version wins" resolution could still pick a version that some
// library's inline coroutine operators weren't compiled against.
configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlinx:kotlinx-coroutines-core:${libs.versions.kotlinxCoroutines.get()}")
        force("org.jetbrains.kotlinx:kotlinx-coroutines-android:${libs.versions.kotlinxCoroutines.get()}")
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.material.icons.extended) // full Material Symbols set (Search, WifiOff, SearchOff, ArrowDropUp/Down)
    implementation(libs.androidx.compose.foundation) // LazyColumn, combinedClickable, shimmer-style Brush animations

    implementation(libs.androidx.lifecycle.viewmodel.compose) // viewModel() composable helper
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // ViewModel + viewModelScope for launching coroutines

    // --- Coroutines (explicit — see resolutionStrategy.force above) ---
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // --- Navigation ---
    implementation(libs.navigation.compose) // NavHost/NavController — list -> detail screen navigation

    // --- Dependency Injection (Koin) ---
    implementation(libs.koin.android) // Koin core Android integration (start Koin in Application class)
    implementation(libs.koin.core) // Koin DSL — module { }, single { }, factory { }
    implementation(libs.koin.compose) // koinInject() for injecting into composables
    implementation(libs.koin.compose.viewmodel) // koinViewModel() — inject ViewModels scoped to nav graph
    implementation(libs.koin.compose.viewmodel.navigation) // ties koinViewModel() lifecycle to NavBackStackEntry

    // --- Networking (Retrofit + OkHttp) ---
    implementation(libs.retrofit) // Retrofit core — defines CoinGeckoApi interface
    implementation(libs.retrofit.kotlinx) // converter: parses JSON responses directly into @Serializable DTOs
    implementation(libs.okhttp) // underlying HTTP client Retrofit runs on
    implementation(libs.logging) // OkHttp logging interceptor — inspect request/response bodies while debugging API calls
    implementation(libs.kotlinx.serialization.json) // @Serializable annotations + JSON parser for CoinDto/CoinDetailDto

    // --- Local Persistence (Room) ---
    implementation(libs.androidx.room.runtime) // Room core — @Database, @Entity, @Dao
    implementation(libs.androidx.room.ktx) // Kotlin coroutines/Flow support for Room DAOs
    ksp(libs.androidx.room.compiler) // annotation processor generating Room's DB implementation at compile time

    // --- Image Loading (Coil) ---
    implementation(libs.coil.compose) // AsyncImage composable — loads + caches coin icons from CoinGecko URLs
    implementation(libs.coil.network.okhttp) // Coil's network layer, backed by OkHttp (shares client config with Retrofit)

    // --- Fonts ---
    implementation(libs.androidx.compose.ui.text.google.fonts) // GoogleFont() downloadable fonts API — for JetBrains Mono/Roboto Mono on price values

    // --- Unit Testing ---
    testImplementation(libs.mockk) // mock CoinRepository/CoinDao in ViewModel + repository tests
    testImplementation(libs.turbine) // test { } DSL for asserting values emitted from StateFlow/Flow
    testImplementation(libs.kotlinx.coroutines.test) // runTest {} + TestDispatcher for testing suspend fns/viewModelScope code

    // --- Instrumented / UI Testing ---
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM for instrumented Compose test deps

}