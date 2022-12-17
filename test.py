import path
import torch as t
from transformers import AutoConfig, AutoTokenizer

from someshots import shotDict
from plbartCopyGenerator import PLBartCopyGenerator


#加载模型
config = AutoConfig.from_pretrained(path.my_checkpoint_copygen_1216)
model = PLBartCopyGenerator.from_pretrained(path.my_checkpoint_copygen_1216,config=config)
#加载分词器
tokenizer = AutoTokenizer.from_pretrained(path.hf_checkpoint)


def test_model(input):
    
    if len(input) > 1024:
        input = input[:1024]
    else:
        input = input[:len(input)]

    tokenized_input = tokenizer(input,add_special_tokens=False,return_tensors="pt")
    input_ids = tokenized_input.input_ids
    seq2seqLMOutput = model(input_ids)
    probs = t.softmax(seq2seqLMOutput.logits, dim=-1)
    output_tokens = probs.argmax(dim=-1)
    outputs = tokenizer.batch_decode(output_tokens, skip_special_tokens=True)

    print(outputs[0])
 

def main():
    for k,v in shotDict.items():
        print(k,"test prefix:")
        test_model(v)
        print()

if __name__ == "__main__":
    main()
