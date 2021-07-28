import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

lazy val `keysemaphore` = project.in(file("."))
  .disablePlugins(MimaPlugin)
  .settings(publish / skip := true)
  .aggregate(core, docs)

lazy val core = project.in(file("core"))
  .disablePlugins(MimaPlugin)
  .settings(commonSettings)
  .settings(
    name := "keysemaphore"
  )

lazy val docs = project.in(file("docs"))
  .settings(commonSettings)
  .dependsOn(core)
  .disablePlugins(MimaPlugin)
  .enablePlugins(MicrositesPlugin)
  .settings(publish / skip := true)
  .settings{
    import microsites._
    Seq(
      micrositeName := "keysemaphore",
      micrositeDescription := "Keyed Semaphores",
      micrositeAuthor := "Christopher Davenport",
      micrositeGithubOwner := "ChristopherDavenport",
      micrositeGithubRepo := "keysemaphore",
      micrositeBaseUrl := "/keysemaphore",
      micrositeDocumentationUrl := "https://www.javadoc.io/doc/io.chrisdavenport/keysemaphore_2.12",
      micrositeFooterText := None,
      micrositeHighlightTheme := "atom-one-light",
      micrositePalette := Map(
        "brand-primary" -> "#3e5b95",
        "brand-secondary" -> "#294066",
        "brand-tertiary" -> "#2d5799",
        "gray-dark" -> "#49494B",
        "gray" -> "#7B7B7E",
        "gray-light" -> "#E5E5E6",
        "gray-lighter" -> "#F4F3F4",
        "white-color" -> "#FFFFFF"
      ),
      scalacOptions --= Seq(
        "-Xfatal-warnings",
        "-Ywarn-unused-import",
        "-Ywarn-numeric-widen",
        "-Ywarn-dead-code",
        "-Ywarn-unused:imports",
        "-Xlint:-missing-interpolator,_"
      ),
      micrositePushSiteWith := GitHub4s,
      micrositeGithubToken := sys.env.get("GITHUB_TOKEN"),
      micrositeExtraMdFiles := Map(
          file("CHANGELOG.md")        -> ExtraMdFileConfig("changelog.md", "page", Map("title" -> "changelog", "section" -> "changelog", "position" -> "100")),
          file("CODE_OF_CONDUCT.md")  -> ExtraMdFileConfig("code-of-conduct.md",   "page", Map("title" -> "code of conduct",   "section" -> "code of conduct",   "position" -> "101")),
          file("LICENSE")             -> ExtraMdFileConfig("license.md",   "page", Map("title" -> "license",   "section" -> "license",   "position" -> "102"))
      )
    )
  }

val catsV = "2.6.1"
val catsEffectV = "3.2.0"
val munitCatsEffectV = "1.0.5"
val kindProjectorV = "0.13.0"

// General Settings
lazy val commonSettings = Seq(
  scalaVersion := "2.13.6",
  crossScalaVersions := Seq(scalaVersion.value, "2.12.14"),
  libraryDependencies ++= Seq(
    "org.typelevel"               %% "cats-core"                  % catsV,
    "org.typelevel"               %% "cats-effect"                % catsEffectV,
		"org.typelevel" %%% "munit-cats-effect-3" % munitCatsEffectV % Test
	) ++
  // format: off
  (if (scalaVersion.value.startsWith("2"))
    Seq(compilerPlugin("org.typelevel" %% "kind-projector" % kindProjectorV).cross(CrossVersion.full))
  else Nil)
  // format: on
)

// General Settings
inThisBuild(List(
  organization := "io.chrisdavenport",
  developers := List(
    Developer("ChristopherDavenport", "Christopher Davenport", "chris@christopherdavenport.tech", url("https://github.com/ChristopherDavenport"))
  ),

  homepage := Some(url("https://github.com/ChristopherDavenport/keysemaphore")),
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),

  pomIncludeRepository := { _ => false},
  Compile / doc / scalacOptions ++= Seq(
      "-groups",
      "-sourcepath", (LocalRootProject / baseDirectory).value.getAbsolutePath,
      "-doc-source-url", "https://github.com/ChristopherDavenport/keysemaphore/blob/v" + version.value + "€{FILE_PATH}.scala"
  ),
))
