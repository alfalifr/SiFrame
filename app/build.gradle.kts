import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

val kotlin_version= findProperty("kotlin_version")
val anko_version= findProperty("anko_version")

val sidev_lib_version_std = findProperty("sidev_lib_version_std")
val sidev_lib_version_std_x = findProperty("sidev_lib_version_std_x")
val sidev_lib_version_std_new = findProperty("sidev_lib_version_std_new")


plugins{
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
//    `kotlin-dsl`
}

android {
    compileSdkVersion(29)
    buildToolsVersion = "29.0.3"

    defaultConfig {
        applicationId = "sidev.lib.jvm"
        minSdkVersion(21) //15
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            isJniDebuggable = false
            isRenderscriptDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            //minifyEnabled true
            //shrinkResources true
            isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    //Ini untuk menangani masalah "Cannot inline bytecode built with JVM target 1.8 into bytecode that is being built with JVM target 1.6"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
//        languageVersion= "1.4"
    }

// To inline the bytecode built with JVM target 1.8 into
// bytecode that is being built with JVM target 1.6. (e.g. navArgs)
}

dependencies {
    implementation(fileTree(
        mapOf("dir" to "libs", "include" to listOf("*.jar"))
    ))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.navigation:navigation-fragment:2.0.0")
    implementation("androidx.navigation:navigation-ui:2.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.0.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.0.0")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    //DEX
    implementation("com.android.support:multidex:1.0.3")

    //anko
    implementation("org.jetbrains.anko:anko:$anko_version")
    implementation("org.jetbrains.anko:anko-design:$anko_version")
    implementation("org.jetbrains.anko:anko-sqlite:$anko_version")

    //Google Material
    implementation("com.google.android.material:material:1.0.0")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.72")

    implementation("com.squareup.picasso:picasso:2.71828")

    implementation("androidx.preference:preference:1.1.1")

    //Loading dots
    implementation("com.romandanylyk:pageindicatorview:1.0.3")
    // google vision
    implementation( "com.google.android.gms:play-services-vision:19.0.0")
    // untuk http request
    implementation("com.loopj.android:android-async-http:1.4.9")

    //String match
    implementation("io.grpc:grpc-core:1.13.1")
    implementation("org.apache.commons:commons-lang3:3.6")


    implementation("sidev.lib.jvm:jvmstdlib:$sidev_lib_version_std_x") //{ isTransitive= true }
    implementation("sidev.lib.kotlin:ktstdlib-jvm:$sidev_lib_version_std_new") //{ isTransitive= true }
    implementation("sidev.lib.kotlin:ktasync-jvm:$sidev_lib_version_std") //{ isTransitive= true }
    implementation("sidev.lib.kotlin:ktreflex-jvm:$sidev_lib_version_std") //{ isTransitive= true }
    implementation("sidev.data:jvmqurandata:$sidev_lib_version_std") //{ isTransitive= true }
    implementation(project(":siframe"))
    implementation(project(":SiFrame-Customizable"))
    implementation(project(":viewrap"))
    //implementation(project(":AndroidStdLib"))
    implementation(project(":androidstdlib"))

//    implementation(project(mapOf("path" to ":jvm:android:viewrap")))
//    implementation(project("path" to ":jvm:universal"))
//    implementation(project("path" to ":jvm:android:siframe"))
//    implementation(project("path" to ":jvm:android:siframe:customizable"))

//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
}
// */




/*
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'

android {
    compileSdkVersion 29
    buildToolsVersion "29.0.3"

    defaultConfig {
        applicationId "sidev.lib.jvm"
        minSdkVersion 21 //15
        targetSdkVersion 29
        versionCode 1
        versionName "1.0"
        multiDexEnabled true

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            debuggable false
            jniDebuggable false
            renderscriptDebuggable false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        debug {
            //minifyEnabled true
            //shrinkResources true
            debuggable true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    //Ini untuk menangani masalah "Cannot inline bytecode built with JVM target 1.8 into bytecode that is being built with JVM target 1.6"
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

// To inline the bytecode built with JVM target 1.8 into
// bytecode that is being built with JVM target 1.6. (e.g. navArgs)
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.72"
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.core:core-ktx:1.2.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'androidx.navigation:navigation-fragment:2.0.0'
    implementation 'androidx.navigation:navigation-ui:2.0.0'
    implementation 'androidx.lifecycle:lifecycle-extensions:2.0.0'
    implementation 'androidx.navigation:navigation-fragment-ktx:2.0.0'
    implementation 'androidx.navigation:navigation-ui-ktx:2.0.0'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'

    //DEX
    implementation 'com.android.support:multidex:1.0.3'

    //anko
    implementation "org.jetbrains.anko:anko:0.10.8"
    implementation "org.jetbrains.anko:anko-design:0.10.8"
    implementation "org.jetbrains.anko:anko-sqlite:0.10.8"

    //Google Material
    implementation 'com.google.android.material:material:1.0.0'

    implementation "org.jetbrains.kotlin:kotlin-reflect:1.3.72"

    //String match
    implementation 'io.grpc:grpc-core:1.13.1'
    implementation 'org.apache.commons:commons-lang3:3.6'
    implementation project(path: ':jvm:universal')
    implementation project(path: ':jvm:android:siframe')
    implementation project(path: ':jvm:android:siframe:customizable')
}
// */
 /*
release {
    //Enable the proguard
    minifyEnabled true
    proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), "proguard-rules.pro"

    //Other parameters
    debuggable false
    jniDebuggable false
    renderscriptDebuggable false
//    signingConfig playStoreConfig //Add your own signing config
    pseudoLocalesEnabled false
    zipAlignEnabled true
}
// */