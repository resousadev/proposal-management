# AGENTS: How to work productively in this repository

This project is a minimal Spring Boot web application (entry: `ProposalManagementApplication.java`) with a small REST surface and a custom JSON login filter. Use the notes below to get an AI coding agent up to speed quickly.

Key concepts
- Application type: Spring Boot (plugin in `build.gradle`), main class: `src/main/java/dio/proposalmanagement/ProposalManagementApplication.java`.
- HTTP surface: `src/main/java/dio/proposalmanagement/infra/http/Controller.java` exposes endpoints: `/` (authenticated), `/influencer` (ROLE_INFLUENCER), `/brand` (ROLE_BRAND), `/session-info` (session details).
- Security: custom authentication filter `RestUsernamePasswordAuthenticationFilter` handles JSON login at `/api/auth/login`. In-memory users are defined in `SecurityConfig` (usernames `influencer` and `brand`, password `password`, roles `INFLUENCER` / `BRAND`). See `src/main/java/dio/proposalmanagement/infra/security/*`.

Why these matter for an agent
- Authentication flows: many controller methods rely on method-level security (`@PreAuthorize`). When changing endpoints or writing tests, an agent must either mock authentication or call the JSON login to obtain a session.
- The custom filter uses an injected `ObjectMapper` and parses a small record `LoginRequest(String username, String password)` (see `RestUsernamePasswordAuthenticationFilter`). Login requests must be JSON bodies, e.g. `{"username":"influencer","password":"password"}`.

Build / run / test (explicit commands)
- Build: `./gradlew build` (Windows: `gradlew.bat build`). Produces jars in `build/libs/`.
- Run app locally: `./gradlew bootRun` (Windows: `gradlew.bat bootRun`).
- Run tests: `./gradlew test` (Windows: `gradlew.bat test`).

Important implementation patterns & expectations
- Security configuration
  - `SecurityConfig` disables CSRF and permits `/api/auth/**` while requiring authentication for other endpoints. A custom filter is registered at the `UsernamePasswordAuthenticationFilter` position. When adding endpoints, update request matchers accordingly.
  - Roles are granted via `User.withUsername(...).roles("INFLUENCER")` — note Spring stores roles as `ROLE_INFLUENCER` internally; controller `@PreAuthorize("hasRole('INFLUENCER')")` matches this pattern.

- Authentication flow example (for tests or integration):
  1. POST /api/auth/login with JSON body {"username":"influencer","password":"password"} -> 200 and session cookie
  2. Use returned session cookie for subsequent GET /influencer to exercise role-restricted endpoint.

- Testing style
  - Unit tests use JUnit 5 + Mockito (see `src/test/java/dio/proposalmanagement/infra/http/ControllerTest.java`) — that test is a plain unit test that injects mocks and does not start Spring context. There is also a very small context-load test `ProposalManagementApplicationTests`.
  - When adding tests, prefer the existing mix: pure unit tests for controller logic and lightweight Spring context tests only when necessary.

Project-specific quirks to watch for
- Java toolchain: `build.gradle` sets `java.toolchain.languageVersion = 25`. Ensure CI or local environment supports the configured JDK version or update the toolchain if needed.
- JSON ObjectMapper import: `RestUsernamePasswordAuthenticationFilter` imports `tools.jackson.databind.ObjectMapper` (injection used). If you see DI errors, search for `ObjectMapper` imports or adjust to the typical `com.fasterxml.jackson.databind.ObjectMapper` depending on dependency shading.
- Session usage: `Controller.sessionInfo` reads `HttpSession.getCreationTime()` (Servlet API versions may vary). When running tests, mock or start a web environment to access session behavior.

Files to inspect when making changes
- Security: `src/main/java/dio/proposalmanagement/infra/security/SecurityConfig.java`
- Custom filter: `src/main/java/dio/proposalmanagement/infra/security/RestUsernamePasswordAuthenticationFilter.java`
- Controllers: `src/main/java/dio/proposalmanagement/infra/http/Controller.java`
- Build/config: `build.gradle`, `settings.gradle`, `src/main/resources/application.yaml`
- Tests: `src/test/java/...` (ControllerTest, ProposalManagementApplicationTests)

Quick troubleshooting checklist for agents
- If authentication fails in integration tests: confirm POST /api/auth/login JSON shape, check the injected ObjectMapper and the security filter registration order.
- If builds fail due to Java version: check `build.gradle` toolchain setting and ensure appropriate JDK is available to Gradle or adjust the languageVersion.
- If role checks behave unexpectedly: remember `roles("INFLUENCER")` corresponds to `hasRole('INFLUENCER')` in SpEL.

Where to look next
- Use `HELP.md` for reference links to Spring and Gradle docs included by maintainers.

Keep this file small and refer back to source files above for exact code examples.

