#TODO via sonnet
#TODO add maven caching
# Multi-stage build to keep final image smaller
FROM eclipse-temurin:20-jdk AS builder

# Install Maven - this layer changes rarely
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Create working directory
WORKDIR /build

# First copy just the foundational modules that others depend on
# This creates a cache layer for the most stable parts
COPY final/h2db/h2 h2db/h2
COPY final/cucumber-parent cucumber-parent
COPY final/cucumber-modified cucumber-modified
COPY final/pom.xml .

# try to get mvn deps cache in a separate layer
# https://stackoverflow.com/questions/42208442/maven-docker-cache-dependencies
COPY final/dep-archive /build/.m2/repository/
ENV MY_PROJECT_ROOT=/build/
COPY final/.mvn/local-settings.xml /root/.m2/settings.xml
# Need to add the pom to repo
RUN mvn install -N
#TODO does this actuially work
RUN mvn verify --fail-never

#TODO separate effective pom
# Build foundation modules #TODO this probably needs to be restructured to actually make sense for iterating on infra modification builds
RUN mvn install -f h2db/h2/pom.xml -DskipTests help:effective-pom -Doutput=h2db/h2/effective-pom.xml
RUN mvn install -f cucumber-parent/pom.xml -DskipTests help:effective-pom -Doutput=cucumber-parent/effective-pom.xml
RUN mvn install -f cucumber-modified/pom.xml -Dcheckstyle.skip=true -DskipTests help:effective-pom -Doutput=cucumber-modified/effective-pom.xml

# Now copy everything else including the parent POM
# This layer will invalidate when POMs change, but the foundation build remains cached
COPY final/ticket-service ticket-service
COPY final/ticket-service-acceptance-tests ticket-service-acceptance-tests
COPY final/ticket-service-android ticket-service-android
COPY final/ticket-service-android-maven-bridge ticket-service-android-maven-bridge
COPY final/ticket-service-cli ticket-service-cli
COPY final/ticket-service-integration ticket-service-integration
COPY final/ticket-service-web ticket-service-web

# try to get mvn deps cache in a separate layer
# https://stackoverflow.com/questions/42208442/maven-docker-cache-dependencies
#RUN mvn verify --fail-never
#TODO what does this actually end up doing...it seems to compile the (dependency?) modules?
#RUN mvn dependency:resolve

# Build everything else
RUN mvn -X help:effective-pom -Doutput=effective-pom.xml
#RUN mvn --resume-from ticket-service install -DskipTests help:effective-pom -Doutput=effective-pom.xml -P requirements-grade2,my-pluginconfigs,custom-repo
RUN mvn  -Dcheckstyle.skip -Dspring.profiles.active=ci -Drequirements.jacoco-branch-coverage=0.0 -Drequirements.jacoco-line-coverage=0.0 -Dmaven.test.failure.ignore=true compile verify test --projects ticket-service --also-make
RUN mvn  -Dcheckstyle.skip -Dspring.profiles.active=ci -Drequirements.jacoco-branch-coverage=0.0 -Drequirements.jacoco-line-coverage=0.0 -Dmaven.test.failure.ignore=true compile verify test --projects ticket-service-cli --also-make

# Create final image
FROM eclipse-temurin:20-jdk

# Install Maven in the final image too since we need it for running
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Create app directory
WORKDIR /app

# Copy the built artifacts and source from builder
COPY --from=builder /build /app
COPY --from=builder /root/.m2 /root/.m2

# Set environment variables
ENV JAVA_OPTS=""

# Default command - container will stay alive
CMD ["tail", "-f", "/dev/null"]