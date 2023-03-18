val scala2 = "2.13.10"
val scala3 = "3.2.2"

// A
val `mutually-exclusive` = projectMatrix
  .in(file("mutually-exclusive"))
  .jvmPlatform(List(scala2))
val `mutually-exclusive-build` = project.aggregate(`mutually-exclusive`.projectRefs: _*)

// B
val `scala3-extension-core` = projectMatrix
  .in(file("scala3-extension/core"))
  .jvmPlatform(List(scala2))
val `scala3-extension-extension` = projectMatrix
  .in(file("scala3-extension/extension"))
  .jvmPlatform(List(scala3))
  .settings(scalacOptions += "-Xignore-scala2-macros")
  .dependsOn(`scala3-extension-core`)
val `scala3-extension-build` = project.aggregate((`scala3-extension-core`.projectRefs ++ `scala3-extension-extension`.projectRefs): _*)

// C
val `scala2-internal-internal` = projectMatrix
  .in(file("scala2-internal/internal"))
  .jvmPlatform(List(scala2))
val `scala2-internal-main` = projectMatrix
  .in(file("scala3-internal/main"))
  .jvmPlatform(List(scala2, scala3))
  .settings(scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((3, _)) => Seq("-Xignore-scala2-macros")
      case _ => Seq.empty
    }
  })
  .dependsOn(`scala2-internal-internal`)
val `scala2-internal-build` = project.aggregate((`scala2-internal-internal`.projectRefs ++ `scala2-internal-main`.projectRefs): _*)

// D
val `runtime-and-extension-methods-runtime` = projectMatrix
  .in(file("runtime-and-extension-methods/runtime"))
  .jvmPlatform(List(scala2))
val `runtime-and-extension-methods-shared` = projectMatrix
  .in(file("runtime-and-extension-methods/shared"))
  .jvmPlatform(List(scala2))
  .dependsOn(`runtime-and-extension-methods-runtime`)
val `runtime-and-extension-methods-extensions` = projectMatrix
  .in(file("runtime-and-extension-methods/extensions"))
  .jvmPlatform(List(scala2, scala3))
  .settings(scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((3, _)) => Seq("-Xignore-scala2-macros")
      case _ => Seq.empty
    }
  })
  .dependsOn(`runtime-and-extension-methods-shared`)
val `runtime-and-extension-methods-runtime-build` = project.aggregate((`runtime-and-extension-methods-runtime`.projectRefs ++ `runtime-and-extension-methods-shared`.projectRefs ++  `runtime-and-extension-methods-extensions`.projectRefs): _*)
