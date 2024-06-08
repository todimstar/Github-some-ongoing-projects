#include<iostream>
#include<cstdio>
#include<cstdlib>
#include<string>

using namespace std;

struct player_account{
	string number;//账号
	string account_key;//账号密码
	string name;//昵称
	size_t Player_uid;
	struct player_account* next=NULL;
};
typedef struct player_account* List;
List L=NULL, last=NULL, newNode=NULL;


int show_account(List a, int visited);
int show_allaccount();

void showMenu()
{
	printf("*** 欢迎来到账号登录系统***\n");
	cout << "---\t1.登录账号\t---" << endl;
	//忘记密码,登录后修改密码,修改昵称****
	cout << "---\t2.注册账号\t---" << endl;
	//密保问题**
	cout << "---\t3.显示所有账号\t---" << endl;
	//管理员后台*
	cout << "---\t0.退出系统\t---" << endl;
	printf("***************************\n");
}
void false_into(){
	printf("Plase check your into\nlet we again\n"); system("pause");
}
void showinit()
{
	system("cls");      
	cout << "---\t账号注册\t---" << endl;
	    
	cout << "---\t-----------\t---" << endl;
}

int init_account()
{	again:
	system("cls");
	cout << "---\t账号注册\t---" << endl;
	cout << "\t1.开始注册" << endl;
	//cout << "\t2.设置账号密码" << endl;
	//cout << "\t3.输入账号昵称" << endl;
	cout << "\t[0].返回主页面" << endl;
	cout << "---\t-----------\t---" << endl;
	char ch=getchar();
	switch (ch)
	{
	case '1':{
				 showinit();

				 newNode = new player_account;//不能malloc，因为结构里有string类，malloc/free无法满足动态对象的要求。newNode = (player_account*)malloc(sizeof(struct player_account));
				 //==========================================================================================这是因为对象在创建的同时需要自动执行构造函数，对象在消亡之前要自动执行析构函数,如果使用malloc/free则要手动完成这些;
				 //===删除账号时记得再delete;

				 if (newNode == NULL){ printf("newNode未正确分配"); return -1; }
				 string t;
				 cout << "注册账号/手机号：";//==暂时没有查重
				 cin >> t; newNode->number = t;
				 printf("\n");
				 cout << "设置账号密码：";
				 cin >> t; newNode->account_key=t;
				 printf("\n");
				 cout << "输入账号昵称：";
				 cin >> t;newNode->name=t;

				 if (L == NULL){
					 L = last = newNode;
					 newNode->Player_uid = 100000000;
				 }
				 else{
					 newNode->Player_uid = last->Player_uid+1;
					 last->next = newNode;
					 last = newNode;
					 
				 }printf("注册成功!\n这是你的uid:%d\n回[0]前往个人中心,按1直接返回登录界面!", newNode->Player_uid);
				 int ttt;cin>>ttt;
				 if(ttt==1) return 2;//返回数2接到登录函数;
				 else if (ttt == 0) show_account(newNode,1);
				 break;
	}
	 
	case '0':return 0;
	case '\n':goto again;
	default:{false_into(); cerr << "无效的输入，请重新输入" << endl; goto again; }
	}
	return -1;//这函数返回-1.0.2,0回主页面,2去登录，-1报错
}

int Are_you_sure(){
	printf("你确定吗? Are you sure?\n1.Yes \t2.NO\n");
	int a;
	cin >> a;
	if (a == 1)return 1;
	else return 0;
}

void showregist(){
	system("cls");
	cout << "---\t账号登录\t---" << endl;

	cout << "---\t-----------\t---" << endl;
}

int show_account(List a,int visited){//visited判断看不看密码,1看,0不看,默认0
	again:
	system("cls");
	cout << "---\t个人中心\t---" << endl;
	cout << "UID:" << a->Player_uid << endl;
	cout << "昵称：" << a->name <<'\n'<< endl;
	cout << "账号：" << a->number << endl;
	if (!visited){
		int t = a->account_key.size();
		cout << "账号密码:";
		while (t--) printf("*"); printf("\n");
		cout << "查看密码请按1,按0返回主页面并退出账号" << endl;
		cin >> t;
		if (t == 1)show_account(a, 1);
		if (t == 0)return 0;
	}
	else{ cout << "账号密码：" << a->account_key << endl;
	cout << "按0返回主页面，并退出个人中心" << endl;
	char t; cin>>t; if (t == '0')return 0;
	else goto again;//只要输的不是0就重新显示;
	}
	return -1;//==return 0;返回regist再0再main主页面;
}

int show_allaccount(){
	List temp = L;
	system("cls");
	cout << "---\t管理员账号后台\t---" << endl;
	while (temp != NULL){
		cout << "UID:" << temp->Player_uid << endl;
		cout << "昵称：" << temp->name <<endl;
		cout << "账号：" << temp->number << endl;
		cout << "账号密码：" << temp->account_key << endl;
		printf("\n"); temp = temp->next;
	}system("pause"); return 0;
}

int regist(){
again:
	system("cls");
	cout << "---\t账号登录\t---" << endl;
	cout << "\t1.开始登录" << endl;
	//cout << "\t2.设置账号密码" << endl;
	//cout << "\t3.输入账号昵称" << endl;
	cout << "\t[0].返回主页面" << endl;
	cout << "---\t-----------\t---" << endl;
	char ch = getchar();
	switch (ch)
	{
	case '1':{
				 showregist();
				 string tnumber;
				 cout << "登录账号/手机号：";//==暂时没有查重
				 cin >> tnumber;
				 //printf("\n");
				 List temp = L; int a = 0;
				 while (temp != NULL){
					 if (temp->number != tnumber){ temp = temp->next; }//printf("temp->number != tnumber\n"); }
					 else break;//cao,竟然是忘了else退出
				 }if (temp == NULL){
					 FILE* fp = NULL;
					 errno_t err = fopen_s(&fp, "zhanghao.txt", "r+");  

					 if (fp == NULL) {
						 printf("error\n");
						 perror("open file failed:");
					 }char c1[100]; int flag = 0;//flag判断是否找到账号 

					 do{// char *fgets(char *str, int n, FILE *stream);
						 printf("1\n");
						 if (fgets(c1, sizeof(c1), fp) == NULL) break;//如果奇数行读到NULL就是到文件末尾了，该走了
						 c1[sizeof(c1)-1] = '\0';
						 if (strcmp(c1, tnumber.c_str()) == 0){
							 flag = 1; printf("ky\n"); break;
						 }

					 } while (fgets(c1, sizeof(c1), fp) != NULL);
					 cout << "查无此号,请检查后再试\n回1重试，按2前往注册,其他(0)则返回主页面" << endl;
					 int t; scanf_s("%d", &t);
					 if (t == 1){ goto again; }
					 else if (t == 2){ return 2; }
					 else return 0;
				 }
				 cout << "输入账号密码：";
				 cin >> tnumber;
				 if (tnumber != temp->account_key){
					 cout << "密码错误，请检查账号或密码是否有误\n任意键后返回登录页面" << endl;
					 system("pause"); goto again;//==没有保留账号重新输入功能
				 }
				 else{
					 if (show_account(temp, 0) == 0) return 0;//返回主页面传送门途经点
					 else  return -1;//能else则show_account出问题了
				 }
				 break;
	}

	case '0':return 0;
	case'\n':goto again;
	default:{false_into(); goto again; }
	}
	return -1;//报错
}


int main()
{
	bool running=true;

	while (running){
		again:
		system("cls");
		showMenu();//展示菜单
		int chose = 0;
		scanf_s("%d", &chose);
		switch (chose)
		{
		case 1:{getchar(); init_to_regist:
		int tt = regist();//接收一下登录返回2还是0
		if (!tt)break;// goto again;//0就重来
		else if (tt == 2)goto regist_to_init;//2就前往注册;
		break;
		}
		case 2:{getchar(); regist_to_init:
		int tt = init_account();
		if (!tt)break;// goto again;
		else if (tt == 2)goto init_to_regist;
		break;
		}
		case 3:{show_allaccount(); //goto again;
			break;
		}
		case 0:{if (!Are_you_sure())break;//goto again;
			   else { cout << "\n再见！See you next time!" << endl; running = false; }
			   break;
		}
		default:{
					false_into(); goto again;
		}
		}
	}

	system("pause");
	return 0;
}
