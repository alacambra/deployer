#!/bin/bash
sudo service docker stop
sudo docker -d -H tcp://0.0.0.0:5555 --api-enable-cors &