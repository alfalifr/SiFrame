import com.android.build.gradle.LibraryExtension //.LibraryExtension_Decorated
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.io.FileInputStream
import java.util.Properties
import java.util.Date
import com.jfrog.bintray.gradle.BintrayExtension


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

fun File.withInputStream(): FileInputStream = FileInputStream(this)

val kotlin_version= findProperty("kotlin_version").also{ println("${project.name} kotlin_version= $it")}
val anko_version= findProperty("anko_version")

val sidev_lib_version_std = findProperty("sidev_lib_version_std")
val sidev_lib_version_std_x = findProperty("sidev_lib_version_std_x")
val sidev_lib_version_std_new = findProperty("sidev_lib_version_std_new")

val GROUP_ID= "sidev.lib.android" //"sidev.lib.jvm.android"
val ARTIFACT_ID= project.name //"SiFrame"
val BINTRAY_REPOSITORY= "SidevLib" //"JvmLib"
val BINTRAY_ORGINIZATION= ""
val ISSUE_URL= ""
val SITE_URL= ""
val VCS_URL= "https://github.com/alfalifr/SiFrame/tree/master/jvm/android/viewrap"
val LIBRARY_VERSION_NAME= "0.0.1x"

val prop= Properties().apply { load(file("../../../setting.properties").withInputStream()) }

val bintrayUser= prop["bintrayUser"] as String //prop.loadPropertyFromResources("setting.properties", "bintrayUser")
val bintrayApiKey= prop["bintrayApiKey"] as String //prop.loadPropertyFromResources("setting.properties", "bintrayApiKey")
val aarDir= properties["aar_dir"]
val aarArtifact= "$aarDir/$ARTIFACT_ID-release.aar"


plugins{
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("com.github.dcendents.android-maven")
    id("maven-publish")
}

group= GROUP_ID //"sidev.lib.jvm.viewrap"
version= LIBRARY_VERSION_NAME //"1.0cob"

apply("plugin" to "maven-publish")
apply("plugin" to "com.jfrog.bintray")


android {
    compileSdkVersion(29)
    buildToolsVersion = "29.0.3"
//    buildToolsVersion = "30.0.0"

    defaultConfig {
        minSdkVersion(15)
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
//        languageVersion= "1.4"
    }
}

dependencies {
    implementation(fileTree(
        mapOf("dir" to "libs", "include" to listOf("*.jar"))
    ))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")


    implementation("sidev.lib.jvm:JvmStdLib:$sidev_lib_version_std_x") //{ isTransitive= true }
    implementation("sidev.lib.kotlin:KtStdLib-jvm:$sidev_lib_version_std_new") //{ isTransitive= true }
//    implementation(project(":SiFrame"))
    implementation(project(":AndroidStdLib"))
//    implementation(project("path" to ":jvm:android:siframe"))
//    implementation(project("path" to ":jvm:universal"))

    //Google Material
    implementation("com.google.android.material:material:1.0.0")
}




/*
========================
Template Task
========================
 */

val javadocJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from("javadoc")
}

publishing {
    publications {
        create<MavenPublication>("androidLibrary"){
            artifact(javadocJar)
            artifact(aarArtifact)

            pom {
                name.set(ARTIFACT_ID)
//                description = "WebDriver binary manager for java"
                url.set(ISSUE_URL)
//                licenses = ['Apache-2.0']

                licenses {
                    license {
                        name.set("Apache-2.0") //"MIT"
//                        url = "https://github.com/rosolko/wdm4j/blob/master/LICENSE"
                    }
                }

                developers {
                    developer {
                        id.set("alfalifr")
                        name.set("Aliffiro") //"Aliaksandr Rasolka"
                        email.set("fathf48@gmail.com")
                    }
                }

                withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")

                    //Iterate over the compile dependencies (we don't want the test ones), adding a <dependency> node for each
                    configurations.implementation.get().allDependencies.forEach {
                        if(it.group?.startsWith("sidev.lib") == true){
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", it.group)
                            dependencyNode.appendNode("artifactId", it.name)
                            dependencyNode.appendNode("version", it.version)
                        }
                    }
                }
            }
        }
    }
}


(properties["bintray"] as BintrayExtension/*_Decorated*/).apply {
    //A user name of bintray to A, and API key of Bintray to B.I do not want to include API Key in git,
    // so I am gradle.properties writing locally.
    // Gradle's property file is read in the order of gradle in the home directory> gradle in the project directory,
    // so if it is written in property in the home directory, it will be useful for other projects as well.
    user = bintrayUser //getBintrayUserProperty()
    key = bintrayApiKey //getBintrayApiKeyProperty()
    //f you set publish to true, the new version will be published the moment you upload to bintray. If false, it will not be published unless you press the publish button on the bintray web.
    // It is recommended that you make it false because it can prevent an accident that you accidentally release the latest version.
    publish = false
//    publications
    setPublications("androidLibrary")

    pkg.apply {
        repo = BINTRAY_REPOSITORY
        name = ARTIFACT_ID
        userOrg = BINTRAY_ORGINIZATION
        setLicenses("Apache-2.0")
        vcsUrl = VCS_URL
        websiteUrl = SITE_URL
        issueTrackerUrl = ISSUE_URL

        version.apply {
            name = LIBRARY_VERSION_NAME
            vcsTag = LIBRARY_VERSION_NAME
            released = Date().toString()
        }
    }
}

tasks["bintrayUpload"].apply {
    dependsOn("bundleReleaseAar")
    dependsOn("publishToMavenLocal")
}

// */
