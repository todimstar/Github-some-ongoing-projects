// ConsoleApplication1(test游戏).cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include<easyx.h>
#include<stdio.h>
#include<stdlib.h>
#include<time.h>
int _tmain(int argc, _TCHAR* argv[])
{
	int x, y,i;
	initgraph(800, 600);//定义窗口像素大小
	setorigin(400, 300);//重选坐标系原点
	setaspectratio(1.5, -1.5);//成倍缩放坐标轴
	circle(0, 0, 250);//圆函数(中心点x,y,半径r)
	for (i = 0; i < 1000; i++){
		//srand(time(NULL));
		x = rand() % (800 + 1) - 400;
		y = rand() % (600 + 1) - 300;
		putpixel(x, y, RED);
	}
	/*putpixel(0, 0, RED);
	putpixel(200, 200, YELLOW);
	putpixel(200, -200, BLUE);
	putpixel(200, -210, CYAN);
	putpixel(-200, 200, GREEN);
	putpixel(-200, -200, WHITE);*/
	getchar();
	closegraph();//关闭窗口;
	return 0;
}

