#!/bin/bash

./gradlew uploadArchives -PossrhUsername="${SONATYPE_USERNAME}" -PossrhPassword="${SONATYPE_PASSWORD}" -Psigning.keyId="${SIGNING_KEYID}" -Psigning.password="${SIGNING_PASSWORD}" -Psigning.secretKeyRingFile=secring.gpg -Penv=production

./gradlew closeAndReleaseRepository -PossrhUsername="${SONATYPE_USERNAME}" -PossrhPassword="${SONATYPE_PASSWORD}"
