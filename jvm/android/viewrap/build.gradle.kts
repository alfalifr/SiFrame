import com.android.build.gradle.LibraryExtension //.LibraryExtension_Decorated
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.io.FileInputStream
import java.util.Properties
import java.util.Date
import com.jfrog.bintray.gradle.BintrayExtension
import java.lang.Thread.sleep


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
val PKG_REPOSITORY= "SidevLib_Private" //"JvmLib"
val BINTRAY_ORGINIZATION= ""
val ISSUE_URL= ""
val SITE_URL= ""
val VCS_URL= "https://github.com/alfalifr/SiFrame/tree/master/jvm/android/viewrap"
val LIBRARY_VERSION_NAME= "0.0.1x"

val prop= Properties().apply { load(file("../../../setting.properties").withInputStream()) }

val aarDir= properties["aar_dir"]
val aarArtifact= "$aarDir/$ARTIFACT_ID-release.aar"

val bintrayUser= prop["bintrayUser"] as String //prop.loadPropertyFromResources("setting.properties", "bintrayUser")
val bintrayApiKey= prop["bintrayApiKey"] as String //prop.loadPropertyFromResources("setting.properties", "bintrayApiKey")

val githubUser= prop["githubUser"] as String //prop.loadPropertyFromResources("setting.properties", "bintrayUser")
val githubApiKey= prop["githubApiKey"] as String //prop.loadPropertyFromResources("setting.properties", "bintrayApiKey")

val githubPkgUrl= "https://maven.pkg.github.com/$githubUser/$PKG_REPOSITORY"

object Author {
    const val id= "alfalifr"
    const val name = "Aliffiro"
    const val email = "fathf48@gmail.com"
}


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


    implementation("sidev.lib.jvm:jvmstdlib:$sidev_lib_version_std_x") //{ isTransitive= true }
    implementation("sidev.lib.kotlin:ktstdlib-jvm:$sidev_lib_version_std_new") //{ isTransitive= true }
//    implementation(project(":SiFrame"))
    //implementation(project(":AndroidStdLib"))
    implementation(project(":androidstdlib"))
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
            artifactId = ARTIFACT_ID.toLowerCase()

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
                        id.set(Author.id)
                        name.set(Author.name) //"Aliaksandr Rasolka"
                        email.set(Author.email)
                    }
                }

                withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")

                    //Iterate over the compile dependencies (we don't want the test ones), adding a <dependency> node for each
                    configurations.implementation.get().allDependencies.forEach {
                        if(it.group != null && it.version != null){
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", it.group)
                            dependencyNode.appendNode("artifactId", it.name)
                            dependencyNode.appendNode("version", it.version)
                        }
                    }
                }
            }
        }
/*
        create<MavenPublication>("androidLibrary-github"){
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
                        id.set(Author.id)
                        name.set(Author.name) //"Aliaksandr Rasolka"
                        email.set(Author.email)
                    }
                }

                withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")

                    //Iterate over the compile dependencies (we don't want the test ones), adding a <dependency> node for each
                    configurations.implementation.get().allDependencies.forEach {
                        if(it.group != null && it.version != null){
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", it.group)
                            dependencyNode.appendNode("artifactId", it.name)
                            dependencyNode.appendNode("version", it.version)
                        }
                    }
                }
            }
        }
*/
    }

    repositories {
        maven {
            name = "GitHubPackages"
            /** Configure path of your package repository on Github
             *  Replace GITHUB_USERID with your/organisation Github userID and REPOSITORY with the repository name on GitHub
             */
            //this@maven.
            url = uri(githubPkgUrl) // Github Package
            credentials {
                //Fetch these details from the properties file or from Environment variables
                username = githubUser //githubProperties.get("gpr.usr") as String? ?: System.getenv("GPR_USER")
                password = githubApiKey //githubProperties.get("gpr.key") as String? ?: System.getenv("GPR_API_KEY")
            }
            //publish = false
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
        repo = PKG_REPOSITORY
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





fun MavenPublication.deleteGithubPkgVersion(){
    //def urlStr= "https://api.github.com/user/packages/maven/${pub.groupId}.${pub.artifactId}/versions/${pub.version}"
    val urlStr= "https://api.github.com/graphql"
    println("Querying version id $urlStr")
    val conn = `java.net`.URL(urlStr).openConnection() as `java.net`.HttpURLConnection
    conn.requestMethod = "POST"
    conn.setRequestProperty("Authorization", "bearer $githubApiKey")
    conn.setRequestProperty("Accept", "application/vnd.github.package-deletes-preview+json")
///*
    conn.doOutput= true
    try {
        //val PKG_REPOSITORY= 'SidevLib_Private'
        val os = `java.io`.DataOutputStream(conn.outputStream)
        //def data= "{\"query\":\"mutation { deletePackageVersion(input:{packageVersionId:\\\"${pub.version}\\\"}) { success }}\"}"
        //def data= "{\"query\":\"mutation { deletePackageVersion(input:{packageVersionId:\\\"MDE0OlBhY2thZ2VWZXJzaW9uNzM1Mzg3Nw==\\\"}) { success }}\"}"
        //def data= "{\"query\":\"mutation { deletePackageVersion(input:{packageName:\\\"${pub.groupId}.${pub.artifactId}\\\",packageVersionName:\\\"${pub.version}\\\"}) { success }}\"}"
        //def data= "{\"query\":\"mutation { deletePackageVersion(input:{packageName:\\\"${pub.groupId}.${pub.artifactId}\\\"}) { success }}\"}"
        println("PKG_REPOSITORY= $PKG_REPOSITORY")
        //def data= "{\"query\":\"query{repository(owner:\\\"alfalifr\\\",name:\\\"$PKG_REPOSITORY\\\"){packages(first:10){nodes{packageType,name,id,versions(first:10){nodes{id,version,readme}}}}}}\"}"
        val data= "{\"query\":\"query{repository(owner:\\\"alfalifr\\\",name:\\\"$PKG_REPOSITORY\\\"){packages(last:50){nodes{packageType,name,id,versions(last:50){nodes{id,version,readme}}}}}}\"}"
        os.write(data.toByteArray(Charsets.UTF_8))
        os.flush()
    } catch(e: Throwable){
        println("ERROR $e")
    }
// */
    println("responseCode= ${conn.responseCode} responseMessage= ${conn.responseMessage}")
    val qureyBytes= conn.inputStream.readBytes()
    println("queryRes: ========")
    val queryRes= String(qureyBytes)
    println(queryRes)

    val json= org.json.JSONObject(queryRes)
    //org.json.simple.parser.JSONParser().parse(queryRes) as org.json.simple.JSONObject //.parseText(queryRes)

    //groovy.json.Json
    val nodes= json.getJSONObject("data")
        .getJSONObject("repository")
        .getJSONObject("packages")
        .getJSONArray("nodes")

    //json.data.repository.packages.nodes
    println("nodes= $nodes")

    val versStr = nodes.find {
        (org.json.JSONObject(it.toString())).run {
            val name= getString("name")
            val vNodes= getJSONObject("versions")
                .getJSONArray("nodes")

            println("it.name= $name")
            println("it.name == \"$groupId.$artifactId\" => ${name == "$groupId.$artifactId"}")
            if(vNodes.isEmpty) false
            else {
                name == "$groupId.$artifactId" &&
                        vNodes.getJSONObject(0).getString("version") == version
            }
        }
    } ?: run {
        println("$groupId.$artifactId:$version gakda, SKIP!!!")
        return
    }

    val vers= versStr.let { if(it is org.json.JSONObject) it else org.json.JSONObject(it.toString()) }
    //.also { println("vers= $it versStr= $versStr versStr::class= ${versStr::class}") }
    val versionId= vers.getJSONObject("versions")
        .getJSONArray("nodes")
        .getJSONObject(0)
        .getString("id")

    println("Removing $urlStr $groupId.$artifactId:$version versionId= $versionId")
    val conn2 = `java.net`.URL(urlStr).openConnection() as `java.net`.HttpURLConnection
    conn2.requestMethod = "POST"
    conn2.setRequestProperty("Authorization", "bearer $githubApiKey")
    conn2.setRequestProperty("Accept", "application/vnd.github.package-deletes-preview+json")
///*
    conn2.doOutput= true
    try {
        //val PKG_REPOSITORY= 'SidevLib_Private'
        val os = `java.io`.DataOutputStream(conn2.outputStream)
        //def data= "{\"query\":\"mutation { deletePackageVersion(input:{packageVersionId:\\\"${pub.version}\\\"}) { success }}\"}"
        val data= "{\"query\":\"mutation { deletePackageVersion(input:{packageVersionId:\\\"$versionId\\\"}) { success }}\"}"
        //def data= "{\"query\":\"mutation { deletePackageVersion(input:{packageName:\\\"${pub.groupId}.${pub.artifactId}\\\",packageVersionName:\\\"${pub.version}\\\"}) { success }}\"}"
        //def data= "{\"query\":\"mutation { deletePackageVersion(input:{packageName:\\\"${pub.groupId}.${pub.artifactId}\\\"}) { success }}\"}"
        println("PKG_REPOSITORY= $PKG_REPOSITORY")
        //def data= "{\"query\":\"query{repository(owner:\\\"alfalifr\\\",name:\\\"$PKG_REPOSITORY\\\"){packages(first:10){nodes{packageType,name,id,versions(first:10){nodes{id,version,readme}}}}}}\"}"
        //def data= "{\"query\":\"query{repository(owner:\\\"alfalifr\\\",name:\\\"$PKG_REPOSITORY\\\"){packages(last:10){nodes{packageType,name,id,versions(first:10){nodes{id,version,readme}}}}}}\"}"
        os.write(data.toByteArray(Charsets.UTF_8))
        os.flush()
    } catch(e: Throwable){
        println("ERROR $e")
    }
// */
    println("responseCode= ${conn2.responseCode} responseMessage= ${conn2.responseMessage}")
    val removalBytes= conn2.inputStream.readBytes()
    println("removalRes: ========")
    val removalRes= String(removalBytes)
    println(removalRes)

    //`java.net`.URLConnection(url)
}


val deleteAllPkg= task("deleteAllPkg") {
    doFirst {
        publishing.publications.forEach {
            (it as MavenPublication).deleteGithubPkgVersion()
/*
            it.groupId = GROUP_ID
            if (it.name.contains("metadata")) {
                it.artifactId = ARTIFACT_ID.toLowerCase()
            } else if(it.name == "kotlinMultiplatform") {
                println("afterEvaluate() artifactId= ${it.artifactId}")
                it.artifactId = "${ARTIFACT_ID.toLowerCase()}-kotlin_multiplatform"
            } else {
                println("afterEvaluate() ELSE artifactId= ${it.artifactId}")
                it.artifactId = "${ARTIFACT_ID.toLowerCase()}-${it.name}"
            }
            it.deleteGithubPkgVersion()
 */
        }
    }
}

val delayedBundleReleaseAar= task("delayedBundleReleaseAar") {
    doLast {
        println("Sleeping for 2000 ms")
        sleep(2000)
    }
}
delayedBundleReleaseAar.dependsOn("bundleReleaseAar")

val delayedPublish= task("delayedPublish"){
    doFirst {
        println("Sleeping for 2000 ms")
        sleep(2000)
    }
}
delayedPublish.dependsOn(delayedBundleReleaseAar)
delayedPublish.finalizedBy("publish")

val reupload = task("reupload") {
    doFirst {
        //tasks["jsTest"].enabled = false
        //tasks["jvmTest"].enabled = false
        //tasks["allTests"].enabled = false
        //tasks["jsSourcesJar"].enabled = false
        //tasks["jvmSourcesJar"].enabled = false
        //tasks["compileTestKotlinJvm"].enabled = false
        //tasks["compileTestKotlinJs"].enabled = false
    }
}

reupload.dependsOn(deleteAllPkg)
reupload.finalizedBy(delayedPublish)
//reupload.finalizedBy(modifySource)
//reupload.finalizedBy("publish")