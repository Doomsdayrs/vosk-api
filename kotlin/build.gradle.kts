import org.jetbrains.dokka.gradle.DokkaTask

/*
 * Copyright 2020 Alpha Cephei Inc. & Doomsdayrs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
	kotlin("multiplatform") version "1.7.21"
	id("com.android.library")
	`maven-publish`
	id("org.jetbrains.dokka") version "1.7.20"
}

group = "com.alphacephei"
version = "0.0.0"

repositories {
	google()
	mavenCentral()
}

val dokkaOutputDir = "$buildDir/dokka"

tasks.getByName<DokkaTask>("dokkaHtml") {
	outputDirectory.set(file(dokkaOutputDir))
}

val deleteDokkaOutputDir by tasks.register<Delete>("deleteDokkaOutputDirectory") {
	delete(dokkaOutputDir)
}

val javadocJar = tasks.register<Jar>("javadocJar") {
	dependsOn(deleteDokkaOutputDir, tasks.dokkaHtml)
	archiveClassifier.set("javadoc")
	from(dokkaOutputDir)
}
kotlin {
	jvm {
		compilations.all {
			kotlinOptions.jvmTarget = "17"
		}
		testRuns["test"].executionTask.configure {
			useJUnitPlatform()
		}
	}
	/*
	mingwX64 {
		binaries {
			executable {
				entryPoint = "main"
			}
		}
	}
	linuxX64 {
		binaries {
			executable {
				entryPoint = "main"
			}
		}
	}
	 */
	android {
		publishAllLibraryVariants()
	}

	publishing {
		publications {
			withType<MavenPublication> {
				artifact(javadocJar)
				pom {
					url.set("http://www.alphacephei.com.com/vosk/")
					licenses {
						license {
							name.set("The Apache License, Version 2.0")
							url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
						}
					}
					developers {
						developer {
							id.set("com.alphacephei")
							name.set("Alpha Cephei Inc")
							email.set("contact@alphacephei.com")
						}
					}
					scm {
						connection.set("scm:git:git://github.com/alphacep/vosk-api.git")
						url.set("https://github.com/alphacep/vosk-api/")
					}
				}
			}
		}
	}

	sourceSets {
		val commonMain by getting
		val commonTest by getting {
			dependencies {
				implementation(kotlin("test"))
			}
		}
		val jvmMain by getting {
			dependencies {
				api("net.java.dev.jna:jna:5.12.1")
			}
		}
		val jvmTest by getting
		//val mingwX64Main by getting
		//val mingwX64Test by getting
		//val linuxX64Main by getting
		//val linuxX64Test by getting
		val androidMain by getting {
			dependsOn(jvmMain)
			dependencies {
				api("net.java.dev.jna:jna:5.12.1@aar")
			}
		}
		val androidTest by getting {
			dependencies {
				implementation("junit:junit:4.13.2")
			}
		}
	}
}

android {
	compileSdk = 33
	sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
	defaultConfig {
		minSdk = 24
		targetSdk = 33
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
}