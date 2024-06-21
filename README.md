# BABY METER
BabyMeter is a mobile application designed to assist parents and caregivers in monitoring the growth and development of their babies. This application allows users to input their baby's data, including photos, and receive predictions about the baby's health based on the provided information.

## Features
- User Authentication: Secure user authentication using Firebase Authentication.
- Baby Data Input: Input fields for National Identification Number (NIK), baby's name, age, weight, and photo.
- Health Predictions: Backend communication to receive health predictions based on input data.
- Save Prediction Results: Store prediction results in Firebase Firestore.
- View History: Access and view the history of saved predictions.
- Real-time Data Sync: Synchronize data in real-time with Firebase Firestore.

## Technologies Used
This application was built using :
- Platform: Android
- Programming Language: Kotlin
- UI Framework: Android Jetpack and Material Design components
- Architecture: MVVM (Model-View-ViewModel)
- Authentication: Firebase Authentication
- Database: Firebase Firestore
- Networking: OkHttp and Retrofit for HTTP requests
- Concurrency: Kotlin Coroutines for asynchronous operations
- Image Handling: Glide for image loading and handling

## Project Structure
- Activities: MainActivity, LoginActivity, RegisterActivity
- Fragments: InputDataFragment, ResultFragment, HistoryFragment
- ViewModels: AuthViewModel, InputDataViewModel, HistoryViewModel
- Repository: AuthRepository, DataRepository
- Network: ApiService, ApiClient
- Utils: Constants, Extensions
  
## Screenshot
![MOCKUP](https://storage.googleapis.com/asset-design/asset%20for%20MD/Cuplikan%20layar%202024-06-21%20162459.png)
![MOCKUP](https://storage.googleapis.com/asset-design/asset%20for%20MD/Cuplikan%20layar%202024-06-21%20162459.png)

## Demo Application
https://drive.google.com/file/d/1v1-_jw7RVEMkoWLODLRHzkQbZFQRQJuG/view?usp=drivesdk
