#!/bin/bash

usage="$0 <directory with jar files>"

[ $# -lt 1 ] && echo usage && exit 1
dir=$1
res=""
for f in $1/*.jar ; do 
	res=$(readlink -f $f):$res
done

echo $res
