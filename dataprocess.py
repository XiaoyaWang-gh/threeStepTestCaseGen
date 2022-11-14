import path
import somevariable
import torch
from datasets import load_dataset,Features,Value,ClassLabel
from transformers import PLBartTokenizer

myTokenizer = PLBartTokenizer.from_pretrained(path.hf_checkpoint)

def collate_fn(data):
    # 同一批次的labels补充到同一长度 用-100

    max_length = max([len(i['labels']) for i in data])
    for i in data:
        pads = [-100] * (max_length - len(i['labels']))
        i['labels'] = i['labels'] + pads

    # 将多个数据整合成一个tensor
    data = myTokenizer.pad(
        encoded_inputs=data,
        padding="longest",
        max_length=1024,
        pad_to_multiple_of=None,
        return_tensors="pt"
    )

    data["decoder_input_ids"] = torch.full_like(
        data["labels"],
        myTokenizer.get_vocab()["<pad>"],
        dtype=torch.long
    )
    data["decoder_input_ids"][:, 1:] = data["labels"][:, :-1]
    data["decoder_input_ids"][data["decoder_input_ids"]
                              == -100] = myTokenizer.get_vocab()["<pad>"]

    return data
# print(help(load_dataset))

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


def ppfun_method(data):

    context = data["context"]
    prefix = data["prefix"]

    # 对于"源语言"context直接编码
    data["input_ids"] = myTokenizer.batch_encode_plus(
        context,
        max_length=1024,  # 后续待改，最后统计一下context的均长
        truncation=True
    )["input_ids"]
    data["attention_mask"] = myTokenizer.batch_encode_plus(
        context,
        max_length=1024,  # 计算出context均长600
        truncation=True
    )["attention_mask"]
    # 对"目标语言"prefix在特殊模块中编码
    with myTokenizer.as_target_tokenizer():
        data["labels"] = myTokenizer.batch_encode_plus(
            prefix,
            max_length=1024,  # 计算出prefix均长284
            truncation=True
        )["input_ids"]
    data.pop("context")
    data.pop("prefix")

    return data

def main():

    # 先尝试只用一部分进行训练
    part_dataset = dataset.shuffle(1).select(range(somevariable.train_data_num))

    processed_dataset = part_dataset.map(
        function=ppfun_method,
        batched=True,
        batch_size=100,
        num_proc=1
    )

    processed_dataset.save_to_disk(path.processed_data_dir)




if __name__ == '__main__':
    main()