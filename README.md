# FRC Custom Dashboard!  [![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://shields.io/)  

This is a custom frc dashboard for competiton, it allows us to visualize the data how we like with the power of js, html, and css.

## Table of contents
* [Custom FRC Dashboard](#kotlin-robot-base)
* [Why and How](#why-and-why)
* [Installation](#installation)
* [License](#license)

## Why and How
We wanted to create a custom modular way to pass data between the driver station and the robot and to create our own modular elements to be on the dashboard. for example, a log file could be shown in the custom dashboard or a camera feed with marked targets over the video. 
Using the [Spark](http://sparkjava.com/) framework, we are hosting a server on the robot containing the dashboard design files (HTML/CSS), And when a user is connected to the robot, he will be able to access those files throw the browser, like a website. Then using the [WebSocket protocol](https://en.wikipedia.org/wiki/WebSocket) we send data updates from the robot to the browser and vice versa.

## Installation
If you are using Gradle, good news because that's the easiest way to add the project to your code.

1. Add the jitpack repository to your build.gradle file
    ```
    allprojects {
	    repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	```
2. Add the project to your dependencies
    ```
	dependencies {
		implementation 'com.github.tiger-team-2679:frc-web-dashboard:$Version'
	}
	```

You are all set, now all you have to do is customize the dashboard as you like!

## License
[MIT](LICENSE) - Tiger Team #2679 - 2018
