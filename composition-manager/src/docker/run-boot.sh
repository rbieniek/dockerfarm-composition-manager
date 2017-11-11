#!/bin/bash

ADDITIONAL_ARGS=""

#if [ -f /spring-configuration/application.yml ]
#then
#	ADDITIONAL_ARGS="$ADDITIONAL_ARGS -Dspring.config.location=/spring-configuration/application.yml"
#else
#	if [ "x${application_profile}" = "x" ];	then
#		ADDITIONAL_ARGS="$ADDITIONAL_ARGS -Dspring.profiles.active=local"
#	else
#		ADDITIONAL_ARGS="$ADDITIONAL_ARGS -Dspring.profiles.active=$application_profile"
#	fi
#fi
#if [ "x$application_db_uri" != "x" ]; then
#	ADDITIONAL_ARGS="$ADDITIONAL_ARGS -Dspring.datasource.url=$application_db_uri"
#fi
#if [ "x$application_db_username" != "x" ]; then
#	ADDITIONAL_ARGS="$ADDITIONAL_ARGS -Dspring.datasource.username=$application_db_username"
#fi
#if [ "x$application_db_password" != "x" ]; then
#	ADDITIONAL_ARGS="$ADDITIONAL_ARGS -Dspring.datasource.password=$application_db_password"
#fi
#
#/bin/echo Additional args: $ADDITIONAL_ARGS

java $ADDITIONAL_ARGS -jar /spring/composition-manager-0.0.1-SNAPSHOT.jar
