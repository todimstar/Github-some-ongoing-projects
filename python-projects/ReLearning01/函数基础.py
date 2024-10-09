# # 函数定义：
# # def 函数名():
# #     代码块
#
# import random
# d= random.randrange(0,99999)
# def fengzhuanghanshu():#封装函数
#     '''
#     这个函数是我第一个封装函数 是带一个随机数的函数
#
#     :return:
#     '''
#     print("小李的身高是%dcm" % 170)
#     print("小李的体重是%d斤" % 110)
#     print("小李爱喝%s" % "菠萝啤")
#     print("小李的成绩是%d分" % 482.5)
#     print("小李的钱包余额有%d元" % d)
#     pass
# fengzhuanghanshu()#按住ctrl,鼠标移动到函数可看注释；ctrl+单击函数，可定位到函数
# print("---------------")
# fengzhuanghanshu()
# #参数：改变函数中某数值
# print("---------------")
# def fzhs(name,height,weight,hobby,mark,money):#封装函数
#     print("%s的身高是%dcm" % (name,height))
#     print("%s的体重是%d斤" % (name,weight))
#     print("%s爱%s" % (name,hobby))
#     print("%s的成绩是%d分" % (name,mark))
#     print("%s的钱包余额有%d元" %(name,money))
#     pass
#
# fzhs("刘师傅",153,70,"睡觉偷懒",89,1)
#
# #                          必要参数    ，默认参数【缺省参数】，   可变参数，     关键字参数
# #  （1）必选参数
# #        ↓
# def sum(a,b):#此a，b为形式参数，不占地址
#     print("%d傻叉吃%d屎"%(a,b))
#     pass
#
# sum(1,2)#此1,2为实际参数，占用内存地址，必须赋值的
# sum()#不能这么写，会丢失必要参数
# sum(11)#同样不行，漏了一个
#    (2).   默认参数【缺省参数】（未赋值时兜底的）始终存在于参数列表中尾部，即默认参数后不能再有必要函数
#             ↓
# def ddd(r=1,e=3):
#     print("the day\oooo~~~~\came with %d"%(r-e))
#     pass
# ddd()#未赋值时兜底的
#vv
# def eee(p,w=1,q):
#     print("默认参数始终存在于参数列表中尾部,即默认参数后不能再有必要参数,所以酱紫不行%d"%(w-q))
#     pass
# # eee()  x
# eee(10)#更不行，本来eee就寄了，而且必要参数当然要填啦
#
# def qqq(t,f=1):
#     print("磐岩结绿%d"%(t+f))
#     pass
# qqq(,2)#会无效语法

#     (3).  可变参数
def yuanshen(*args,**dic):
    '''
    *变量--可变参数，元组形式存储
    只能用于计算累加和？只能走循环？24.9.4不然嘞哈哈一年前的我
    :param args: 可变长参数 *元组
    dic：关键字可变参数 **字典
    两者都用于不知道输入的长度时使用
    :return:
    '''
    result=1    #累乘需要初始为1
    for item in args:
        result *= item #*=就是累乘 +=就是累加
    print(args)     #输出传入元组
    print(dic)
    print(result)
    pass
yuanshen(9,3)

#9.4判断质数
def zhishu(num):
    if num in range(0,4):return True
    for i in range(num//2,num):
        if(num % i == 0): return False
    return True
print(zhishu(4))