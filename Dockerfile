# ─────────────────────────────────────────────
# Stage 1 – BUILD
# Uses full JDK + Maven to compile & package.
# The fat jar is produced under target/.
# ─────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /build

# Copy dependency manifest first – lets Docker cache the layer
# as long as pom.xml does not change (faster rebuilds).
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -q 2>/dev/null || true

# Copy full source and build, skipping tests (tests run in CI).
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn package -DskipTests -q

# ─────────────────────────────────────────────
# Stage 2 – RUNTIME
# Uses slim JRE only – no compiler, no mvn.
# Results in a much smaller final image (~130 MB vs ~350 MB).
# ─────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS runtime

# Security: run as a dedicated non-root user (CWE-250 – excessive privilege)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy only the fat jar from the build stage
COPY --from=builder /build/target/game-backend-*.jar app.jar

# Ensure the log directory exists and is owned by the app user
RUN mkdir -p logs && chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

# JVM tuning for containers:
#   -XX:+UseContainerSupport   → respect cgroup memory/cpu limits
#   -XX:MaxRAMPercentage=75.0  → use max 75% of container RAM for heap
#   -Djava.security.egd        → faster entropy for SecureRandom on Linux
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
