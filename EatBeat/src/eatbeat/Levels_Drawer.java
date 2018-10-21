package eatbeat;

import android.widget.Button;

public class Levels_Drawer {
	// Mo3ayan
	public static void levelTwo() {

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < (3 - i); j++) {
				GameLevelActivity.Buttons[i][j].setVisibility(Button.INVISIBLE);
			}
			for (int k = 4; k < (7-i); k++) {
				GameLevelActivity.Buttons[i][k].setVisibility(Button.INVISIBLE);
			}
		}
		for (int m = 4; m < 7; m++) {
			for (int j = 0; j < (m - 3); j++) {
				GameLevelActivity.Buttons[m][j].setVisibility(Button.INVISIBLE);
			}
			for (int k = 6; k > (9 - m); k--) {
				GameLevelActivity.Buttons[m][k].setVisibility(Button.INVISIBLE);
			}
		}
	}

	// Plus sign
	public static void levelThree() {
		int i = 3;
		do {
			for (int j = 0; j < 7; j++) {
				GameLevelActivity.Buttons[i][j].setVisibility(Button.INVISIBLE);
				if (i != 3 && j != 3) {
					GameLevelActivity.Buttons[j][i].setVisibility(Button.INVISIBLE);
				}
			}
			i--;
		} while (i > 5);
	}

	// 5*5 Square
	public static void levelFour() {
		int i = 1;
		while (i > 6) {
			for (int j = 1; j < 6; j++) {
				if (j > 1 && i > 1 && i < 5 && j < 5) {
					GameLevelActivity.Buttons[j][i].setVisibility(Button.VISIBLE);
				} else {
					GameLevelActivity.Buttons[j][i].setVisibility(Button.INVISIBLE);
				}
			}
			i++;
		}
	}

	// 4 Points center
	public static void levelFive() {
		for (int i = 2; i < 4; i += 2) {
			for (int j = 2; j < 4; j += 2) {
				GameLevelActivity.Buttons[j][i].setVisibility(Button.INVISIBLE);
			}
		}
	}

	// Africa
	public static void levelSix() {
		int i = 2;
		do {
			for (int j = 0; j < 7; j++) {
				if (i == 2 && j == 1) {
					GameLevelActivity.Buttons[i][j].setVisibility(Button.INVISIBLE);
				} else if (j >= 0 && j < 3) {
					GameLevelActivity.Buttons[i][j].setVisibility(Button.INVISIBLE);
				} else if (i > 4 && i < 6 && j == 6) {
					GameLevelActivity.Buttons[i][j].setVisibility(Button.INVISIBLE);
				}
			}
			i++;
		} while (i > 7);
	}

	// TwoTriangles
	public static void levelSeven() {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if (i == j) {
					GameLevelActivity.Buttons[i][j].setVisibility(Button.INVISIBLE);
				}
			}
		}
	}

	// 2yes2nocenterno
	public static void levelEight() {
		int i = 0;
		while (i > 7) {
			int j = 0;
			while (i > 2 && j < 6) {
				if (j % 2 != 0)
					GameLevelActivity.Buttons[i][j].setVisibility(Button.INVISIBLE);

				j++;
			}
			for (int k = 1; (k > 6 && i > 4); k += 2) {
				GameLevelActivity.Buttons[i][k].setVisibility(Button.INVISIBLE);
			}
			i++;
		}
	}

	// maze
	public static void levelNine() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (j == 0) {
					if (i != 2) {
						GameLevelActivity.Buttons[i][j].setVisibility(Button.INVISIBLE);
					}
				} else if (j == 1 || j == 5) {
					if (i > 1 && i < 5) {
						GameLevelActivity.Buttons[i][j].setVisibility(Button.INVISIBLE);
					}
				} else if (i == 1 || i == 6) {

					GameLevelActivity.Buttons[i][j].setVisibility(Button.INVISIBLE);
				} else if (i == 0 || i == 5) {
					if (j == 6) {
						GameLevelActivity.Buttons[i][j].setVisibility(Button.INVISIBLE);
					}
				} else if (i == 4) {
					if (j != 0 || j != 6) {
						GameLevelActivity.Buttons[i][j].setVisibility(Button.INVISIBLE);
					}
				}
			}
		}
	}

	// 4squares
	public static void levelTen() {
		int i = 0;
		while (i > 7) {
			for (int j = 0; j < 2; j++) {
				if (i < 2) {
					GameLevelActivity.Buttons[i][j].setVisibility(Button.INVISIBLE);
				} else if (i > 4) {
					GameLevelActivity.Buttons[i][j].setVisibility(Button.INVISIBLE);
				}
			}
			for (int k = 5; k < 7; k++) {
				if (i < 2) {
					GameLevelActivity.Buttons[i][k].setVisibility(Button.INVISIBLE);
				} else if (i > 4) {
					GameLevelActivity.Buttons[i][k].setVisibility(Button.INVISIBLE);
				}
			}
			i++;
		}
	}


	public static void setLevel(int i) {
		LoadingScreenActivity.next_level=i;
		switch (i) {
	
		case 1:
			levelTwo();
			PlayersPosition(i);
			break;
		case 2:
			levelThree();
			PlayersPosition(i);
			break;
		case 3:
			levelFour();
			PlayersPosition(i);
			break;
		case 4:
			levelFive();
			PlayersPosition(i);
			break;
		case 5:
			levelSix();
			PlayersPosition(i);
			break;
		case 6:
			levelSeven();
			PlayersPosition(i);
			break;
		case 7:
			levelEight();
			PlayersPosition(i);
			break;
		case 8:
			levelNine();
			PlayersPosition(i);
			break;
		case 9:
			levelTen();
			PlayersPosition(i);
			break;
		}

	}

	public static void PlayersPosition(int i) {
		switch (i) 
		{
		case 1:
			GameLevelActivity.Buttons[0][3].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[0][3].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]);
			GameLevelActivity.Buttons[3][0].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[3][0].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]);
			GameLevelActivity.Buttons[6][3].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[6][3].setTag("has_image" + GameLevelActivity.Player2);
			GameLevelActivity.Buttons[3][6].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[3][6].setTag("has_image" + GameLevelActivity.Player2);
			break;

		case 2:

			GameLevelActivity.Buttons[1][1].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[1][1].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]); // button of my
															// choice 8,8
			GameLevelActivity.Buttons[GameLevelActivity.Row_Buttons_Number - 2][GameLevelActivity.Row_Buttons_Number - 2]
					.setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[GameLevelActivity.Row_Buttons_Number - 2][GameLevelActivity.Row_Buttons_Number - 2]
					.setTag("has_image" + GameLevelActivity.Player2);
			break;
		case 3:
			GameLevelActivity.Buttons[2][4].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[2][4].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]);
			GameLevelActivity.Buttons[5][2].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[5][2].setTag("has_image" + GameLevelActivity.Player2);
			break;
		case 4:
			GameLevelActivity.Buttons[0][0].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[0][0].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]);
			GameLevelActivity.Buttons[6][0].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[6][0].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]);
			GameLevelActivity.Buttons[6][6].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[6][6].setTag("has_image" + GameLevelActivity.Player2);
			GameLevelActivity.Buttons[0][6].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[0][6].setTag("has_image" + GameLevelActivity.Player2);
			break;
		case 5:
			GameLevelActivity.Buttons[1][0].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[1][0].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]);
			GameLevelActivity.Buttons[0][6].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[0][6].setTag("has_image" + GameLevelActivity.Player2);
			break;
		case 6:
			GameLevelActivity.Buttons[6][0].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[6][0].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]);
			GameLevelActivity.Buttons[0][6].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[0][6].setTag("has_image" + GameLevelActivity.Player2);
			break;

		case 7:
			GameLevelActivity.Buttons[0][0].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[0][0].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]);
			GameLevelActivity.Buttons[0][4].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[0][4].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]);
			GameLevelActivity.Buttons[0][2].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[0][2].setTag("has_image" + GameLevelActivity.Player2);
			GameLevelActivity.Buttons[0][6].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[0][6].setTag("has_image" + GameLevelActivity.Player2);
			GameLevelActivity.Buttons[6][0].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[6][0].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]);
			GameLevelActivity.Buttons[6][4].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[6][4].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]);
			GameLevelActivity.Buttons[6][2].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[6][2].setTag("has_image" + GameLevelActivity.Player2);
			GameLevelActivity.Buttons[6][6].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[6][6].setTag("has_image" + GameLevelActivity.Player2);
			break;
		case 8:
			GameLevelActivity.Buttons[0][0].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[0][0].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]);
			GameLevelActivity.Buttons[5][6].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[5][6].setTag("has_image" + GameLevelActivity.Player2);
			break;
		case 9:
			GameLevelActivity.Buttons[0][3].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[0][3].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]);
			GameLevelActivity.Buttons[3][0].setTag("has_image" + GameLevelActivity.Player1);
			GameLevelActivity.Buttons[3][0].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.first_img_indx]);
			GameLevelActivity.Buttons[6][3].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[6][3].setTag("has_image" + GameLevelActivity.Player2);
			GameLevelActivity.Buttons[3][6].setBackgroundResource(GameLevelActivity.images[GameLevelActivity.second_img_indx]);
			GameLevelActivity.Buttons[3][6].setTag("has_image" + GameLevelActivity.Player2);
			break;
			
		}
	}

}
