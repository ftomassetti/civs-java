#!/usr/bin/env bash
rm .idea -Rf
rm *.iml -f
rm core/*.iml -f
rm desktop/*.iml -f
./gradlew idea