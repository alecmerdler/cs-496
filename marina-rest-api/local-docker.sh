#!/bin/bash

docker run -it --net=host -v $PWD:/usr/src/app quay.io/alecmerdler/marina-rest-api:latest

