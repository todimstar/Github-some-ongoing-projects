#include<stdio.h>
#include<string.h>
#include<stdlib.h>

int map[19][19];

int flag;

void init();

int isWin(int x, int y);

int playerMove(int x, int y);

void menuView();

void gameView_ShowMap();

void winView();

void gameView();
// -------------------- view --------------------
int main()
{
	int testflag = 0;
	//init测试代码
	
	init();
	if (flag != 0) {
	printf("init()错误");
	exit(0);
	}
	for (int i = 0; i < 19; i++) {
	for (int j = 0; j < 19; j++) {
	if (map[i][j]) {
	printf("init()错误");
	exit(0);
	}
	}
	}
	printf("init()测试成功\n");
	testflag++;
	
	//playerMove测试代码
	
	int result = 1;
	result &= playerMove(2, 2);
	result &= playerMove(2, 3);
	result &= playerMove(2, 4);
	result &= playerMove(2, 5);
	if (result != 1 || (map[2][2] && map[2][3] && map[2][4] && map[2][5]) != 1)
	{
	printf("playerMove()错误");
	exit(0);
	}
	flag = 1;
	result &= playerMove(2, 5);
	if (result != 0 || map[2][5] != 1) {
	printf("playerMove()错误");
	exit(0);
	}
	printf("playerMove()测试成功\n");
	testflag++;
	
	//isWin测试代码
	
	playerMove(2, 1);
	if (isWin(2, 1)) {
	printf("isWin()错误");
	exit(0);
	}
	playerMove(1, 0);
	playerMove(3, 2);
	playerMove(4, 3);
	playerMove(5, 4);
	if (isWin(1, 0) != 2) {
	printf("isWin()错误");
	exit(0);
	}
	printf("isWin()测试成功\n");
	testflag++;
	
	if (testflag == 3) {
		printf("service代码测试成功\n");
	}getchar();
	menuView();
	system("pause");
	return 0;
}
// -------------------- service --------------------
/*
将棋盘的值初始化为0
当前回合设为黑棋(flag设为0)
*/
void init(){
	memset(map, 0, sizeof(map));
	flag = 0;
}
/*
功能: 根据传入的坐标(map对应位置)和flag值 判断落点后是否获胜
返回值:
0表示没有获胜
1表示黑子胜利
2表示白子胜利
*/
int isWin(int x, int y){
	/*int i, j,k,q,p;
	int lag;
	for (i = 0; i < 19; i++){
		for (j = 0; j < 19; j++){
			//行历遍判断
			if (map[i][j] == 1){//断白子(1)
				lag = 1;//找到第一个白
				for (k = j + 1; k<j+5 && k<19; k++){//看后4个是不是都是1;
					if (map[i][k] != 1){
						//lag = 1;//可以省了，也不用再多赋值一次了反正每次找到个新的开头都会lag=1的
						
						break;//发现有个不是就跑
					}
					else lag++;//是就+1看看实力
				}if (lag == 5)return 1;//白子胜
			}
			if (map[i][j] == 2){//断黑子(2)
				lag = 1;
				for (k = j + 1; k<j + 5 && k<19; k++){//同上
					if (map[i][k] != 2){
						//lag = 1;
						break;
					}
					else lag++;
				}if (lag == 5)return 2;//黑子胜
			}
			//列历遍判断
			if (map[j][i] == 1 ){//断白子
				lag = 1; 
				for (k = j + 1; k < j + 5 && k < 19; k++){
					if (map[k][i] != 1){
						break;
					}
					else lag++;
				}if (lag == 5)return 1;//白子胜
			}
			if (map[j][i] == 2){//断黑子
				lag = 1;
				for (k = j + 1; k < j + 5 && k < 19; k++){
					if (map[k][i] != 2){
						//lag = 1;
						break;
					}
					else lag++;
				}if (lag == 5)return 2;//黑子胜
			}
		}
	}
	//判断左斜
	for (i = 0; i < 19 - 5; i++){//倒数后5列不用跑,凑不够5个位子成一排
		
		for (j = i,k=0; j < 19 && k<(19-i); j++,k++){//可以画图想想除了对角线是走满19格的,其他斜的走到哪就出棋局了
			if (map[j][k] == 1){//白
				lag = 1;
				for (q = j + 1, p = k + 1; q < 19 && p < (19 - i); q++, p++){
					if (map[q][p] != 1)break;
					else lag++;
				}if (lag == 5)return 1;
			}

			if (map[j][k] == 2){//黑
				lag = 1;
				for (q = j + 1, p = k + 1; q < 19 && p < (19 - i); q++, p++){
					if (map[q][p] != 2)break;
					else lag++;
				}if (lag == 5)return 2;
			}

			if (map[k][j] == 1){
			lag = 1;
				for (q = j + 1, p = k + 1; q < 19 && p < (19 - i); q++, p++){
					if (map[p][q] != 1)break;
					else lag++;
				}if (lag == 5)return 1;
			}

			if (map[k][j] == 2){
				lag = 1;
				for (q = j + 1, p = k + 1; q < 19 && p < (19 - i); q++, p++){
					if (map[p][q] != 2)break;
					else lag++;
				}if (lag == 5)return 2;
			}//对角线多走了一次  1.要不在最开始就把对角线搞了
		}
	}
	//判断右斜
	//for
	*/
	//int m = 0, n = 0;//m表示黑子，n表示白子，1才赢,0还没赢
	int count1 = 0;
	int count2 = 0;
	int i, j;
	//根据最后一个落子遍历行判断...666666666
	if (flag % 2 == 0) {
		for (j = 0; j < 19; j++) {
			if (map[x][j] == 1) {
				count1++;
			}
		}if (count1 == 5) {
			return 1;

		}
	}if (flag % 2 == 1) {
		for (j = 0; j < 19; j++) {
			if (map[x][j] == 2) {
				count2++;
			}
		}if (count2 == 5) {
			return 2;
		}
	}
	//根据最后一个棋子遍历列判断
	count1 = 0; count2 = 0;//记得清零
	if (flag % 2 == 0) {
		for (i = 0; i < 19; i++) {
			if (map[i][y] == 1) {
				count1++;
			}
		}if (count1 == 5) {
			return 1;

		}
	}if (flag % 2 == 1) {
		for (i = 0; i < 19; i++) {
			if (map[i][y] == 2) {
				count2++;
			}
		}if (count2 == 5) {
			return 2;
		}
	}
	//左斜判断...
	/*if (x - 4 >= 0 && x + 4 < 19){
		count1 = 0; count2 = 0;
		for (i = x - 4,j = y-4; i < x + 4&&j<y+4; i++,j++){
			if (map[i][j] == 1){
				count1++;
			}
			else if (map[i][j] == 2){
				count2++;
			}
		}if (count1 == 5){
			return 1;
		}
		else if (count2 == 5){
			return 2;
		}
	}
	if (x - 4 < 0){
		count1 = 0; count2 = 0;
		for (i = 0, j = y - 4; i < x + 4 && j<y + 4; i++, j++){
			if (map[i][j] == 1){
				count1++;
			}
			else if (map[i][j] == 2){
				count2++;
			}
		}if (count1 == 5){
			return 1;
		}
		else if (count2 == 5){
			return 2;
		}
	}*/
	int yy = y, xx = x;
	while (yy >= 1 && xx >= 1){
		xx--; yy--;
	}count1 = 0; count2 = 0;

	for (i = xx, j = yy; i < 19 && j < 19; i++, j++){
		if (map[i][j] == 1)count1++; //count2 = 0;
		else break;//count2++; count1 = 0; 
	}if (count1 == 5)return 1;

	for (i = xx, j = yy; i < 19 && j < 19; i++, j++){
		if (map[i][j] == 2)count2++; 
		else break;
	}if (count2 == 5)return 2;

	//右斜判断
	yy = y, xx = x;
	while (yy >= 1 && xx < 18){
		xx++; yy--;
	}count1 = 0; count2 = 0;
	for (i = xx, j = yy; i >0 && j < 19; i--, j++){
		if (map[i][j] == 1)count1++;
		else break;
	}if (count1 == 5)return 1;
	for (i = xx, j = yy; i >0 && j < 19; i--, j++){
		if (map[i][j] == 2)count2++;
		else break;
	}if (count2 == 5)return 2;
	return 0;//没胜利 
}
/*在指定位置落子，如果map[x][y]是空地，就改为相应颜色
x为当前回合落子的x坐标，y为当前回合落子的y坐标
返回值，0表示落子失败（已经有子），1表示落子成功*/
int playerMove(int x, int y){
	if (map[x][y] == 0 && flag % 2 == 0) {
		map[x][y] = 1; return 1;
	}
	else if (map[x][y] == 0 && flag % 2 == 1) {
		map[x][y] = 2; return 1;
	}
	else { return 0; }
}
/*
功能: 展示选项, 玩家可以在这里选择进入游戏, 进入设置或退出游戏
进入游戏: 调用游戏界面函数gameView();
进入设置: 敬请期待...
退出游戏: 调用exit(0);
*/
void menuView(){
	
	int m;//输入m==0就是进入游戏，m==1进入设置，m==2退出游戏
	printf("输入0进入游戏\n输入1进入设置\n输入2退出游戏");
	scanf_s("%d", &m);//============================================================14:00添加此处
	if (m == 0) {
		gameView();//=============================================================14:16添加此处
		/*printf("请输入坐标（x，y）");
		scanf("%d %d", &x, &y);//=================================================14:11添加此处
		playerMove(x, y);*/
	}
	else if (m == 1) {
		printf("设置功能未开发,敬请期待\n");//=======================================13:50添加此处
	}
	else { printf("游戏已退出\n"); exit(0); };
}
/*
负责人: 张三
功能: 根据map数组 打印游戏棋盘
参数: void
返回值: void
*/
void gameView_ShowMap(){
	int i, j;
	for (i = 0; i < 19; i++){
		for (j = 0; j < 19; j++){//=======输出棋盘
			printf("%d ", map[i][j]);
		}printf("\n");
	}if (flag % 2 == 0)printf("接下来是黑棋落子\n");//===提示更清晰;
	else{ printf("接下来是白棋落子\n"); }
}
/*
负责人: 张三
功能: 根据flag的值 打印游戏胜利界面 用户可以按任意键回到主菜单
参数: void
返回值: void
*/
void winView(){
	//int count;//=============================================================14:44修改
	if (flag % 2 != 0 ) {
		printf("恭喜黑棋赢了\n");
	}
	else if (flag % 2 == 0) { printf("恭喜白棋赢了\n"); }
	printf("按任意键回到主菜单");
	getchar();//按任意键
	menuView();//回到主菜单
	return;
}
/*
*难点2
负责人: 张三
功能: 游戏界面整合
初始化游戏数据(调用函数init())
while(1){
打印游戏界面(调用函数gameView_ShowMap())
接收玩家坐标输入
落子(调用落子函数playerMove())
(如果落子失败 重新开始循环)
判断游戏是否胜利(调用胜利判断函数isWin())
(如果游戏胜利 调用胜利界面函数 然后结束当前界面)
切换玩家(修改flag值)
}
参数: void
返回值: void
*/
void gameView(){//============================================================14:21开始编写
	int x, y;
	init();
	while (1){
		system("cls");
		gameView_ShowMap();
		printf("请输入落子坐标（x，y）");
		scanf_s("%d %d", &x, &y);//接收玩家坐标输入
		if (playerMove(x, y) == 0){
			printf("落子失败,请重新落子\n"); system("pause"); continue;//(如果落子失败 重新开始循环)
		}
		if (isWin(x, y) > 0){//判断游戏是否胜利(调用胜利判断函数isWin())(如果游戏胜利 调用胜利界面函数 然后结束当前界面)
			winView();
		}
		else{
			++flag;//切换玩家(修改flag值)
		}
		
	}
}
