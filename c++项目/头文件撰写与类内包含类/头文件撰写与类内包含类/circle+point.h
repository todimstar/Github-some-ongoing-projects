#pragma once
#include<iostream>
using namespace std;
#define PI 3.14

class point{
public:
	void setX(double X);
	void setY(double Y);
	void setXY(double X, double Y);
	double getX();
	double getY();
private:
	double x, y;
};
class circle{

public:
	void setR(double r);
	void setcenter(point c);
	double getR();
	point getcenter();
private:
	double m_r;//半径 
	point center;//圆心
	double c = 2 * PI*m_r;//周长 
	double s = PI*m_r*m_r;//面积 

};