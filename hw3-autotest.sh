#!/bin/bash

for ((  i = 1 ;  i <= 8;  i++  ))
do
	#echo "aaa$i" > /home/bolei/Desktop/test/test-$i.txt
	java -cp ".:/media/bolei/work/libary/lucene-4.3.0/*" QryEval /home/bolei/Desktop/test/parameterFile-$i.txt > ~/Desktop/test/$i-data.txt
done 

