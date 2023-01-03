class_path=src/main/java/org/apache/commons/lang3/StringUtils.java
# 千万注意这里的数字得是三位数
line=39,0aa57f04ede369a4f813bbb86d3eac1ed20b084c,cb40e35f5e0990fad4c5278964fcc24e112dde8c
bug_id=${line: 0: 2}
hash_buggy=${line: 3: 40}
hash_fixed=${line: 44: 40}
git diff ${hash_buggy} ${hash_fixed} > /data1/xiaoyawang/d4j/recoverfiles/Lang/${bug_id}/${bug_id}.diff
git show ${hash_buggy}:${class_path} > /data1/xiaoyawang/d4j/recoverfiles/Lang/${bug_id}/${bug_id}.java