Releasing
=========

The process of deploying to maven central has been automated based on 
the [Complete guide to continuous deployment to maven central from Travis CI](http://www.debonair.io/post/maven-cd/)
and will be executed whenever a non-snapshot version is committed.

## Check [![Build Status](https://travis-ci.org/cucumber/cucumber-parent.svg?branch=master)](https://travis-ci.org/cucumber/cucumber-parent) ##

Is the build passing?

```
git checkout master
```

Also check if you can upgrade any dependencies:

```
mvn versions:display-plugin-updates
mvn versions:display-dependency-updates
```

## Make the release ##

Now release everything:

```
mvn release:clean release:prepare -DautoVersionSubmodules=true -Darguments="-DskipTests=true"  
```

Ping a maintainer to deploy the release.

It is preferable to use the automated deployment process over the manual process. However should travis.ci fail or should the 
need arise to setup another continuous integration system the [Manual deployment](#manual-deployment) section 
describes how this works.

# Manual deployment #

It is preferable to use the automated deployment process over the manual process.

The deployment process of `cucumber-jvm` is based on 
[Deploying to OSSRH with Apache Maven](http://central.sonatype.org/pages/apache-maven.html#deploying-to-ossrh-with-apache-maven-introduction).
This process is nearly identical for both snapshot deployments and releases. Whether a snapshot 
deployment or release is executed is determined by the version number.

To make a release you must have the `devs@cucumber.io` GPG private key imported in gpg2.

```
gpg --import devs-cucumber.io.key
```

Additionally upload privileges to the Sonatype repositories are required. See the 
[OSSRH Guide](http://central.sonatype.org/pages/ossrh-guide.html) for instructions. Then an 
administrator will have to grant you access to the cucumber repository.

Finally both your OSSRH credentials and private key must be setup in your `~/.m2/settings.xml` - 
for example:

```
<settings>
    <servers>
        <server>
            <id>ossrh</id>
            <username>sonatype-user-name</username>
            <password>sonatype-password</password>
        </server>
    </servers>
    <profiles>
        <profile>
            <id>ossrh</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <gpg.executable>gpg2</gpg.executable>
                <gpg.useagent>true</gpg.useagent>
            </properties>
        </profile>
        <profile>
            <id>sign-with-cucumber-key</id>
            <properties>
                <gpg.keyname>dev-cucumber.io-key-id</gpg.keyname>
            </properties>
        </profile>
    </profiles>
</settings>
```


# Deploy the release #

```
mvn release:perform -Psign-source-javadoc -DskipTests=true
```

## Using the monorepo settings

Another way to release is to use the settings and encrypted secrets from the monorepo:

```
source ../cucumber/secrets/.bash_profile
mvn release:perform -Psign-source-javadoc -DskipTests=true --settings ../cucumber/.templates/java/scripts/ci-settings.xml
```
