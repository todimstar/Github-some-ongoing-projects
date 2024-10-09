# 这是一个示例 Python 脚本。
import math

a = '去古巴比伦当国王！'
print(a + '你会上吗？')
print('试着输出：" Let\'s go "')
print('''山山水水\\n或者直接用\'\'\'或\"\"\"换行,写一首诗吧
难
道
你
手
不会
痛吗？
''')

#常用函数，len()字符串专用判断长度，
BBC = '忍者有多长吗?!'
print("Do you know \"" + BBC + "\"\n是=" + str( len(BBC) ) )

#尝试字符串数组，即二维数组，哈哈好像c方式还行(✿◡‿◡)
Temp = ["双节棍","希望他是真的比我还要爱你"]
print("怎么概~怎么概？呵呵蛤嗨!我使用\n" + Temp[0] + Temp[1][len(Temp[1])-2] + Temp[1][len(Temp[1])-1] )


'''
想要坐直升机~
接下来是数学运算，b**2 就是 b的2次方, c**(1/2) 就是根号c，也可以直接 math.sqrt(c)，还有math.pow(x,y) 即x^y
'''
#Ax^2+Bx+C=0
a=1
b=5
c=1

Delta_x = math.sqrt(b**2 - 4*a*c)

'''
数据类型 type()看类型，None 空值NoneType类型 读音类似NULL，type(None)=NoneType
'''
print(type(None))


'''
#输入input()
'''
print("sin(1)=" + str( math.sin(1) ) )
#              str()转字符串形式，int()字符串转数字 float(),
#BMI计算：体重/(身高**2)
Weight = float(input("\n\n输入你的体重(kg)："))
High = float(input("输入你的身高(m)："))

BMI=Weight/(High**2)
print("Your BMI is " + str(BMI))

print("你是我的",end=" "+"你的谁"+"喂喂喂")

print("文科的{}".format(BMI))