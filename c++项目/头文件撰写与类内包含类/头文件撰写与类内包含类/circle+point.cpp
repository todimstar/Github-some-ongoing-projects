#include"circle+point.h"

void point::setX(double X){ x = X; }
void point::setY(double Y){ y = Y; }
void point::setXY(double X, double Y){ x = X; y = Y; }
double point::getX(){ return x; }
double point::getY(){ return y; }



void circle::setR(double r){ m_r = r; };
void circle::setcenter(point c){ center = c; }
double circle::getR(){ return m_r; }
point circle::getcenter(){ return center; }
