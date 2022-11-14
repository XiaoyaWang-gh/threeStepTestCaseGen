#各种import
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


#load_from_disk
processed_train_dataset = load_from_disk(path.processed_data_dir)

#选择GPU
device = torch.device("cuda:0")

def getLoader(_batch_size):
    #得到数据加载器
    loader = DataLoader(
        dataset = processed_train_dataset,
        collate_fn = dataprocess.collate_fn,
        batch_size = _batch_size,
        drop_last=True
    )
    return loader

# 训练曲线的横轴和纵轴
plot_x_epoch = []
plot_y_loss = []

#定义train() 
def train(model,_loader):
    optimizer = AdamW(model.parameters(),lr=2e-4)
    scheduler = get_scheduler(
        name = "linear",
        num_warmup_steps = 0,
        num_training_steps = len(_loader),
        optimizer = optimizer
    )
    model.train()
    minimum = 1
    for i, data in enumerate(_loader):
        data = data.to(device)
        out = model(**data)
        loss = out["loss"]

        loss.backward()
        torch.nn.utils.clip_grad_norm_(model.parameters(),1.0)#梯度裁剪，用来防止梯度爆炸

        optimizer.step()#执行优化
        scheduler.step()

        optimizer.zero_grad()#梯度清零
        model.zero_grad()

        if i%20 == 0:
            lr = optimizer.state_dict()['param_groups'][0]['lr']
            print(f'Epoch : {i:05d}, '
                    f'Loss : {loss:.4f}' ,
                    f"Lr : {lr:.6f}")
            plot_x_epoch.append(i)
            plot_y_loss.append(loss)

        if loss < minimum:
            minimum = loss
            model.save_pretrained(path.my_checkpoint)
            

def parallel_model(_model,switch):
    if(switch==1):
        # 包装为并行风格模型
        os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"
        os.environ["CUDA_VISIBLE_DEVICES"] = '2,3'
        device_ids = [0, 1]
        model = torch.nn.DataParallel(_model, device_ids=device_ids)
        return model.cuda()
    if(switch==2):
        return _model.to(device)

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--batch_size', type=int, default=4)
    args = parser.parse_args()

    #获得模型 
    model = PLBartForConditionalGeneration.from_pretrained(path.hf_checkpoint)
    model = parallel_model(model,2)

    loader = getLoader(args.batch_size)
    train(model,loader)

    x = np.array(torch.Tensor(plot_x_epoch).cpu())
    y = np.array(torch.Tensor(plot_y_loss).cpu())
    plt.plot(x,y,ls="-",c="g",lw=2,label="loss")
    plt.legend()
    plt.savefig("/data1/xiaoyawang/code/yaya_plbart/pictures/loss-11-14-1.png")


if __name__ == "__main__":
    main()