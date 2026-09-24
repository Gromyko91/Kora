# Kora

Kora is an Android app built with Kotlin and Jetpack Compose. Its current Firebase configuration points to the Firebase project `kora-bafed` and registers the Android package `com.example.kora`.

## Backend

The app uses Firebase Authentication, Cloud Firestore, Cloud Storage, and Firebase Cloud Messaging. Open the Firebase console at <https://console.firebase.google.com/> and sign in with the Google account that owns or has been granted access to `kora-bafed`. In that project, the Firestore data is under **Firestore Database**, user sign-in settings are under **Authentication**, and uploaded images are under **Storage**.

If `kora-bafed` belongs to someone else, ask its owner to add your Google account to the Firebase project with an appropriate role, or create a new Firebase project that you own. For a new project, register an Android app with the exact package name `com.example.kora`, enable the Firebase services this app uses, and replace `app/google-services.json` with the configuration file downloaded for that app. The Android package name and Firebase registration must match.

## Put this source in your GitHub account

This folder may be a downloaded source copy rather than a Git checkout. To publish it under `Gromyko91`, create a new repository in that GitHub account, then run these commands from the project folder (replace `REPOSITORY` with the repository name you chose):

```powershell
git init
git add .
git commit -m "Initial Kora project"
git branch -M main
git remote add origin https://github.com/Gromyko91/REPOSITORY.git
git push -u origin main
```

Authenticate with GitHub when prompted. Do not commit passwords, service account keys, signing keys, or other private credentials. `local.properties` is ignored by Git.
