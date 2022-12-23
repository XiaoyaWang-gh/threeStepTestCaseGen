#! /bin/bash

#SBATCH -J noMoreDPorDDP  # 作业名
#SBATCH -p a40  # 使用节点a40 or a30
#SBATCH -N 1  # 使用节点个数 
#SBATCH -n 2  # 使用CPU个数
#SBATCH --gres=gpu:1  # 使用gpu个数
#SBATCH -t  0-1000:00 # 最大运行时间
#SBATCH -o /share/home/22251151/code/yaya_plbart/logging/slurmOutput/12-19.o # 输出结果STDOUT
#SBATCH -e /share/home/22251151/code/yaya_plbart/logging/slurmOutput/12-19.e # 报错结果STDERR

/share/home/22251151/miniconda3/envs/yayapy37/bin/python \
/share/home/22251151/code/yaya_plbart/train.py \
--batch_size 4 \
--epochs 100 \