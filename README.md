# App Development 2026 — Server Example

A complete Android app and Ktor server to explore and experiment with.
There are no homework tasks to complete.

## Run it

1. Clone your repository and open it in Android Studio.
2. Start the backend from the project root and leave it running:

   ```sh
   ./gradlew :server:run
   ```

   On Windows, use `gradlew.bat :server:run`.
3. Run the `app` configuration on an Android emulator. If prompted, allow local
   network access in the app.

The server runs at `http://localhost:8080`; the emulator connects through
`http://10.0.2.2:8080`. You can also open `http://localhost:8080/options` in your
computer's browser to see the JSON response.

## Explore

- `app/`: the Android UI and HTTP client.
- `server/`: the Ktor routes, example data, and images.
- `shared/`: data models used by both sides.

Feel free to change the data, UI, or routes and see what happens.
