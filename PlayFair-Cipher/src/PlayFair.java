import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PlayFair {

	static char[][] matrix = new char[5][5];
	static boolean Zflag = false; // if a Z is added at the end of the string to make it even numbered
	static boolean Sflag = false; // if there are spaces in plain text
	static ArrayList<Integer> Sindex = new ArrayList<Integer>(); // to know index of removed spaces
	static boolean Jflag = false; // if there is a 'j' in text that will be changed to 'i'
	static ArrayList<Integer> Jindex = new ArrayList<Integer>(); // to know index of changed 'j'
	static Character[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	static ArrayList<Character> array = new ArrayList<Character>();

	public static ArrayList<Character> find(String key) {
		array.addAll(Arrays.asList(alphabet));
		for (int i = 0; i < key.length(); i++) {
			if (array.contains(Character.toUpperCase(key.charAt(i)))) {
				int index = array.indexOf(Character.toUpperCase(key.charAt(i)));
				array.remove(index);
			} else {
				continue;
			}
		}
		return array;
	}

	public static void fill_Matrix(String key) {
		int x = 0; // counter for key alphabets to fill first in matrix
		int z = 0; // counter for rest of alphabets from arraylist to fill in matrix
		String modifiedKey = key.replace('J', 'I');
		ArrayList<Character> rawKey = new ArrayList<Character>();
		for (int count = 0; count < modifiedKey.length(); count++) {
			if (!rawKey.contains(modifiedKey.charAt(count))) {
				rawKey.add(modifiedKey.charAt(count));
			} else {
				continue;
			}
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (x < rawKey.size()) {
					matrix[i][j] = Character.toUpperCase(rawKey.get(x));
					x++;
				} else {
					ArrayList<Character> remaining_alphabets = new ArrayList<Character>();
					remaining_alphabets = find(modifiedKey);
					char value = remaining_alphabets.get(z);
					matrix[i][j] = value;
					z++;
				}
			}
		}
	}

	public static String check_string(String plainText) {
		StringBuffer new_plainText = new StringBuffer(plainText);
		for (int i = 0; i < plainText.length(); i++) {
			if (i + 1 < plainText.length()) {
				Character Fchar = new Character(plainText.charAt(i));
				Character Schar = new Character(plainText.charAt(i + 1));
				if (Fchar.equals(Schar)) {
					new_plainText.insert(i + 1, 'X');
				}
			} else {
				break;
			}
		}
		if (new_plainText.length() % 2 != 0) {
			plainText = new_plainText + "Z";
			Zflag = true;
		} else {
			plainText = new_plainText.toString();
		}
		for (int count = 0; count < plainText.length(); count++) {
			if (plainText.charAt(count) == 'J') {
				Jflag = true;
				Jindex.add(count);
			}
		}
		String modifiedText = plainText.replace('J', 'I');
		return modifiedText;
	}

	public static int[] find_Index_in_Matrix(String pair) {
		int[] indecies = new int[4];
		char Fchar = pair.charAt(0);
		char Schar = pair.charAt(1);
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (matrix[i][j] == Fchar) {
					indecies[0] = i; // First char row index
					indecies[1] = j; // First char col index
				} else if (matrix[i][j] == Schar) {
					indecies[2] = i; // Second char row index
					indecies[3] = j; // Second char col index
				} else {
					continue;
				}
			}
		}
		return indecies;
	}

	public static String encryption(String plainText) {
		int count = 0;
		String new_plainText = check_string(plainText);
		String cipherText = "";
		String[] pairs = new String[new_plainText.length() / 2];
		String[] new_pairs = new String[new_plainText.length() / 2];
		// Making them pairs
		for (int i = 0; i < new_plainText.length(); i += 2) {
			pairs[count] = Character.toString(new_plainText.charAt(i))
					+ Character.toString(new_plainText.charAt(i + 1));
			count++;
		}
		for (int j = 0; j < pairs.length; j++) {
			String pair = pairs[j];
			int[] characters_index = find_Index_in_Matrix(pair);
			int FCrow = characters_index[0];
			int FCcol = characters_index[1];
			int SCrow = characters_index[2];
			int SCcol = characters_index[3];
			// First rule (same col)
			if (FCcol == SCcol) {
				if (FCrow < 4 && SCrow < 4) {
					new_pairs[j] = Character.toString(matrix[FCrow + 1][FCcol])
							+ Character.toString(matrix[SCrow + 1][SCcol]);
				} else if (FCrow < 4 && SCrow == 4) {
					new_pairs[j] = Character.toString(matrix[FCrow + 1][FCcol]) + Character.toString(matrix[0][SCcol]);
				} else if (FCrow == 4 && SCrow < 4) {
					new_pairs[j] = Character.toString(matrix[0][FCcol]) + Character.toString(matrix[SCrow + 1][SCcol]);
				} else {
					continue;
				}
			}
			// Second rule (Same row)
			else if (FCrow == SCrow) {
				if (FCcol < 4 && SCcol < 4) {
					new_pairs[j] = Character.toString(matrix[FCrow][FCcol + 1])
							+ Character.toString(matrix[SCrow][SCcol + 1]);
				} else if (FCcol < 4 && SCcol == 4) {
					new_pairs[j] = Character.toString(matrix[FCrow][FCcol + 1]) + Character.toString(matrix[SCrow][0]);
				} else if (FCcol == 4 && SCcol < 4) {
					new_pairs[j] = Character.toString(matrix[FCrow][0]) + Character.toString(matrix[SCrow][SCcol + 1]);
				} else {
					continue;
				}
			}
			// Third rule (Not on same row or col)
			else {
				new_pairs[j] = Character.toString(matrix[FCrow][SCcol]) + Character.toString(matrix[SCrow][FCcol]);
			}
			cipherText += new_pairs[j];
		}
		// System.out.println(Arrays.toString(pairs));
		return cipherText;

	}

	public static String decryption(String cipherText) {
		String plainText = "";
		int count = 0;
		String[] pairs = new String[cipherText.length() / 2];
		String[] new_pairs = new String[cipherText.length() / 2];
		// Making them pairs
		for (int i = 0; i < cipherText.length(); i += 2) {
			pairs[count] = Character.toString(cipherText.charAt(i)) + Character.toString(cipherText.charAt(i + 1));
			count++;
		}
		for (int j = 0; j < pairs.length; j++) {
			String pair = pairs[j];
			int[] characters_index = find_Index_in_Matrix(pair);
			int FCrow = characters_index[0];
			int FCcol = characters_index[1];
			int SCrow = characters_index[2];
			int SCcol = characters_index[3];
			// First rule reversed (same col)
			if (FCcol == SCcol) {
				if (FCrow > 0 && SCrow > 0) {
					new_pairs[j] = Character.toString(matrix[FCrow - 1][FCcol])
							+ Character.toString(matrix[SCrow - 1][SCcol]);
				} else if (FCrow > 0 && SCrow == 0) {
					new_pairs[j] = Character.toString(matrix[FCrow - 1][FCcol]) + Character.toString(matrix[4][SCcol]);
				} else if (FCrow == 0 && SCrow > 0) {
					new_pairs[j] = Character.toString(matrix[4][FCcol]) + Character.toString(matrix[SCrow - 1][SCcol]);
				} else {
					continue;
				}
			}
			// Second rule reversed (same row)
			else if (FCrow == SCrow) {
				if (FCcol > 0 && SCcol > 0) {
					new_pairs[j] = Character.toString(matrix[FCrow][FCcol - 1])
							+ Character.toString(matrix[SCrow][SCcol - 1]);
				} else if (FCcol > 0 && SCcol == 0) {
					new_pairs[j] = Character.toString(matrix[FCrow][FCcol - 1]) + Character.toString(matrix[SCrow][4]);
				} else if (FCcol == 0 && SCcol > 0) {
					new_pairs[j] = Character.toString(matrix[FCrow][4]) + Character.toString(matrix[SCrow][SCcol - 1]);
				} else {
					continue;
				}
			}
			// Third rule reversed (Not on same row or col)
			else {
				new_pairs[j] = Character.toString(matrix[FCrow][SCcol]) + Character.toString(matrix[SCrow][FCcol]);
			}
			plainText += new_pairs[j];
		}
		StringBuffer new_plainText = new StringBuffer(plainText);
		// change I back to J
		if (Jflag == true) {
			for (int n = 0; n < Jindex.size(); n++) {
				int num = Jindex.get(n);
				new_plainText.setCharAt(num, 'J');
			}
		}
		// remove Z added to make length even number
		if (Zflag == true) {
			new_plainText.deleteCharAt(new_plainText.length() - 1);
		}
		// remove any added X for duplication
		for (int i = 0; i < new_plainText.length(); i++) {
			if (new_plainText.charAt(i) == 'X') {
				if (i == 0 || i == new_plainText.length() - 1) {
					continue;
				} else {
					char C1 = new_plainText.charAt(i - 1);
					char C2 = new_plainText.charAt(i + 1);
					if (C1 == C2) {
						new_plainText.deleteCharAt(i);
					}
					continue;
				}
			}
		}
		// add removed spaces
		if (Sflag == true) {
			for (int s = 0; s < Sindex.size(); s++) {
				int space = Sindex.get(s);
				new_plainText.insert(space, ' ');
			}
		}
		String FinalplainText = new_plainText.toString();
		return FinalplainText;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Enter Key:");
		Scanner scan = new Scanner(System.in);
		String key = scan.nextLine();
		System.out.println("Enter Plain Text:");
		String plainText = scan.nextLine();
		if (plainText.contains(" ")) {
			Sflag = true;
			for (int i = 0; i < plainText.length(); i++) {
				if (plainText.charAt(i) == ' ') {
					Sindex.add(i);
				}
			}
		}
		String noSpaceplainText = plainText.replaceAll("\\s", "");
		fill_Matrix(key.toUpperCase());
		System.out.print("\n");
		System.out.println("Matrix:");
		System.out.println("-------------");
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				System.out.print(matrix[i][j] + ", ");
			}
			System.out.print("\n");
		}
		System.out.println("-------------");
		System.out.print("\n");
		System.out.println("Encrypting:");
		System.out.println("-------------");
		String cipher = encryption(noSpaceplainText.toUpperCase());
		System.out.println("Cipher Text: " + cipher);
		System.out.println("-------------");
		System.out.print("\n");
		System.out.println("Decrypting:");
		System.out.println("-------------");
		String dec = decryption(cipher.toUpperCase());
		System.out.println("Plain Text: " + dec);
		System.out.println("-------------");
		scan.close();
	}
}