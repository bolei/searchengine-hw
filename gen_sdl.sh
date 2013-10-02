#!/bin/sh
while read line           
do           
	./dm.pl "$line" $1 $2 $3
done 
