# 算术运算符 #  +  -   *  /  %    **   //
#            # 加  减  乘 除 整除 指数  地板除
a=3
b,c=10,20
print(a+b)
print(a-b)
print(a*b)
print(a/b)
print(a%b)
print(a**b)
print(2**10)
print(a//b)

#比较运算符  #  ==    !=    >    <     >=      <=
             # 等于 不等于 大于 小于 大于等于 小于等于
print(a==b)
print(a!=b)
print(a>b)
print(a<b)
print(a>=b)
print(a<=b)

#逻辑运算符  # and   or   not
             #  和   或   否
# and 双真才真，一假全假 &&
# or 双假才假，一真全真  ||
# not 真变假，假变真     !         ^是xor?

#优先级：( ) > not > and > or
print("-------------------------------------")
a=((not 1<2) and (10>11) or (100>1) and (23<24))
print(a)
print(type(a))
a=str(a)
if a=='True':
    print('这次是%s的'%a)