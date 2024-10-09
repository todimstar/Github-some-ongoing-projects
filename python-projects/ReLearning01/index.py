# + - * / // % **           //是整除，跟c一样，，，/是当浮点数的除，返回浮点数，余数转换为.几
# =  +=  -=  *=  /=  //=  **=  %= 是赋值运算符
# =是赋值，{==是判断 >= <= !=（这是不等于） < >}是比较运算符

# print('你好你好，我不好')
# a,b=100,2
# a/=b
# print(a)

# {}型占位为{} .format组合

# gril=input('(〃^ω^) 你喜欢的人是：')
# age=input('\（￣︶￣）/ 这次是他/她过多少岁生日：')
# day=input('ε(┬┬﹏┬┬)3暗恋人家的天数：')
# print('恭喜你！{}!(๑╹◡╹)ﾉ"""今日是你{}岁生辰\n(灬°ω°灬) 其实也是我喜欢你的第{}天，思来想去只希望你给我个回答，谢谢！(ღ˘︶˘ღ)'.format(gril,age,day))

# 自动生成卡片系统（input的运用）

# print('(σﾟ∀ﾟ)σ..:*      d=====(￣▽￣*)b \n       自动生成名片系统\n')
# company=input('您任职的公司名称：')
# name=input('请输入您的姓名：')
# age=input("请输入您的年龄：")
# pone=input('希望写在名片上的联系方式：')
# addr=input('您要在名片上注明的地址;')
# print("这是你的名片啦，请收好咯！\n-----------------------------------------------------------------------")#不知道这行会不会直接与名片一起显示
# print('          {}\n姓名：{}          ┗( ▔, ▔ )┛\n年龄：{}          (✪ω✪) \n联系方式：{}          (^_−)☆\n联系地址：{}          (oﾟ▽ﾟ)o  '.format(company,name,age,pone,addr))
# print("-----------------------------------------------------------------------")

# 幸运数字（%d %s %f 型占位）

# import random
# num=random.randrange(1,9)
# print('今天你的幸运数字是%d'%num)

# if条件句： elif：   else：

# score=int(input("你的成绩..."))
# if score>90:
#     print("90以上")
#     # pass
# elif score>80:
#     print('80以上')
#     # pass
# elif score>70:
#     print("区区70几")
#     # pass
# else:
#     print("你是三十三中的学生吗？")

# import random#引入随机数
# person=int(input('你出什么呢...（1石头 2剪刀 3布）'))#想把输入的中文定义为数字，电脑好判断
# #石头=0,剪刀=1,布=2#为啥布能定义，前两者不行
# computer=random.randint(1,3)
# if computer==person:
#     print('computer出了%d'%computer)
#     print('哟，跟我想的一样，平局')
#     pass
# elif (computer==1 and person==2):
#     print('computer出了%d'%computer)
#     print('，嘿嘿，我赢啦！')
#     pass
# elif (computer==2 and person==3):
#     print('computer出了%d'%computer)
#     print('，嘿嘿，我赢啦！')
# elif (computer==3 and person==1):
#     print('computer出了%d'%computer)
#     print('，嘿嘿，我赢啦！')
# else:
#     print('computer出了%d' % computer)
#     print('霍，看来你技高一筹，我输了')

# if嵌套

# while 循环之打印1到100数字

# index=1
# while index<=100:
#     print(index)
#     index+=1
#     pass

# while 循环猜拳

xuhuan=1
while xuhuan<=5:
    import random  # 引入随机数
    person = int(input('你出什么呢...（1石头 2剪刀 3布）'))  # 想把输入的中文定义为数字，电脑好判断
    # 石头=0,剪刀=1,布=2#为啥布能定义，前两者不行
    computer = random.randint(1, 3)
    if computer == person:
        print('computer出了%d' % computer)
        print('哟，跟我想的一样，平局')
        pass
    elif computer == 1 and person == 2:
        print('computer出了%d' % computer)
        print('嘿嘿，我赢啦！')
        pass
    elif computer == 2 and person == 3:
        print('computer出了%d' % computer)
        print('嘿嘿，我赢啦！')
    elif computer == 3 and person == 1:
        print('computer出了%d' % computer)
        print('嘿嘿，我赢啦！')
    else:
        print('computer出了%d' % computer)
        print('霍，看来你技高一筹，我输了')
        pass
    a=5-xuhuan
    print('五局三胜还剩%d次'%a)
    xuhuan+=1

# 九九乘法表打印（while循环）
# a=1
# while a<=9:
#     b=1
#     while b<=a:
#         print('{}*{}={} '.format(b,a,a*b),end="")
#         b+=1
#         pass
#     print()
#     a+=1
#     pass

# 直角三角形打印
# row=1
# while row<=9:
#     b=9
#     while b>=row:
#         print('#',end=" ")
#         b-=1
#     print()
#     row+=1
#
# # 正三角形打印（函数尝试版）
# row=1
# while row<=9:
#     a=1
#     while a<=9-row:
#         print(' ',end='')
#         a+=1
#     b=1
#     while b<=2*row:#各种函数的意思
#         print("##",end="")
#         b+=1
#     print()
#     row+=1
#
# # 打印正三角形（与函数有关）
# row=1
# while row<=9:
#     a=1
#     while a<=9-row:
#         print(' ',end='')
#         a+=1
#     b=1
#     while b<=2*row-1:#各种函数的意思
#         print("#",end="")
#         b+=1
#     print()
#     row+=1
#beak 终止
# sum=0
# for item in range(1,101):
#     sum+=item
#     if sum>100:
#         print('执行到%d就停止了'%item)
#         break
#         pass
#     pass
# print("sum=%d"%sum)
# 升级版猜拳
# a=1
# win=0
# debit=0
# pingju=0
# chengfa=0
# import random  # 引入随机数
# while True:
#     if win==3:
#         print('可恶，N局三胜，ai输了，你赢了。。。')
#         break
#     elif debit==3:
#         print('哈哈哈哈，N局三胜，ai赢了，你输咯。。。')
#         break
#     elif pingju==5:
#         print('哼，千年不得一见的场景，算了，今日收兵，来日再战！(ai/you)')
#         break
#     elif chengfa==5:
#         print('请不要玩弄ai,现已触犯惩罚机制,禁止您操作3min,并退出本次游戏.')
#         break
#     else:
#         person = int(input('你出什么呢...（1石头 2剪刀 3布）'))  # 想把输入的中文定义为数字，电脑好判断
#         # 石头=0,剪刀=1,布=2#为啥布能定义，前两者不行
#         if person>3 or person<1:
#             print("请正确输入数字1,2,3")
#             chengfa+=1
#         else:
#             computer = random.randint(1, 3)
#             if computer == person:
#                 print('computer出了%d' % computer)
#                 print('哟，跟我想的一样，平局')
#                 pingju+=1
#                 pass
#             elif (computer == 1 and person == 2):
#                 print('computer出了%d' % computer)
#                 print('嘿嘿，我赢啦！')
#                 debit += 1
#                 pass
#             elif (computer == 2 and person == 3):
#                 print('computer出了%d' % computer)
#                 debit += 1
#                 print('嘿嘿，我赢啦！')
#             elif (computer == 3 and person == 1):
#                 print('computer出了%d' % computer)
#                 print('嘿嘿，我赢啦！')
#                 debit += 1
#             else:
#                 print('computer出了%d' % computer)
#                 print('霍，看来你技高一筹，我输了')
#                 win+=1
#                 pass
#             print('N局三胜已进行%d次\n                 '%a)
#             a+=1
