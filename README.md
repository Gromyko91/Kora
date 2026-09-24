# Kora

Kora is an Android app built with Kotlin and Jetpack Compose. Its Firebase configuration points to the Firebase project `kora-firebase-project` and registers the Android package `com.example.kora`.

## Backend

The app uses Firebase Authentication, Cloud Firestore, Cloud Storage, and Firebase Cloud Messaging. Open the [Firebase console](https://console.firebase.google.com/project/kora-firebase-project/overview) with the Google account used to create this project. Firestore data is under **Firestore Database**, sign-in methods are under **Authentication**, and uploaded images are under **Storage**.

Enable Firestore, Authentication, Storage, and Cloud Messaging in the Firebase console as needed. The Android package name and Firebase registration must match.

## Put this source in your GitHub account

The source is hosted at <https://github.com/Gromyko91/Kora>. Push future changes from the project folder with:

```powershell
git add .
git commit -m "Initial Kora project"
git push
```

Authenticate with GitHub when prompted. Do not commit passwords, service account keys, signing keys, or other private credentials. `local.properties` is ignored by Git.
