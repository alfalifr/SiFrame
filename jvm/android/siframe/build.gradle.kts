//import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.LibraryExtension //.LibraryExtension_Decorated
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
//import sidev.lib.gradle
//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


fun Project.android(configure: LibraryExtension.() -> Unit) =
    (this as ExtensionAware).extensions.configure("android", configure)

fun LibraryExtension.kotlinOptions(configure: KotlinJvmOptions.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("kotlinOptions", configure)

fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    add("testImplementation", dependencyNotation)

fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    add("androidTestImplementation", dependencyNotation)

val kotlin_version= findProperty("kotlin_version")
val anko_version= findProperty("anko_version")


// /*
plugins{
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("com.github.dcendents.android-maven")
}

group="sidev.lib.jvm.android"
//version="1.0cob"

android {
    compileSdkVersion(29)
    buildToolsVersion = "29.0.3"

    defaultConfig {
        minSdkVersion(21) //15
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            //minifyEnabled true
            //debuggable false
            //jniDebuggable false
            //renderscriptDebuggable false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            //minifyEnabled true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(fileTree(
        mapOf("dir" to "libs", "include" to listOf("*.jar"))
    ))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.2.0")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    implementation(project("path" to ":jvm:universal"))
    implementation(project("path" to ":jvm:android:external"))
    implementation(project("path" to ":jvm:android:siframe:customizable"))

    //anko
    implementation("org.jetbrains.anko:anko:$anko_version")
    implementation("org.jetbrains.anko:anko-design:$anko_version")
    implementation("org.jetbrains.anko:anko-sqlite:$anko_version")

    //Google Material
    implementation("com.google.android.material:material:1.0.0")

    //Load Gambar dari Server
    implementation("com.squareup.picasso:picasso:2.71828")

    //Untuk nertwok
    implementation("com.android.volley:volley:1.1.1")
    implementation("com.loopj.android:android-async-http:1.4.9")

    //Gson
    implementation("com.google.code.gson:gson:2.8.6")
//    implementation(kotlinModule("reflect", "1.3.72"))

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.72")
}
// */

/*
apply plugin: "com.android.library"
apply plugin: "kotlin-android"
apply plugin: "kotlin-android-extensions"
apply plugin: "com.github.dcendents.android-maven"
group="sidev.lib.jvm.android"
//version="1.0cob"

android {
    compileSdkVersion 29
    buildToolsVersion "29.0.3"

    defaultConfig {
        minSdkVersion 21 //15
        targetSdkVersion 29
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles "consumer-rules.pro"
    }

    buildTypes {
        release {
            //minifyEnabled true
            //debuggable false
            //jniDebuggable false
            //renderscriptDebuggable false
            proguardFiles getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
        }
        debug {
            //minifyEnabled true
            proguardFiles getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation fileTree(dir: "libs", include: ["*.jar"])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.72"
    implementation "androidx.appcompat:appcompat:1.1.0"
    implementation "androidx.core:core-ktx:1.2.0"
    testImplementation "junit:junit:4.12"
    androidTestImplementation "androidx.test.ext:junit:1.1.1"
    androidTestImplementation "androidx.test.espresso:espresso-core:3.2.0"
    implementation project(path: ":jvm:universal")
    implementation project(path: ":jvm:android:external")
    implementation project(path: ":jvm:android:siframe:customizable")

    //anko
    implementation "org.jetbrains.anko:anko:0.10.8"
    implementation "org.jetbrains.anko:anko-design:0.10.8"
    implementation "org.jetbrains.anko:anko-sqlite:0.10.8"

    //Google Material
    implementation "com.google.android.material:material:1.0.0"

    //Load Gambar dari Server
    implementation "com.squareup.picasso:picasso:2.71828"

    //Untuk nertwok
    implementation "com.android.volley:volley:1.1.1"
    implementation "com.loopj.android:android-async-http:1.4.9"
}
// */

