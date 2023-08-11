#!/bin/bash
echo "ip addresse de PC : " 
echo `ifconfig | grep 'inet ' | sed '1d' | cut -d " " -f 2` 

