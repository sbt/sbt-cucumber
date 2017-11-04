#!/bin/bash

sbt_cmd="sbt ++$TRAVIS_SCALA_VERSION"
all="$sbt_cmd clean compile"
eval $run_cmd
