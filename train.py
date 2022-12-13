import torch
import os
from transformers import PLBartForConditionalGeneration,AdamW
from transformers.optimization import get_scheduler
import path
from datasets import load_from_disk
from torch.utils.data import DataLoader
import dataprocess
import matplotlib.pyplot as plt
import numpy as np
import argparse
import time

# 指定使用的GPU
os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"
os.environ["CUDA_VISIBLE_DEVICES"] = '0,1,2,3'
device_ids = [0, 1, 2, 3]

#load_from_disk
print(help(load_from_disk))
processed_train_dataset = load_from_disk(path.new_len_processed_data_dir)
print(processed_train_dataset.__len__)

#选择GPU
device = torch.device("cuda:0")

def getLoader(_batch_size):
    
    #得到数据加载器
    loader = DataLoader(
        dataset = processed_train_dataset.shuffle(1),
        collate_fn = dataprocess.collate_fn,
        batch_size = _batch_size,
        num_workers = 8
    )
    return loader



#定义train() 
def train(model,batch_size,epochs):
    b_t = time.time()
    print("Begin time mark : ",time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    print("Epoch: ",epochs," is beginning!")

    # 不想再占用过高CPU
    torch.set_num_threads(1)
    # 训练曲线的横轴和纵轴
    plot_x_epoch = []
    plot_y_loss = []

    loader = getLoader(batch_size)
    optimizer = AdamW(model.parameters(),lr=5e-5)
    scheduler = get_scheduler(
        name = "linear",
        num_warmup_steps = 0,
        num_training_steps = len(loader),
        optimizer = optimizer
    )
    model.train()
    minimum = 1
    for i, data in enumerate(loader):
        data = data.to(device)
        model = model.to(device)
        out = model(**data)
        loss = out["loss"]

        loss.sum().backward()#.sum()是并行训练需要加的
        # torch.nn.utils.clip_grad_norm_(model.parameters(),1.0)#梯度裁剪，用来防止梯度爆炸

        optimizer.step()#执行优化
        scheduler.step()

        optimizer.zero_grad()#梯度清零
        model.zero_grad()

        if i % 50 == 0:
            lr = optimizer.state_dict()['param_groups'][0]['lr']
            print(f'Iterarion : {i:05d}, '
                    f'Loss : {loss.mean().item():.4f}' ,
                    f"Lr : {lr:.6f}")
            plot_x_epoch.append(i)
            plot_y_loss.append(loss.mean().item())

        if loss.mean().item() < minimum:
            minimum = loss.mean().item()
            # model.save_pretrained(path.my_checkpoint) 不并行就不需要module
            model.module.save_pretrained(path.my_checkpoint_new_len_1213)

    x = np.array(torch.Tensor(plot_x_epoch).cpu())
    y = np.array(torch.Tensor(plot_y_loss).cpu())
    plt.plot(x,y,ls="-",c="g",lw=2,label="loss")
    plt.legend()
    plt.savefig("/data1/xiaoyawang/code/yaya_plbart/pictures/12-13/loss-epoch-"+str(epochs)+".png")
    e_t = time.time()
    print("End time mark : ",time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    cost_sec = e_t - b_t
    cost_min = cost_sec//60
    cost_hour = cost_min//60

    print("From begin to end, it cost",cost_hour,"hours and",cost_min-cost_hour*60,"minutes.")
    print("Epoch: ",epochs," ended!")

def parallel_model(_model,switch):
    if(switch==1):
        # 包装为并行风格模型
        model = torch.nn.DataParallel(_model, device_ids=device_ids)
        return model.cuda()
    if(switch==2):
        return _model.to(device)

def get_parameter_number(model):
    total_num = sum(p.numel() for p in model.parameters())
    trainable_num = sum(p.numel() for p in model.parameters() if p.requires_grad)
    return {'Total': total_num, 'Trainable': trainable_num}


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--batch_size', type=int, default=16)#
    parser.add_argument('--epochs', type=int, default=70)#
    args = parser.parse_args()

    #获得模型 
    model = PLBartForConditionalGeneration.from_pretrained(path.my_checkpoint_new_len_1211)
    print(get_parameter_number(model))#打印模型的参数量和可训练的参数量
    print(model)
    model = parallel_model(model,1)

    
    print("batch_size = ",args.batch_size)
    epochs = args.epochs
    print("epochs = ",epochs)
    for i in range(1,epochs+1):
        train(model,args.batch_size,i)

    


if __name__ == "__main__":
    main()