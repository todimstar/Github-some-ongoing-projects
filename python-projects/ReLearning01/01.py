row=1
while row<=9:
    a=1
    while a<=9-row:
        print(' ',end='')
        a+=1
    b=1
    while b<=2*row-1:#各种函数的意思 #什么玩意，就是个推算出的公式不是

        print("#",end="")
        b+=1
    print()
    row+=1

