class_path=src/com/google/javascript/jscomp/TypeInference.java
# 千万注意这里的数字得是三位数，位数改动截取字符串的起始位置要跟着改动
line=176,d6eebf3357f6f06b44cefbe55aabeab5bf25216e,aeed47f424d93d9ff82e0782fca53259829362b1
bug_id=${line: 0: 3}
hash_buggy=${line: 4: 40}
hash_fixed=${line: 45: 40}
git diff ${hash_buggy} ${hash_fixed} > /data1/xiaoyawang/d4j/recoverfiles/Closure/${bug_id}/${bug_id}.diff
git show ${hash_buggy}:${class_path} > /data1/xiaoyawang/d4j/recoverfiles/Closure/${bug_id}/${bug_id}.java