plugins {
    id("com.android.library")
    `maven-publish`
}
android {
    namespace = "com.itskylin.devices.rixiang"
    compileSdk = 22

    defaultConfig {
        minSdk = 8
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 22
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        isAbortOnError = false
    }

    sourceSets {
        getByName("main").jniLibs.srcDir("libs")
    }
    packagingOptions {
        resources.excludes += "**/R.class"
        resources.excludes += "**/BuildConfig.class"
    }
}

dependencies {
    implementation(files("libs/androidnet.jar"))
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "io.github.bluesky15171"
                artifactId = "rixiang"
                version = "2.1.0"
            }
        }
    }
}
