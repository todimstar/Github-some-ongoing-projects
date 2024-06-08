#include<iostream>

using namespace std;//  函数重载：可以让函数名相同，提高复用性
//条件:1.同一作用域   2.函数名相同
//     3.参数类型不同，或个数不同，或顺序不同 
//     ps:返回类型不同不能作为重载条件 

void func() {
	cout << "func" << endl;
}
/*
void func(int a){//         个数不同 
	cout << "int a->func" << endl;
}
*/
void func(int b, double c) {//       需要重载的函数最好不要有默认参数，因为有一个就占两个区别个数,比如 double c=10){ 则与上一个函数冲突有二义性 
	cout << "int b,int c->func" << endl;
}
//
void func(double b, int c){ cout << "啥?" << endl; }//  顺序不同 

void func(double a){//       类型不同 
	cout << "double a捏" << endl;
}


//      函数重载的注意事项: 1.引用也能当类型不同?   貌似dev的int a跟int &a一样的捏 

void func(int &a) {
	cout << ++a << endl;
}
void func(const int &a){
	cout << "const int " << a << endl;//    const int &a 和int &a都能被 int a 冲掉;不过int &a 和 const int &a 还是不一样的
}

int main(){
	int a = 8;
	func(a);
	func(8);
	printf("main中的a是%d", a);
	system("pause");
	return 0;
}