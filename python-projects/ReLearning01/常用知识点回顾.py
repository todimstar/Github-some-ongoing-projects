# #关于format和%的用法
# # 占位符1  %
# # a=int(input("输入友人的程度..."))
# # print("傻逼六六程度%d"%a)
# # #占位符2   {}  .format
# # c=input("刘雪飞看什么看...")
# # print("对啦程序完成{}%".format(c))
#
#
# # 随机数生成，且范围须定义为整数
# import random#引入随机数函数
# f=random.randrange(-1,1)
# r=random.randint(-20,2)#有randint和randrange两函数，那么区别是什么呢
# print(f)
# print(r)
#
# print("该怎么换行呢\n是这样吗")#换行使用（\n）
#
#
# #       列表方法.append进.remove出.upper大写.sorted()排序.max().min()最大最小都有   #[a:b]---[a,b),右不选中
# #               extend()好像也是进
# #               进整个？  出也可以a[0:3]=[]也是为空列表删除掉
# #               对同类进整个同类     可隔空删除？把中间删掉？---但是不能移除[0:5:2]，因为不能跨着切，哈哈
# #                                     是的，直接移除后拼接，且[0:3:1]是从0到下标位2一共(3-0)个,步长默认为1
# '''增'''
# #append()列表增
# mylist=["你好",1,-90,'ok']
# mylist.append(555)
# print(mylist)
# #extend()同类型续加
# testlist01=['hello',11,'a',False]
# tt=['910车播',21]
# testlist01.extend(tt)
# print(testlist01)
#
# '''删'''
# #remove()单个删
# mylist.remove(1)
# print(mylist)
# #list[a:b]=[]范围删除列表元素
# print(testlist01[2:5:2])
# print(testlist01[2:5])
# testlist01[2:5]=[]    #testlist01[2:5:2]=[]不行
# print(testlist01)
# #del list[a:b]可以范围和分块删除
# del testlist01[0:len(testlist01):2]  #del 可以分块删，=[]不行  #del testlist01[0:2]  #[0:2]---[0,2)
# print(testlist01)
#
# '''改'''
# #正常单个改
# mylist[0]='小鱼'
# #分区切割改，分块改
# mylist[1:3]=['陆远','一班']
# print(mylist)
# mylist[1:4:2]=["战争修士","在你左右"]
# print(mylist)
#
# '''方法'''
# #upper()大写方法
# mylist=["你好",1,-90,'ok']
# mylist.append("what can I say")
# print(mylist[4].upper())
# print(mylist[4])
# #"".join()方法
# print(" --- ".join(map(str,mylist))+"\tmylist长度为"+str(len(mylist)))
# # #摘抄join和enm用法
# # mylist = ['a', 'b', 'c', 'd']
# # # 使用列表推导式和join来合并索引和值
# # result = list(f"Index: {index}, Value: {value}" for index, value in enumerate(mylist))
# # print(result)
#
# mylist=[90,13,4235,543]
#
# sorted_mylist=sorted(mylist)
# max_mylist=max(mylist)
# min_mylist=min(mylist)
#
# print(sorted_mylist)
# print(max_mylist)
# print(min_mylist)


#       字典，dictionary={},空字典，其中为一个个键值对pair<key:value>
curriculum_dict={"中国家发展史课程编码":"114514",
          "生命健康网课要求":"网课就行",
          ("这是元组tuple","其不可变才能做key"):1314159}

#添加删除修改字典中键值对
#添加
curriculum_dict[("test",1)]=2233
#删除
del curriculum_dict[("这是元组tuple","其不可变才能做key")]
#修改--根据key值
curriculum_dict["中国家发展史课程编码"]="覆盖"

#字典的应用，查询字典
# query=input("请输入查询词")
# if query in curriculum_dict:
#     print("字典中"+query+"的意思如下")
#     print(curriculum_dict[query])
# else:
#     print("No found it,please check it right")
#     print("当前课程字典中收录有"+str(len(curriculum_dict))+"节课程")

#for字典.方法.key()键.values()值.items()键值对
for query in curriculum_dict.items():
    #print(query + "," + a)有元组当字典键时不行，得将那个元组str，这两条都是print的锅，直接所有输入全格式化好了
    #print(str(query)+ ", "+ str(a))
    print()
    print(f'{query[0]},{query[1]}') #笑拉了，既然都要转str，直接f'{}'快捷格式化字符串

#for中range应用
for_range=[555,5555434,3535]
for i in range(0,3):
    print(for_range[i])
#还可以这样默认从0开始到参数
for i in range(len(for_range)):
    print(for_range[i])


#while应用
sum=0
cont=0
user = input("你还输入来算平均值吗，输入q结束：")
while user!= "q":
    sum+=float(user)
    cont+=1
    user = input("你还输入来算平均值吗，输入q结束：")
if cont==0: result=0.0
else: result=sum/cont
print(f'平均值是{result}')
#打开文件  with关键字--with open('data2_2.txt')as f:

