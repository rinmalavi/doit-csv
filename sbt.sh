#!/bin/bash
JVM_PARAMS="-Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M"

GRUJ_PATH="./project/strap/gruj_vs_sbt-launch-0.13.x.jar"

java $JVM_PARAMS -jar $GRUJ_PATH $LOG_LEVEL $SBT_PARAMS
