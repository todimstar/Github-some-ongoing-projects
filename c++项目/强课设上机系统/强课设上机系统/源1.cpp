#include<stdio.h>  
#include<stdlib.h>  
#include<string.h>  

struct zhanghao {
	char yonghu[20];
	char mima[20];
};

struct record {
	int shangji_hour;
	int shangji_minute;
	int xiaji_hour;
	int xiaji_minute;
	double money;
};

void shangxian() {
	char z1[20] = { 0 };
	char z2[20] = { 0 }; // 用于存储密码  
	FILE* fp_account = NULL;
	
	errno_t err = fopen_s(&fp_account, "zhanghao.txt", "r+");
	

	if (fp_account == NULL ) {
		printf("Error opening file.\n");
		system("pause");
		return;
	}

	struct zhanghao account;
	

	while (fscanf_s(fp_account, "%19s", account.yonghu) != EOF) {
		fscanf_s(fp_account, "%19s",account.mima);

		printf("请输入用户名: ");
		scanf_s("%19s", z1, sizeof(z1));
		printf("请输入密码: ");
		scanf_s("%19s", z2, sizeof(z2));

		if (strcmp(account.yonghu, z1) == 0 && strcmp(account.mima, z2) == 0) {
			printf("登录成功！\n");
			// 记录上机时间，跳转到主菜单或其他操作  
			break;
		}
		else{ printf("账号或密码错误，请重试!\n"); }
	}

	fclose(fp_account);
	
}

void xiaji() {
	// 实现下机功能，记录下机时间并计算费用  
}

void Introduction() {
	// 实现功能介绍  
}

void checkRecord() {
	// 实现查看上机记录功能  
}
void beginViewer_01() {

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

}

int main() {
	int choice;

	do {
		beginViewer_01();
		scanf_s("%d", &choice);
		switch (choice) {
		case 1:
			shangxian();
			break;
		case 2:
			Introduction();
			break;
		case 3:
			checkRecord();
			break;
		case 4:
			printf("退出系统。\n");
			break;
		default:
			printf("无效选择，请重新输入。\n");
		}
	} while (choice != 4);

	return 0;
}

