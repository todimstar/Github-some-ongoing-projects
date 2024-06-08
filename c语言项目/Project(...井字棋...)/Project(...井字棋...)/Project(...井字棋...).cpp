#include<stdio.h>
#include<easyx.h>

int a;
char board_data[3][3] = { { '-', '-', '-' }, { '-', '-', '-' }, { '-', '-', '-' } };
char current_piece = 'o';

//检测指定玩家是否获胜
bool CheckWin(char c){
	int win=0,winl=0;
	for(int j=0;j<3;j++){
		for(int i=0;i<3;i++){
			if(board_data[j][i]==c){//行胜利
				win++;
			}
			if (board_data[i][j] == c){//列胜利
				winl++;
			}
		}
		if(win==3 || winl==3){
			return true;
		}else{
			win = 0; winl = 0;
		}
	}win=0;int win1=0;
	for(int i=0;i<3;i++){//左右斜胜利
		if(board_data[i][i]==c){
			win++;
		}if(board_data[i][2-i]==c){
			win1++;
		}
	}if(win==3 || win1==3){
		return true;
	}
	
	return false;
}

//检测是否出现平局
bool CheckDraw(){
	int flag=0;
	for(int i=0;i<3;i++){
		for(int j=0;j<3;j++){
			if(board_data[i][j]=='-'){
				return false;
			}
		}
	
	}return true;
}

//绘制棋局网络
void DrawBoard(){
	line(0,200,600,200);
	line(0,400,600,400);
	line(200,0,200,600);
	line(400,0,400,600);
}

//绘制棋子
void DrawPiece(){
	for(int i=0;i<3;i++){
		for(int j=0;j<3;j++){
			switch(board_data[i][j])
			{
				case'o':
					circle(200*j+100,200*i+100,100);
					break;
				case'x':
					line(200*j,200*i,200*(j+1),200*(i+1));
					line(200*(j+1),200*i,200*j,200*(i+1));
					break;
				case'-':
					break;
				default:
					break;
			}
		}
	}
}

//绘制提示信息
void DrawTipText(){
	static TCHAR str[64];
	_stprintf_s(str, _T("当前要下子类型为:%c"), current_piece);//搜搜
	settextcolor(RGB(225, 175, 45));
	outtextxy(0, 0, str);
}


int _tmain(int argc, _TCHAR* argv[])
{
	again:
	for (int i = 0; i < 3; i++){
		for (int j = 0; j < 3; j++){
			board_data[i][j] = '-';
		}
	}
	initgraph(600, 600);
	
	bool running = true;

	ExMessage msg;
	
	BeginBatchDraw();

	while (running){
		DWORD start_time = GetTickCount();
		while (peekmessage(&msg))
		{
			//检查鼠标左键按下信息
			if (msg.message == WM_LBUTTONDOWN){
				//计算点击位置
				int x = msg.x;
				int y = msg.y;

				int index_x = x / 200;
				int index_y = y / 200;
				//尝试落子
				if (board_data[index_y][index_x] == '-'){
					board_data[index_y][index_x] = current_piece;
					//切换棋子类型
					if (current_piece == 'o'){
						current_piece = 'x';
					}
					else current_piece = 'o';
				}
			}
		}

		cleardevice();

		DrawBoard();
		DrawPiece();
		DrawTipText();

		FlushBatchDraw();


		if (CheckWin('x'))
		{
			
			a = MessageBox(GetHWnd(), _T("x 玩家获胜 Again ?"), _T("Game Over!"), MB_OKCANCEL | MB_SYSTEMMODAL);
			if (a == IDOK)
			{
				goto again;  // 游戏重新初始化
			}
			else if (a == IDCANCEL)
			{
				exit(0);
			}
		}
		else if (CheckWin('o'))
		{
			a = MessageBox(GetHWnd(), _T("o 玩家获胜 Again ?"), _T("Game Over!"), MB_OKCANCEL | MB_SYSTEMMODAL);
			if (a == IDOK)
			{
				goto again;  // 游戏重新初始化
			}
			else if (a == IDCANCEL)
			{
				exit(0);
			}
		}
		else if (CheckDraw())
		{
			a = MessageBox(GetHWnd(), _T("平局,6 Again ?"), _T("Game Over!"), MB_OKCANCEL | MB_SYSTEMMODAL);
			if (a == IDOK)
			{
				goto again;  // 游戏重新初始化
			}
			else if (a == IDCANCEL)
			{
				exit(0);
			}
		}

		DWORD end_time = GetTickCount();
		DWORD cha_time = end_time - start_time;

		if (cha_time < 1000 / 60){
			Sleep(1000 / 60 - cha_time);
		}

	}EndBatchDraw();

	return 0;
}

