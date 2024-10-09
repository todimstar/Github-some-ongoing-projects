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
#         print('请不要玩弄ai,现已触犯惩罚机制,禁止您操作3min,并退出本次游戏.')#待完事项：禁止操作......
#         break
#     else:
#         person = int(input('你出什么呢...（1石头 2剪刀 3布）'))  # 想把输入的中文定义为数字，电脑好判断
#         # 石头=0,剪刀=1,布=2#为啥布能定义，前两者不行
#         if person>3 or person<1:
#             print("请正确输入数字1,2,3")
#             chengfa+=1
#         else:
#             computer = random.randint(1, 3)#引入随机后应用随机数
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

# for 99乘法表

# for i in range(1,10):
#     for o in range(1,i+1):
#         print('%d*%d=%d '%(o,i,i*o),end='')
#         pass
#     print()
#     print()
#     pass

# day2作业

# xunhuan=1
# while xunhuan>=1:
#     age=int(input('猜猜我的年龄是...'))
#     if age==18:
#         print('我的年龄确实是18。恭喜你猜对啦！' )
#         break
#         pass
#     elif (xunhuan%3)==0:
#         print('嘿嘿，您已经玩了%d次了，请问您还想玩下去吗'%xunhuan)
#         yesno=str(input('还想玩y,不想玩了n...'))
#         if yesno=="y" or yesno=="Y":
#             continue
#             pass
#         elif yesno=="n" or yesno=="N":
#             break
#         else:
#             print("bug还需修复,先默认你不玩了吧，嘿嘿")
#             break
#     else:
#         xunhuan+=1
#         pass
# 成功啦

# day2作业计算BMI

# print('         智能BMI计算系统')
# hang=float(input('请输入您的身高(m)...'))
# weight=float(input('请输入您的体重(kg)...'))
# BMI=weight/(hang*hang)
# if BMI<18.5 and BMI>0:
#     print('过轻')
# elif BMI>=18.5 and BMI<25:
#     print("正常")
# elif BMI>=25 and BMI<28:
#     print("过重")
# elif BMI>=28 and BMI<=32:
#     print("肥胖")
# elif BMI>32:
#     print("严重肥胖")
# else:
#     print('请输入正常人类的身高体重...')
# 如何去循环重新开始
# 看着是成功了


#列表--数组的尝试
# c=['这是一个非空列表',245,7678] #这是字典？看着说是列表
# c=[46]
# print(c)
# a=(1,[[1,0],875,3333])
#
# print(a)
#
# print(a[-3:-1])

# a=[1,'san']
# print('a的id地址为%d'%id(a))
# b=a
# print("b的id地址为%d"%id(b))
# c="qi"
# print(c.find('吸血鬼'))#list没有find函数，find在字符串中,同时find()中也必须是str，有就返位置，无就返-1
# #index也是查找
