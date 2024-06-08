
#include<easyx.h>
#include<stdio.h>
#include<stdlib.h>
#include<iostream>

#include<time.h>




int main(){


	//printf("%s\n", GetEasyXVer());
	initgraph(1280, 720);
	setorigin(640, 360);
	setaspectratio(1,-1);
	
	//solidcircle(0, 0, 150);//ÃÓ≥‰‘≤;
	circle(0, 0, 300);//øÚº‹‘≤
	putpixel(0,0, RED);
	//putpixel(10, 0, BLACK);
	int x=0, y=0;
	/*srand((unsigned int)time(NULL));
	for(int i=0;i<1000;i++){
		x = rand() % (1280+1) - 640;
		y = rand() % (720+1)-360;
		putpixel(x, y, WHITE);
	}*/ //ÀÊª˙µ„
	BeginBatchDraw();
	ExMessage msg;
	while (true){
		while (peekmessage(&msg)){
			if (msg.message == WM_MOUSEMOVE){
				x = msg.x-640; y = (-1)*msg.y+360;
				//cleardevice();
				solidcircle(x, y, 50);
				FlushBatchDraw();
			}
			
		}
	}EndBatchDraw();
	closegraph();
	
	return 0;
}