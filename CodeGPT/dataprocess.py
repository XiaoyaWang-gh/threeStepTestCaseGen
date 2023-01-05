import path
import torch
from datasets import load_dataset,Features,Value
from transformers import AutoTokenizer
from torch.nn.functional import normalize


myTokenizer = AutoTokenizer.from_pretrained(path.hf_checkpoint)

context_feat = Features({'text': Value(dtype='string', id=None)})
dataset = load_dataset(
    path="text",
    data_dir=path.data_dir,
    data_files="input.fm.plus.fc.txt",
    split="train",
    features=context_feat
)

dataset = dataset.rename_column("text", "context")

dataset_prefix = load_dataset(
    path="text",
    data_dir=path.data_dir,
    data_files="output.prefix.txt",
    split="train"
)

dataset = dataset.add_column(
    "prefix", dataset_prefix["text"])


def normalize_fn(data,dimList): # 归一化，但是搞错了对象，没用的代码，还舍不得删
    '''对于dimList中的每一个维度
    step1 求出mean和standard deviation
    step2 对于当前维度上的所有值 减mean，除以standard deviation
    '''
    for dim in dimList:
        data_float = torch.tensor(data[dim],dtype=torch.float32)
        data[dim] = normalize(data_float,p=2.0,dim=1)

    return data


def collate_fn(data): # 在当前文件中不会被调用，作为train.py中DataLoader的参数
    # 同一批次的labels补充到同一长度 用-100

    max_length = 512 # max([len(i['labels']) for i in data])
    for i in data:
        if len(i['labels']) > max_length:
            i['labels'] = i['labels'][:max_length]
        else:
            pads = [-100] * (max_length - len(i['labels']))
            i['labels'] = i['labels'] + pads

    # 将多个数据整合成一个tensor
    data = myTokenizer.pad(
        encoded_inputs=data,
        max_length = max_length,
        padding="max_length",
        return_tensors="pt"
    )

    # data["decoder_input_ids"] = torch.full_like(#取第一个参数的形状，取第二个参数的值
    #     data["labels"],
    #     myTokenizer.get_vocab()["<pad>"],
    #     dtype=torch.long
    # )
    # data["decoder_input_ids"][:, 1:] = data["labels"][:, :-1]
    # data["decoder_input_ids"][data["decoder_input_ids"]
    #                           == -100] = myTokenizer.get_vocab()["<pad>"]

    return data


def ppfun_method(data):

    context = data["context"]
    prefix = data["prefix"]

    # 对于"源语言"context直接编码
    data["input_ids"] = myTokenizer.batch_encode_plus(
        context,
        max_length=512,  # 后续待改，最后统计一下context的均长
        truncation=True
    )["input_ids"]
    data["attention_mask"] = myTokenizer.batch_encode_plus(
        context,
        max_length=512,  # 计算出context均长600
        truncation=True
    )["attention_mask"]
    # 对"目标语言"prefix在特殊模块中编码
    with myTokenizer.as_target_tokenizer():
        data["labels"] = myTokenizer.batch_encode_plus(
            prefix,
            max_length=512,  # 计算出prefix均长284
            truncation=True
        )["input_ids"]
    data.pop("context")
    data.pop("prefix")

    return data

def main():

    processed_dataset = dataset.map(
        function=ppfun_method,
        batched=True,
        batch_size=100,
        num_proc=1
    )

    processed_dataset.save_to_disk(path.processed_data_dir)




if __name__ == '__main__':
    main()