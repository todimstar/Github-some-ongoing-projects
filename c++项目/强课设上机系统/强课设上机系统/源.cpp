/*题目54：机房上机模拟系统
【说明及要求】
根据用户输入的账号和密码，判断用户是否合法，如果是合法用户则记录用户的账号、上机时间，
如果为非法用户则提示账号或密码错误，请重试。
用户上机结束后，记录用户的下机时间，并计算费用（设每小时时间费用为1元）。
【提示】
（1）用两个文件分别存放用户账号（用户账号、密码），上机记录（账号、上机时间、下机时间、费用）；
（2）定义两个结构体分别表示用户账号和上机记录；
（3）编写函数分别实现上机和下机功能；*/

#include<stdio.h>
#include<stdlib.h>
#include<string.h>

struct zhanghao {   //定义账号结构体（用户，密码）,其中奇数行为账号，偶数行为密码
	char yonghu[20];
	char mima[20];
};
/*struct zhanghao student1 = { "jisuaunji2301", "jianzhudaxue0912" };
struct zhanghao student2 = { "jiusuanji2302", "jianzhudaxue0707" };
struct zhanghao student3 = { "dashuju2301", "jisuanji0424" };*/
struct record {
	int shangji_hour;
	int shangji_minute;   //上机时间（时,分）
	int xiaji_hour;
	int xiaji_minute;   //下机时间(时,分)
	double money;
};
struct record student;

void shangxian() {   //用于上机登录的函数
	//思路：两个一组，两个循环，第一个循环是输入用户名是否记录在
	//系统内，否则需要重新输入。第二个循环是密码输入对了没有

	char z1[20] ;   //z1为用户名输入
	//int shu;   //shu为触发下机操作的
	printf("请输入用户名:\n");
	scanf_s("%19s",z1);   //输入用户名
	
	FILE* fp=fopen( "zhanghao.txt", "r+");
	
	if (fp == NULL) {
		printf("error\n");
		perror("open file failed:");
		return;
	}
	char c1[100]; int flag = 0;//flag判断是否找到账号 
	
	do{// char *fgets(char *str, int n, FILE *stream);
		printf("1\n");
		if (fgets(c1, sizeof(c1), fp) == NULL) break;//如果奇数行读到NULL就是到文件末尾了，该走了
		c1[sizeof(c1)-1] = '\0'; 
		if (strcmp(c1, z1)==0){
			flag = 1; printf("ky\n"); break;
		}

	} while (fgets(c1, sizeof(c1), fp) != NULL);//这里又一次调用了fgets可以算把偶数行跳过了
	/*char z2[20] = { 0 };   //z2为密码输入
	if (strcmp(c1, z1) == 0) {
		printf("请输入密码");
		scanf("%s", &z2);
		char c2[20] = { 0 };
		while (c2[20] = fgets(c2[0], sizeof(c2[0]), fp) != EOF) {
			c2[20] = fgets(c2[0], sizeof(c2[0]), fp);       //怎么才能在用户名正确的情况下比较那个组的密码？
		}
		if (strcmp(c2, z2) == 0) {
			printf("登录成功,欢迎使用");
			printf("请输入上机时间:");
			scanf("%d %d", &student.shangji_hour, &student.shangji_minute);
		}
		else {
			printf("密码错误，请重试");
		}*/
		printf("输入000进行下机操作");
	}

	/*void xiaxian() {
	printf("请输入下机时间");
	scanf("%d %d", );
	}*/
	int MoneyUsing(int begin_hour, int end_hour, int begin_minute, int end_minute)
	{//用于计算费用的函数
		double money;
		int time = end_hour - begin_hour;
		if ((end_minute - begin_minute) > 0)   //不足一小时按一小时算
			time++;
		else if ((end_minute - begin_minute) < 0) {
			time--;
		}
		money = time * 1;
		return money;
	}
	void Introduction() {
		printf("正确输入用户名和密码后，可以进行上机\n");
		printf("上下机时,请输入当前时间，用于费用记录\n");
		printf("费用为每小时一元，不足一小时,按一小时算\n");
		return;
	}
	void beginViewer_01() {
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
			case 1:shangxian(); break;
			case 2:Introduction(); break;
			//case 3:略
			case 4:exit(0); break;
		}
	}
	void Viewer_02() {
		int num2 = 0;
		printf("**************************\n");
		printf("*                        *\n");
		printf("*        继续上机        *\n");
		printf("*        下机操作        *\n");
		printf("*                        *\n");
		printf("**************************\n");

		return;
	}
	int main() {
		
		/*if(fp == NULL) {
		perror("open file failed:");
		return -1;
		}*/
		
		beginViewer_01();
		/*double money = MoneyUsing(student.shangji_hour,student.xiaji_hour, student.shangji_minute, student.xiaji_minute);
		printf("此次上机花费为：%lf", money);*/
		return 0;
	}
	