a="  a是一个刘胜飞"
# print(a[0:2]+a[4:7])#这就是切片[:]
# print(a.lstrip())
# print(a[2])#索引
print(a[::-1])#倒序 负号表方向  故a[-1:-4:1]为空
print(a[::])#正序 负号表方向
print(a[-1:-4:1])#故a[-1:-4:1]为空
print(a[-1:-4:-1])#隔一取 有就取，没有就空
print(a[-1:-4:-2])#隔二取
print(a[-1:-4:-3])#隔三取