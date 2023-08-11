#!/bin/bash

for i in $(seq 1 9)
do
	if [ $i == 4 ]
    then
		nice -n -18 ./mytimes2 ./loopcpu & 
	else
		./mytimes2 ./loopcpu & 
	fi	
done