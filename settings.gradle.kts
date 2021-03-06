rootProject.name= "SiFrame_Root"
include(":app")

include(":androidstdlib")
project(":androidstdlib").projectDir= File("/AndroidStdLib")

include(":jvm:universal")

include(":Android-External")
project(":Android-External").projectDir= File("/jvm/android/external")

include(":siframe")
project(":siframe").projectDir= File("/jvm/android/siframe")

include(":SiFrame-Customizable")
project(":SiFrame-Customizable").projectDir= File("/jvm/android/siframe/customizable")

include(":viewrap")
project(":viewrap").projectDir= File("/jvm/android/viewrap")

//include(":jvm:android:siframe:customizable")
//include(":jvm:android:viewrap")


//project(":SidevLibKt-StdLib").projectDir= File("/StdLib")