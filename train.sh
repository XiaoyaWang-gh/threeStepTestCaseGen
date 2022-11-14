#!/bin/bash

start=$(date +%s)

# 用需要计时的程序将下面这句替换掉
/home/xiaoyawang/anaconda3/envs/yayapy37/bin/python /data1/xiaoyawang/code/yaya_plbart/train.py 

end=$(date +%s)
take=$(( end - start ))
echo Time taken to execute commands is ${take} seconds.

let min=${take}/60
let left1=${take}-$(( ${min} * 60 ))

echo That is ${min} minutes and ${left1} seconds.

let h=${take}/3600
let left2=${min}-$(( ${h} * 60 ))

echo That is ${h} hours and ${left2} minutes.

