import path
import torch as t
from transformers import AutoConfig, AutoTokenizer
from plbartCopyGenerator import PLBartCopyGenerator

#每个defects4j中的项目作为一个.txt文件
file_path = path.pro_lang

#加载模型
trained_path = path.my_ckpt_epoch_6
config = AutoConfig.from_pretrained(trained_path)
model = PLBartCopyGenerator.from_pretrained(trained_path,config=config)
#加载分词器
tokenizer = AutoTokenizer.from_pretrained(path.hf_checkpoint)


# 读取d4j中的focal methods,key是项目名称+缺陷序号
def read_file()->dict():
    langDict = dict()
    with open(file_path,"r") as file:
        for line in file:
            key, value = line.split(',',1)
            langDict[key] = value
    return langDict

def cut_seq(input) -> str:
    if len(input) > 1024:
        input = input[:1024]
    return input

def test_model(input): # 不用Generate
    tokenized_input = tokenizer(input,add_special_tokens=False,return_tensors="pt")
    input_ids = tokenized_input.input_ids
    seq2seqLMOutput = model(input_ids)
    probs = t.softmax(seq2seqLMOutput.logits, dim=-1)
    output_tokens = probs.argmax(dim=-1)
    outputs = tokenizer.batch_decode(output_tokens, skip_special_tokens=True)

    print(outputs[0])

def test_model_gen(input): # 使用Generate()
    tokenized_input = tokenizer(input,add_special_tokens=False,return_tensors="pt")
    input_ids = tokenized_input.input_ids
    # 对生成做要求
    gen_kwargs = {
        "no_repeat_ngram_size":3,
        "max_length":200,
        "min_length":40,
    }
    outputs = model.generate(inputs=input_ids,**gen_kwargs)
    output_tokens = tokenizer.batch_decode(outputs, skip_special_tokens=True)
    print(output_tokens[0])
    pass
 

def main():
    langDict = read_file()
    for k,v in langDict.items():
        print(k,"😃")
        print("focal method 👇\n",v)
        print("focal method 👆")
        print("test prefix 👇")
        v = cut_seq(v)
        test_model_gen(v)
        print("test prefix 👆 ")


if __name__ == "__main__":
    main()
