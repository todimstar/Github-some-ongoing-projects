# 九九乘法表打印
a=9
while a>=1:
    b=1
    while b<=a:
        print('{}*{}={} '.format(b,a,a*b),end="")
        b+=1
        pass
    print()
    a-=1
    pass