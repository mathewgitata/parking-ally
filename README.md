# Parking Ally

This is a utility app that aims at guiding motorists to discover parking areas whenever, wherever.

# BDD
| Input                                      | Behavior                                 | Output                                        |
|--------------------------------------------|------------------------------------------|-----------------------------------------------|
| The user launches the app                  | The app fetches the landing activity        | The user is presented with the landing page view |
| The user follows either the SignUp or Login              | The app gets the specified activity           | The user is taken to the appropriate view        |
| The user clicks on availed parking lots | The app shows the user the specific's parking lot's status | The user sees available spaces   |
|User follows Account Settings navigation bar menu | App renders their registered information | User is shown the respective information|
|User follows a view, on their respective information| App loads the edit account activity | User presented with dialogs to input their updated information |

### Prerequisites

* Android SDK v4
* Latest Android Build Tools
* Android Support Repository

## Getting Started

This sample uses the Gradle build system.
1. Download the samples by cloning this repository or downloading an archived snapshot here **[parking-ally](https://github.com/gitatam/parking-ally.git)**.
2. In Android Studio, create a new project and choose the "Import Project" option.
3. If prompted for a gradle configuration, accept the default settings. Alternatively use the "gradlew build" command to build the project directly.


### Installing

To configure the ParkingAlly App follow these instructions.
1. Open Android Studio.
2. In the Quick Start menu, select Open an existing Android Studio project.
3. Select the parking-ally directory you cloned from the GitHub repository.
4. You can be sure you imported this correctly if you can locate in the project explorer the source files.
5. In the Android Studio top menu bar, select Build -> Make Project.
6. In Android Studio, click the Run menu and select Run app.
7. Select your Physical Device or the  Virtual Device you are going to test using.
7. Click OK.

The Sign up/ Sign In screen will appear and you can now use the Parking Ally App during the demo.


## Built With

* [Gradle](https://gradle.org/) - The build automation system used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Butter Knife](https://jakewharton.github.io/butterknife/) - View binding tool

## Versioning

ParkingAlly-v.1.0

# Screenshots
| ![<img src="/readme/01_landing_page.jpg"](/readme/01_landing_page.jpg)     | ![<img src="/readme/02_registration_page.jpg"](/readme/02_registration_page.jpg) | ![<img src="/readme/03_login_page.jpg"](/readme/03_login_page.jpg)   |
|-------------------------------------------------------|-----------------------------------------------------|-------------------------------------------------------|
| ![<img src="/readme/05_account_details.jpg"](/readme/05_account_details.jpg)  | ![<img src="/readme/06_edit_details.jpg"](/readme/06_edit_details.jpg) | ![<img src="/readme/04_parking_lots.jpg"](/readme/04_parking_lots.jpg)   |
| --- | --- | --- |
| ![<img src="/readme/07_reserve_parking_space.jpg"](/readme/07_reserve_parking_space.jpg) |




# Known bugs
* None at the moment

## Authors

* [Mathew Gitata](https://github.com/gitatam)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License and Copyright information

This project is licensed under the MIT License.

The MIT License (MIT)

Copyright (c) 2019, Mathew Gitata.

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


## Acknowledgments

* MS Community
