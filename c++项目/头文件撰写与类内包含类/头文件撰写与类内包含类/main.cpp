#include<iostream>
using namespace std;
#include"circle+point.h"
#include<time.h>


int main(){
	clock_t start_time = clock();

	point cen;cen .setXY(0, 0);
	circle oo; oo.setR(1); oo.setcenter(cen);
	point op;op .setXY(1, 1);
	double R = oo.getR();
	double D = (op.getX() - oo.getcenter().getX())*(op.getX() - oo.getcenter().getX()) + (op.getY() - oo.getcenter().getY())*(op.getY() - oo.getcenter().getY());
	if (R*R>D)//两点之间距离公式:sqrt((x1-x2)^2+(y1-y2)^2);
	{
		printf("op在圆oo内\n");
	}
	else if (R*R == D)printf("这小op在圆oo上\n");
	else printf("好家伙,小op在圆oo外\n");
	clock_t end_time = clock();
	printf("运行了%.10fms", (end_time - start_time)*1.0); /// CLOCKS_PER_SEC);
	system("pause");
	return 0;
}