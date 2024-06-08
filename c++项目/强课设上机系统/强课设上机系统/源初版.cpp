#define _CRT_SECURE_NO_WARNINGS 1
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<time.h>

void beginViewer_01();
void xiaji();
int shangxian();
//long time1();
//char* time2();
double MoneyUsing(long int t_compare);
void Introduction();
void Record();

struct zhanghao {   //定义账号结构体（用户，密码）,其中奇数行为账号，偶数行为密码
	char yonghu[30];
	char mima[30];
};

struct record {
	struct zhanghao zhanghao;
	char shangji[30];   //上机时间
	char xiaji[30];    //下机时间
	double money;
};
struct record student;

void xiaji(time_t t_start) {
	int num = 0;
	double h = 0;
	printf("\n公主王子，您正在上机\n");
	do {
		printf("扣6下机，返回主界面，请输入:");
		scanf("%d", &num);
		if (num == 6) {
			//long t1 = time1();
			//char t2[100] = time2();
			//t1 /= 3600;
			time_t t;
			time(&t);
			struct tm* tmp_time = localtime(&t);
			char s[100];
			strftime(s, sizeof(s), "%Y.%m.%d %H:%M:%S", tmp_time);
			long int t_compare = (long)(t - t_start);
			printf("\n---下机时间为:%s---\n---共使用:%d秒---\n", s, t_compare);
			//student.xiaji[0] = s;
			strcpy(student.xiaji, s);

			double money1 = MoneyUsing(t_compare);
			printf("此次上机花费为：%.2lf元。", money1);
			student.money = money1;
			//strcpy(student.money,money1);
			//system("pause");
			FILE* fp2 = fopen("record.txt", "a");
			if (fp2 == NULL) {
				perror("文件打开失败：");
				return;
			}
			/*fputs(student.zhanghao.yonghu, fp2);
			fputs(student.zhanghao.mima, fp2);
			fputs(student.shangji, fp2);
			fputs(student.xiaji, fp2);*/
			fprintf(fp2,"账号%s :", student.zhanghao.yonghu);
			fprintf(fp2,"上机时间：%s ", student.shangji);
			fprintf(fp2, "下机时间：%s \n", student.xiaji);

			fclose(fp2);
			
			system("pause");
			beginViewer_01();
		}
		else {
			printf("请输入正确数字");
		}
	} while (num != 6);
}
int shangxian() {   //用于上机登录的函数
	//思路：两个一组，两个循环，第一个循环是输入用户名是否记录在
	//系统内，否则需要重新输入。第二个循环是密码输入对了没有

	char z1[20] = { 0 };   //z1为用户名输入
	FILE* fp1 = fopen("zhanghao.txt", "r");
	char c1[20] = { 0 };
	int flag = 0, flag1 = 1;
	while (flag1) {
		printf("---账号登录---\n");
		printf("请输入用户名:\n");
		scanf("%s", z1);   //输入用户名
		do {
			if (fgets(c1, sizeof(c1), fp1) == NULL) {
				break;
			}
			c1[strlen(c1) - 1] = '\0';
			if (strcmp(c1, z1) == 0) {
				flag = 1;
				//student.zhanghao.yonghu[20] = c1;
				strcpy(student.zhanghao.yonghu, c1);
				break;
			}
		} while (fgets(c1, sizeof(c1), fp1) != NULL);
		if (flag == 0) {
			rewind(fp1);
			printf("用户不存在，请重新输入\n按1继续输入,按0返回主界面\n");//准备退出通道
			int a;
			scanf("%d", &a);
			if (a == 0) {
				return 0;
			}
		}
		else flag1 = 0;
	}
	char z2[20] = { 0 };   //z2为密码输入
	char c2[20] = { 0 };
	fgets(c2, sizeof(c2), fp1);
	c2[strlen(c2) - 1] = '\0';
	fclose(fp1);
	flag1 = 1;
	while (flag1) {
		system("cls");//清屏
		printf("---账号登录---\n");
		printf("用户:%s\n", c1);
		printf("请输入密码\n");
		scanf("%s", z2);
		if (strcmp(c2, z2) == 0) {
			flag1 = 0;
			//student.zhanghao.mima = c2;//好像char类型是不能直接赋值的,string才行
			strcpy(student.zhanghao.mima, c2);
			printf("---登录成功,欢迎使用---\n");
			printf("扣6开始上机\n");
			int num;
			scanf("%d", &num);
			if (num == 6) {
				//long t1 = time1();
				//char t2[100] = time2();
				time_t t;
				time(&t);
				struct tm* tmp_time = localtime(&t);
				char s[100];
				strftime(s, sizeof(s), "%Y.%m.%d %H:%M:%S", tmp_time);
				printf("---此次于%s时刻上机---", s);
				//student.shangji = s;
				strcpy(student.shangji, s);
				long int t_start = t;
				xiaji(t_start);
			}
			else {
				printf("密码错误，请重试");//按0返回主页面
				system("pause");
			}
		}
		
	}
}
//long time1(){   //返回秒数的函数
//	time_t t;
//	time(&t);
//	struct tm* tmp_time = localtime(&t);
//	char s[100];
//	strftime(s, sizeof(s), "%Y.%m.%d %H:%M:%S", tmp_time);
//    printf("%ld: %s\n", t, s);
//	return t;
//}
//char* time2() {   //返回时间戳的函数
//	time_t t;
//	time(&t);
//	struct tm* tmp_time = localtime(&t);
//	char s[100];
//	strftime(s, sizeof(s), "%Y.%m.%d %H:%M:%S", tmp_time);
//	printf("%ld: %s\n", t, s);
//	return s;
//}
double MoneyUsing(long int t_compare){//用于计算费用的函数
	double money;
	long int t_hour;
	if (t_compare > 0 && t_compare <= 3600) {
		return 1;
	}
	t_hour = t_compare / 3600;  //不足一小时按一小时算
	if (t_hour % 3600 != 0) {
		t_hour++;
	};
	money = t_hour * 1;
	return money;
}
void Introduction() {   //功能介绍函数
	printf("正确输入用户名和密码后，可以进行上机\n");
	printf("上下机时,请输入当前时间，用于费用记录\n");
	printf("费用为每小时一元，不足一小时,按一小时算\n");
	system("pause");
	beginViewer_01();
	return;
}
void beginViewer_01() {   //主界面函数
	int flag = 1;
	while (flag) {
		system("cls");
		int num1 = 0;
		printf("**************************\n");
		printf("*   机房上机模拟系统     *\n");
		printf("*                        *\n");
		printf("*      1.用户登录        *\n");
		printf("*      2.功能介绍        *\n");
		printf("*      3.上机记录        *\n");
		printf("*      4.退出系统        *\n");
		printf("*                        *\n");
		printf("*     (!感谢使用！)      *\n");
		printf("**************************\n");
		printf("请输入：");
		scanf_s("%d", &num1);
		switch (num1) {
		case 1:if (shangxian() != 0)flag = 0; break;
		case 2:Introduction(); break;
		case 3:Record(); break;
		case 4:exit(0); break;
		default:printf("请输入正确的数字再试\n");
			//system("pause");
		printf("怎么又给你小子找到漏洞了...记一下你刚才的操作报告给开发者吧\n");
		}
		system("pause");
	}
}
void Record() {
	char c1;
	FILE* fp2 = fopen("record.txt", "r+");
	while ((c1 = fgetc(fp2)) != EOF) {
		printf("%c", c1);
	}fclose(fp2);
	return;
}
int main()
{
	//FILE* fp = fopen("账户.txt", "r");
	beginViewer_01();
	//fclose(fp);
	return 0;
}
